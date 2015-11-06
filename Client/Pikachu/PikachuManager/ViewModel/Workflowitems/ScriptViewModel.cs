using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows.Input;
using PikachuLib;
using PikachuLib.Models.TaskComponent;
using PikachuLib.Models.Workflow;
using PikachuViewBase.Hilfsklassen;

namespace PikachuManager.ViewModel.Workflowitems
{
    /// <summary>
    /// ViewModel für ein Script Workflowitem.
    /// </summary>
    public class ScriptViewModel : WorkflowItemViewModel
    {
        #region properties

        /// <summary>
        /// Gibt den Python-Code des Scripts zurück.
        /// </summary>
        private string scriptContent;
        public string ScriptContent 
        {
            get { return scriptContent; }
            set
            {
                if (!string.Equals(scriptContent, value))
                {
                    scriptContent = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Liste aller Variablen des Scripts.
        /// </summary>
        private ObservableCollection<PythonVariableViewModel> scriptVariable;
        public ObservableCollection<PythonVariableViewModel> ScriptVariable
        {
            get { return scriptVariable; }
            set
            {
                if (scriptVariable != value)
                {
                    scriptVariable = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Die Ausgewählte Script Variable.
        /// </summary>
        private PythonVariableViewModel selectedVariable;
        public PythonVariableViewModel SelectedVariable
        {
            get { return selectedVariable; }
            set
            {
                if (!Equals(selectedVariable, value))
                {
                    selectedVariable = value;
                    VariableChanged();
                    OnPropertyChanged();
                }
            }
        }


        /// <summary>
        /// Liste mit allen verfügbaren Tasks. (Für die auswahl der Script-Variable)
        /// </summary>
        private ObservableCollection<Task> allTasks;
        public ObservableCollection<Task> AllTasks
        {
            get { return allTasks; }
            set
            {
                if (allTasks != value)
                {
                    allTasks = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Der Ausgewählter Task. (Für die auswahl der Script-Variable)
        /// </summary>
        private Task selectedTask;
        public Task SelectedTask
        {
            get { return selectedTask; }
            set
            {
                if (!Equals(selectedTask, value))
                {
                    selectedTask = value;
                    OnPropertyChanged();
                    if (selectedVariable != null && value != null)
                    {
                        selectedVariable.TaskId = value.Id;
                    }
                }

                SelectedTaskChanged();
            }
        }


        /// <summary>
        /// Alle TaskComponents des ausgewählten Tasks. (Für die auswahl der Script-Variable)
        /// </summary>
        private ObservableCollection<TaskComponent> componentsFromTask;
        public ObservableCollection<TaskComponent> ComponentsFromTask
        {
            get { return componentsFromTask; }
            set
            {
                if (componentsFromTask != value)
                {
                    componentsFromTask = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Das ausgewaählte TaskComponent. (Für die auswahl der Script-Variable)
        /// </summary>
        private TaskComponent selectedComponent;
        public TaskComponent SelectedComponent
        {
            get { return selectedComponent; }
            set
            {
                if (!Equals(selectedComponent, value))
                {
                    selectedComponent = value;
                    OnPropertyChanged();
                    if (selectedVariable != null && value != null)
                    {
                        selectedVariable.TaskComponentId = value.Id;
                    }
                }
            }
        }

        #endregion

        #region commands

        /// <summary>
        /// Command für das hinzufügen einer Variable.
        /// </summary>
        private ICommand addVariableCommand;
        public ICommand AddVariableCommand
        {
            get
            {
                if (addVariableCommand == null)
                {
                    addVariableCommand = new ActionCommand(x =>
                    {
                        var newVariable = new PythonVariableViewModel(new PythonVariable{Name = "new"});
                        ScriptVariable.Add(newVariable);
                        SelectedVariable = newVariable;
                    });
                }
                return addVariableCommand;
            }
        }

        /// <summary>
        /// Command für das löschen einer Variable.
        /// </summary>
        private ICommand removeVariableCommand;
        public ICommand RemoveVariableCommand
        {
            get
            {
                if (removeVariableCommand == null)
                {
                    removeVariableCommand = new ActionCommand(x =>
                    {
                        if (SelectedVariable == null) return;
                        ScriptVariable.Remove(SelectedVariable);
                    });
                    
                }

                return removeVariableCommand;
            }
        }

        #endregion

        #region constructor


        public ScriptViewModel(Script script)
            : base(script)
        {
            Image = "script.png";
            Width = 90;
            Height = 90;

            ScriptVariable = new ObservableCollection<PythonVariableViewModel>();
            AllTasks = new ObservableCollection<Task>();
            ComponentsFromTask = new ObservableCollection<TaskComponent>();
        }

        #endregion

        #region methods

        public override void LoadDetails()
        {
            AllTasks.Clear();
            var client = ClientSession.Instance.RelaxoClient;

            // Load all tasks
            var tasks = client.GetTasks(ClientSession.Instance.CurrentWorkflow);
            tasks.ForEach(t => AllTasks.Add(t));

            // Load script
            var script = client.GetScript(Id);
            ScriptContent = script.ScriptContent;
            
            // get decision variables
            ScriptVariable.Clear();
            script.Variables.ForEach(v=>ScriptVariable.Add(new PythonVariableViewModel(v)));

        }

        /// <summary>
        /// Methode die aufgerufen wird, wenn sich die aktuell ausgewählte Variable geändert hat,
        /// damit die entsprechenden Werte der Variable (Task, TaskComponent) in den DropDowns gesetzt werden können.
        /// </summary>
        private void VariableChanged()
        {
            if(selectedVariable == null) return;

            SelectedTask = AllTasks.FirstOrDefault(task => task.Id == selectedVariable.TaskId);
        }

        /// <summary>
        /// Methode die aufgerufen wird, wenn der aktuell ausgewählter Task geändert wird.
        /// Wird gebraucht um die entsprechenden TaskComponents des Tasks in die Dropdown zu laden.
        /// </summary>
        private void SelectedTaskChanged()
        {
            if (SelectedTask == null)
            {
                SelectedComponent = null;
                return;
            }

            if (SelectedVariable == null)
            {
                SelectedTask = null;
                SelectedComponent = null;
                return;
            }

            // merke die ausgewählte TaksComponent, damit die nach dem laden wieder gesetzt werden kann.
            var tmpComponentId = selectedVariable.TaskComponentId;

            var task = ClientSession.Instance.RelaxoClient.GetTask(SelectedTask.Id);
            var components = new List<TaskComponent>();

            ComponentsFromTask.Clear();
            task.FormGroups.ForEach(group => components.AddRange(group.Components));
            components.ForEach(comp => ComponentsFromTask.Add(comp));

            SelectedComponent = ComponentsFromTask.FirstOrDefault(component => component.Id == tmpComponentId);
        }

        public override void SaveDetails()
        {
            var client = ClientSession.Instance.RelaxoClient;
            var script = new Script();
            script.Id = Id;
            script.ScriptContent = ScriptContent;       
            script.Variables.AddRange(from variable
                                        in ScriptVariable
                                        select new PythonVariable { Name = variable.Name,                                                                      
                                                                      TaskComponentId = variable.TaskComponentId,
                                                                      TaskId = variable.TaskId});

            client.UpdateScript(script);
        }

        #endregion
    }
}
