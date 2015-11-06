using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net.Mime;
using System.Windows;
using System.Windows.Data;
using System.Windows.Input;
using PikachuLib;
using PikachuLib.Communication;
using PikachuLib.Models.Job;
using PikachuLib.Models.Workflow;
using PikachuViewBase.Hilfsklassen;
using System.Timers;
using System.Text;
using System.Threading;
using PikachuLib.Communication.Event;

namespace PikachuClient.ViewModel
{
    public delegate void NavigateToTaskJobDelegate(int taskId, int jobId);

    /// <summary>
    /// Repräsentation eines einzelnen Items in der Jobliste.
    /// </summary>
    public class JobListElementView : ViewModelBase
    {
        #region properties

        private NavigateToTaskJobDelegate navigateToTaskJob;

        /// <summary>
        /// Der Task, an dem der job ist.
        /// </summary>
        public Task Task { get; private set; }

        /// <summary>
        /// Der aktuelle Job.
        /// </summary>
        public Job Job { get; private set; }

        /// <summary>
        /// TaskId des Task, an dem der job ist.
        /// </summary>
        public int TaskId 
        {
            get { return Task.Id; }
        }

        /// <summary>
        /// Taskname des Task, an dem der job ist.
        /// </summary>
        public string TaskName { get { return Task.Name; } }

        /// <summary>
        /// Die Jobid des jobs.
        /// </summary>
        public int JobId { get { return Job.Id; } }

        /// <summary>
        /// String der die Startzeit des Jobs 
        /// </summary>
        public string TimeSince
        {
            get
            { 
                TimeSpan span = DateTime.Now - Job.StartTime;
                return GenerateTimeSpanString(span); 
            }
        }

        #endregion

        #region commands

        /// <summary>
        /// Command um einen Job zum Bearbeiten öffnen.
        /// </summary>
        private ICommand getToJobCommand;
        public ICommand GetToJobCommand
        {
            get
            {
                if (getToJobCommand == null)
                {
                    getToJobCommand = new ActionCommand(x => navigateToTaskJob(Task.Id, JobId), x => !Job.Locked);
                }

                return getToJobCommand;
            }
        }

        #endregion

        #region constructor

        public JobListElementView(Task t, Job j, NavigateToTaskJobDelegate navigateTo)
        {
            Task = t;
            Job = j;
            navigateToTaskJob = navigateTo;
        }

        #endregion

        #region methods

        /// <summary>
        /// Methode um die aktualisierung der Property TimeSience zu triggern.
        /// </summary>
        public void RefreshTime()
        {
            OnPropertyChanged("TimeSince");
        }

        /// <summary>
        /// Generiert einen sprechenden String von einem Timespan.
        /// </summary>
        /// <param name="span">Die Zeitspanne, die ausgegeben werden soll.</param>
        /// <returns>Ein String mit Tage Stunden und Minuten.</returns>
        private string GenerateTimeSpanString(TimeSpan span)
        {
            var result = new StringBuilder();

            // Tage
            if (span.Days > 0)
            {
                result.Append(span.Days);
                if (span.Days > 1)
                {
                    result.Append(" Tage");
                }
                else 
                {
                    result.Append(" Tag");
                }                
            }

            // Stunden
            if (span.Hours > 0)
            {
                result.Append(" "); 
                result.Append(span.Hours);
                result.Append(" Std.");                
            }

            // Minuten
            if (span.Minutes > 0)
            {
                result.Append(" "); 
                result.Append(span.Minutes);
                result.Append(" Min.");
            }

            if (result.Length == 0) 
            {
                result.Append("0 Min.");
            }

            return result.ToString();
        }

        #endregion
    }

    public class JobListViewModel : ViewModelBase
    {
        #region properties

        public event NavigateToTaskJobDelegate NavigateToJobEvent;
        private readonly RelaxoClient client;

        /// <summary>
        /// Timer um die Darstellung der vergangene Zeit zu aktualisieren.
        /// </summary>
        private readonly System.Timers.Timer sinceTimer;
        
        /// <summary>
        /// Darstellungsliste der Jobs.
        /// </summary>
        private readonly List<JobListElementView> jobItemViewModel;

        /// <summary>
        /// Der ausgewählte Job.
        /// </summary>
        public JobListElementView SelectedJob { get; set; }

