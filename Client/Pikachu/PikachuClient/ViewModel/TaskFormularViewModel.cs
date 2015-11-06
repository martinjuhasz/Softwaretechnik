using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Windows.Input;
using Microsoft.Win32;
using PikachuClient.ViewModel.ComponentTypes;
using PikachuLib;
using PikachuLib.Communication.Event;
using PikachuLib.Models.TaskComponent;
using PikachuLib.Models.Workflow;
using PikachuViewBase.Hilfsklassen;

namespace PikachuClient.ViewModel
{
    /// <summary>
    /// ViewModel dass ein TaskFormular repräsentiert.
    /// </summary>
    public class TaskFormularViewModel : ViewModelBase
    {
        #region properties

        /// <summary>
        /// Legt fest, ob das Formular angezeigt wird.
        /// </summary>
        private bool isEnabled;
        public bool IsEnabled
        {
            get { return isEnabled; }
            private set
            {
                if (isEnabled != value)
                {
                    isEnabled = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Aktueller Task
        /// </summary>
        private Task currenTask;
        
        /// <summary>
        /// Gibt den aktuellen Taskname zurück.
        /// </summary>
        private string taskName;
        public string TaskName
        {
            get { return taskName; }
            set
            {
                if (taskName != value) 
                {
                    taskName = value;
                    OnPropertyChanged();
                }
            }
        }

        
        /// <summary>
        /// Gibt den aktuellen Workflowname zurück.
        /// </summary>
        private string workflowName;
        public string WorkflowName
        {
            get { return workflowName; }
            set
            {
                if (workflowName != value)
                {
                    workflowName = value;
                    OnPropertyChanged();
                }
            }
        }

        
        /// <summary>
        /// Gibt die aktuelle Job-ID zurück.
        /// </summary>
        private int jobId;
        public int JobId
        {
            get { return jobId; }
            set
            {
                if (jobId != value)
                {
                    jobId = value;
                    OnPropertyChanged();
                }
            }
        }

        
        /// <summary>
        /// Gibt eine Error-Message zurück, wenn ein Fehler aufgetreten ist.
        /// </summary>
        private string errorMessages;
        public string ErrorMessages
        {
            get { return errorMessages; }
            set
            {
                if (errorMessages != value)
                {
                    errorMessages = value;
                    OnPropertyChanged();
                }
            }
        }

        
        /// <summary>
        /// Gibt die ViewModel-Liste der Formularfelder des Tasks zurück.
        /// </summary>
        private ObservableCollection<ComponentViewModelBase> components;
        public ObservableCollection<ComponentViewModelBase> Components
        {
            get { return components; }
            set
            {
                if (components != value)
                {
                    components = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region commands

        /// <summary>
        /// Command zum speichern des Jobs.
        /// </summary>
        private ICommand saveTaskCommand;
        public ICommand SaveTaskCommand
        {
            get
            {
                if (saveTaskCommand == null)
                {
                    saveTaskCommand = new ActionCommand(x => Close(true), x => CheckComponentsValid());
                }
                return saveTaskCommand;
            }
        }

        /// <summary>
        /// Command zum abbrechen der aktuellen Bearbeitung.
        /// </summary>
        private ICommand abortCommand;
        public ICommand AbortCommand
        {
            get
            {
                if (abortCommand == null)
                {
                    abortCommand = new ActionCommand(x=> Close());
                }

                return abortCommand;
            }
        }

        #endregion

        #region constructor

        ~TaskFormularViewModel()
        {
            try
            {
                Debug.Print("destrucot taskformular called");
                ClientSession.Instance.RelaxoClient.UnlockJob(currenTask.Id, jobId);
            }
            catch (Exception ex)
            {
                Debug.Print("Fehler beim unlock job: {0}", ex.Message);
            }
            finally
            {
                IsEnabled = false;
            }
        }

        public TaskFormularViewModel()
        {
            components = new ObservableCollection<ComponentViewModelBase>();
            isEnabled = false;
            ClientSession.Instance.CurrentWorkflowChangedEvent += InstanceOnCurrentWorkflowChangedEvent;
        }


        public TaskFormularViewModel(int taskId, int jobId)
            :this()
        {
            LoadJobTask(taskId, jobId);
        }

        #endregion

        #region methods

        /// <summary>
        /// Ruft einen Task vom Server ab und füllt die Models mit Werten von dem aktuellen Job.
        /// </summary>
        /// <param name="taskId">Task ID.</param>
        /// <param name="jobId">Die Job ID.</param>
        public void LoadJobTask(int taskId, int jobId)
        {
            try
            {
                ErrorMessages = String.Empty;

                JobId = jobId;
                WorkflowName = ClientSession.Instance.CurrentWorkflow.Name;

                
                var task = ClientSession.Instance.RelaxoClient.GetTask(taskId);
                var jobs = ClientSession.Instance.RelaxoClient.GetJobDetail(jobId, task.Id);

                // lock job
                ClientSession.Instance.RelaxoClient.LockJob(task.Id, jobId);

                var jobValues = jobs.JobTasks.FirstOrDefault(); // kann nur ein Jobtask sein.
                currenTask = task;
                TaskName = task.Name;

                Components.Clear();
                foreach (var formGroup in task.FormGroups)
                {
                    foreach (var taskComponent in formGroup.Components)
                    {
                        var component = TaskComponentViewModelFactory.CreateAndFill(taskComponent, jobValues);
                        Components.Add(component);
                    }
                }

                IsEnabled = true;
            }
            catch (Exception ex)
            {
                ErrorMessages = string.Format("Fehler beim Laden des Tasks:\r\n{0}", ex.Message);
                Debug.Print(ErrorMessages);
                Debug.Print(ex.StackTrace);
            }
        }


        /// <summary>
        ///  Prüft, ob alle Eingabefelder valide sind.
        /// </summary>
        /// <returns>True, wenn kein Fehler vorhanden ist, ansonsten false.</returns>
        private bool CheckComponentsValid()
        {
            return Components.Cast<IDataErrorInfo>().All(errInfo => String.IsNullOrEmpty(errInfo.Error));
        }

        /// <summary>
        /// Schliest die Formularansicht.
        /// </summary>
        /// <param name="save">True, wenn die Werte gespeichert werden sollen.</param>
        private void Close(bool save = false)
        {
            if (save)
            {
                try
                {
                    foreach (var item in components)
                    {
                        item.SaveValues();
                    }

                    ClientSession.Instance.RelaxoClient.UpdateJob(currenTask, JobId);
                }
                catch (Exception ex)
                {
                    ErrorMessages = string.Format("Fehler beim speichern:\r\n{0}", ex.Message);
                    Debug.Print(ErrorMessages);
                    Debug.Print(ex.StackTrace);
                    return;
                }
            }

            // unlock item.
            try
            {
                if (currenTask != null)
                {
                    ClientSession.Instance.RelaxoClient.UnlockJob(currenTask.Id, jobId);
                }
            }
            catch (Exception e)
            {
                Debug.Print("Fehler beim unlock job: {0}", e.Message);
            }
            finally
            {
                IsEnabled = false;
                
            }
        }


        private void InstanceOnCurrentWorkflowChangedEvent(WorkflowChangedEventArgs workflowChangedEventArgs)
        {
            Close();
        }

        #endregion
    }
}
