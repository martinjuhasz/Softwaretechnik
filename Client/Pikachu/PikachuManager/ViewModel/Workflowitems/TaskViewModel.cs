using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using PikachuLib;
using PikachuLib.Models.TaskComponent;
using PikachuLib.Models.Usermanagement;
using PikachuLib.Models.Workflow;

namespace PikachuManager.ViewModel.Workflowitems
{
    /// <summary>
    /// Repräsentation eines ViewModels für das Workflowitem Task
    /// </summary>
    public class TaskViewModel : WorkflowItemViewModel
    {
        #region properties

        /// <summary>
        /// Der Name des Tasks.
        /// </summary>
        private string name;
        public string Name { get { return name; }
            set
            {
                if (name != value)
                {
                    name = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Alle verfügbaren Benutzergruppen.
        /// </summary>
        private ObservableCollection<UserGroup> usergroups;
        public ObservableCollection<UserGroup> Usergroups 
        {
            get { return usergroups; }
            set
            {
                if (usergroups != value)
                {
                    usergroups = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Liste der hinzugefügten Benutzergruppen.
        /// </summary>
        private ObservableCollection<UserGroup> selectedUserGroups;
        public ObservableCollection<UserGroup> SelectedUserGroups
        {
            get { return selectedUserGroups; }
            set
            {
                if (selectedUserGroups != value)
                {
                    selectedUserGroups = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Liste der TaskComponents
        /// </summary>
        private ObservableCollection<TaskComponentGroupViewModel> taskGroupComponents;
        public ObservableCollection<TaskComponentGroupViewModel> TaskGroupComponents 
        {
            get { return taskGroupComponents; }
            set
            {
                if (taskGroupComponents != value)
                {
                    taskGroupComponents = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region constructor

        public TaskViewModel(Task task) : base(task)
        {
            Image = "task.png";
            Width = 90;
            Height = 90;
            Name = task.Name;

            Usergroups = new ObservableCollection<UserGroup>();
            SelectedUserGroups = new ObservableCollection<UserGroup>();
            TaskGroupComponents = new ObservableCollection<TaskComponentGroupViewModel>();
        }

        #endregion

        #region methods

        public override void LoadDetails()
        {
            LoadTaskComponentGroups();
            LoadUserGroups();

            LoadTaskInfo();
        }

        /// <summary>
        /// Ruft die Informationen über den Task über REST ab und speichert diese in die dafür vorgesehene Properties.
        /// </summary>
        public void LoadTaskInfo()
        {
            var client = ClientSession.Instance.RelaxoClient;

            var task = client.GetTask(Id);
            
            Name = task.Name;

            // setzte die schon hinzugefügten Benutzergruppen.
            selectedUserGroups.Clear();
            foreach (var userGroup in usergroups)
            {
                if (task.Usergroups.Contains(userGroup.Id))
                {
                    selectedUserGroups.Add(userGroup);
                }
            }

            // gehe alle schon hinzugefügten TaskComponents durch und markiere diese im ViewModel
            foreach(var fg in task.FormGroups)
            {
                var groupViewModel = TaskGroupComponents.FirstOrDefault(group => group.Id == fg.Id);
                if (groupViewModel == null) continue;

                foreach (var component in fg.Components)
                {
                    var componentViewModel = groupViewModel.TaskComponents.First(comp => comp.Id == component.Id);
                    componentViewModel.Selected = true;
                    componentViewModel.ReadOnly = component.ReadOnly;
                }
            }
        }

        /// <summary>
        /// Ruft alle Benutzergruppen vom Server ab um die entrspechende Liste zu füllen.
        /// </summary>
        public void LoadUserGroups()
        {
            var client = ClientSession.Instance.RelaxoClient;
            Usergroups = new ObservableCollection<UserGroup>(client.GetAllUserGroups());
        }

        /// <summary>
        /// Ruft alle TaskComponents ab um die entsprechende Liste zu füllen.
        /// </summary>
        public void LoadTaskComponentGroups()
        {
            var client = ClientSession.Instance.RelaxoClient;
            TaskGroupComponents.Clear();
            var taskComponentGroup = client.GetComponentGroups(ClientSession.Instance.CurrentWorkflow.Id);
            foreach (var componentGroup in taskComponentGroup)
            {
                TaskGroupComponents.Add(TaskComponentGroupViewModel.Parse(componentGroup));
            }
        }

        public override void SaveDetails()
        {
            var client = ClientSession.Instance.RelaxoClient;
            List<int> groupIds = selectedUserGroups.Select(userGroup => userGroup.Id).ToList();

            var taskComponents = new List<TaskComponent>();
            foreach (var groupViewModel in TaskGroupComponents)
            {
                taskComponents.AddRange(groupViewModel.TaskComponents
                    .Where(component => component.Selected)
                    .Select(component => component.TaskComponent));
            }

            client.UpdateTask(Id, Name, groupIds, taskComponents);
        }

        #endregion
    }
}