        /// <summary>
        /// Liste der aktuell angezeigten jobs.
        /// </summary>
        private ListCollectionView myJobs;
        public ListCollectionView MyJobs
        {
            get { return myJobs; }
            set
            {
                if (myJobs == null || !myJobs.Equals(value))
                {
                    myJobs = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Ist der Benutzer dazu berechtigt im aktuellen Workflow einen Job zu erstellen?
        /// </summary>
        private bool userCanCreateJob;
        public bool UserCanCreateJob
        {
            get
            {
                return userCanCreateJob;
            }
            set
            {
                if (!userCanCreateJob.Equals(value))
                {
                    userCanCreateJob = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region commands

        /// <summary>
        /// ICommand für Button "neuen Job erstellen".
        /// Ruft die Methode CreateJob aus RelaxoClient auf und erstellt somit einen neuen Job.
        /// Je nachdem ob der Benutzer die Rechte dazu hat, öffnet sich direkt die Job bearbeiten Ansicht des ersten Tasks.
        /// </summary>
        private ICommand createNewJobCommand;
        public ICommand CreateNewJobCommand
        {
            get
            {
                if (createNewJobCommand == null)
                {
                    createNewJobCommand = new ActionCommand(x =>
                    {
                        // erzeuge neuen Job
                        var createJobResponse = client.CreateJob(ClientSession.Instance.CurrentWorkflow);
                        // Falls User Rechte dazu hat, zeige sofort das Formular des 1. Tasks des Workflows an
                        if (createJobResponse.TaskId != null)
                        {
                            NavigateToJobEvent((int)createJobResponse.TaskId, (int)createJobResponse.JobId);
                        }
                    });
                }

                return createNewJobCommand;
            }
        }
        #endregion

        #region constructor

        public JobListViewModel()
        {
            // alle 10 sek. aktualisieren
            sinceTimer = new System.Timers.Timer(10*1000);
            sinceTimer.Elapsed += TimerElapsed;

            jobItemViewModel = new List<JobListElementView>();
            MyJobs = new ListCollectionView(jobItemViewModel);

            client = ClientSession.Instance.RelaxoClient;

            if (ClientSession.Instance.CurrentWorkflow != null)            
            {
                userCanCreateJob = ClientSession.Instance.CurrentWorkflow.UserCanCreateJob;
            }

            // bisher zeigt es auf die gleiche Methode, da es (noch) keinen Unterschied macht.
            ClientSession.Instance.CurrentWorkflowChangedEvent += WorkflowStateChanged;
            ClientSession.Instance.CurrentWorkflowStateChangedEvent += WorkflowStateChanged;
        }

        #endregion

        #region methods

        /// <summary>
        /// Methode um zu einem Job zu Navigieren.
        /// </summary>
        /// <param name="taskId">Der Task zum gewünschten job.</param>
        /// <param name="jobId">Der Job zu dem navigiert werden soll.</param>
        private void NavigateToJob(int taskId, int jobId)
        {
            NavigateToJobEvent(taskId, jobId);
        }

        /// <summary>
        /// Timer Elabsed um die Darsttellung der Jobs zu aktualisieren.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void TimerElapsed(object sender, ElapsedEventArgs e)
        {
            foreach (var job in jobItemViewModel)
            {
                job.RefreshTime();
            }
            
        }

        /// <summary>
        /// Aktualisiert die Jobliste.
        /// </summary>
        public void RefreshJobList()
        {
            Application.Current.Dispatcher.Invoke(() =>
            {
                Debug.Print("Refresh JobList");
            
                sinceTimer.Stop();
                var workflow = ClientSession.Instance.CurrentWorkflow;
                if (workflow == null) return;

                jobItemViewModel.Clear();
                var tasks = client.GetTasks(workflow);
                foreach (var task in tasks)
                {
                    foreach (var job in client.GetJobs(task))
                    {
                        if (job.Active)
                        {
                            jobItemViewModel.Add(new JobListElementView(task, job, NavigateToJob));
                        }
                    }
                }

                jobItemViewModel.Sort((view, elementView) => view.Job.StartTime.CompareTo(elementView.Job.StartTime));

                // FIX: muss neu gesetzt werden, da sich ansonsten die view nicht aktualisiert!
                MyJobs = new ListCollectionView(jobItemViewModel);
                sinceTimer.Start();
            
            });
        }

        /// <summary>
        /// Methode um bei einem Workflow-wechsel bzw. aktualisierung die Jobliste neu zu laden.
        /// </summary>
        /// <param name="arg">Das WorkflowEvent objekt.</param>
        private void WorkflowStateChanged(WorkflowEventArgs arg)
        {
            RefreshJobList();
            if (ClientSession.Instance.CurrentWorkflow != null)
            {
                UserCanCreateJob = ClientSession.Instance.CurrentWorkflow.UserCanCreateJob;
            }
        }

        #endregion
    }
}
