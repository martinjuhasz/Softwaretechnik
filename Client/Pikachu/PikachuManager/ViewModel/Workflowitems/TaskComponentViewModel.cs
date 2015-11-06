using System.Collections.ObjectModel;
using PikachuLib.Models.TaskComponent;
using PikachuViewBase.Hilfsklassen;

namespace PikachuManager.ViewModel.Workflowitems
{
    /// <summary>
    /// ViewModel einer TaskComponentGroup.
    /// </summary>
    public class TaskComponentGroupViewModel : ViewModelBase
    {
        #region properties

        /// <summary>
        /// Name der ComponentGroup
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Id der ComponentGroup
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Liste aller TaskComponents, die in dieser Gruppe enthalten sind.
        /// </summary>
        private ObservableCollection<TaskComponentViewModel> taskComponents;
        public ObservableCollection<TaskComponentViewModel> TaskComponents
        {
            get { return taskComponents; }
            set
            {
                if (taskComponents != value)
                {
                    taskComponents = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region constructor

        public TaskComponentGroupViewModel()
        {
            TaskComponents = new ObservableCollection<TaskComponentViewModel>();
        }

        #endregion

        #region methods

        /// <summary>
        /// Gibt für eine TaskComponentGroup das entsprechende ViewModel zurück.
        /// </summary>
        /// <param name="componentGroup">Die TaskComponentGroup.</param>
        /// <returns>Ein ViewModel des TaskComponentGroups.</returns>
        public static TaskComponentGroupViewModel Parse(TaskComponentGroup componentGroup)
        {
            var vm = new TaskComponentGroupViewModel { Id = componentGroup.Id, Name = componentGroup.Name };
            foreach (var component in componentGroup.Components)
            {
                vm.TaskComponents.Add(TaskComponentViewModel.Parse(component));
            }

            return vm;
        }

        #endregion
    }

    /// <summary>
    /// ViewModel eines TaskComponents.
    /// </summary>
    public class TaskComponentViewModel : ViewModelBase
    {
        #region properties

        private TaskComponent taskComponent;

        /// <summary>
        /// Die ID der TaskComponent
        /// </summary>
        private int id;
        public int Id
        {
            get { return id; }
        }

        /// <summary>
        /// Ein beschreibender Name der Komponente.
        /// </summary>
        private string name;
        public string Name
        {
            get { return name; }
            set
            {
                if (name != value)
                {
                    name = value;
                    taskComponent.Name = name;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Gibt an, ob die TaskComponent ausgewählt wurde. (beim Workflowitem-Editor)
        /// </summary>
        private bool selected;
        public bool Selected
        {
            get { return selected; }
            set
            {
                if (selected != value)
                {
                    selected = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Gibt an, ob die TaskComponent Readonly ist. (beim Workflowitem-Editor)
        /// </summary>
        private bool readOnly;
        public bool ReadOnly
        {
            get { return readOnly; }
            set
            {
                if (readOnly != value)
                {
                    readOnly = value;
                    taskComponent.ReadOnly = readOnly;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Gibt die TaskComponent des ViewModels zurück.
        /// </summary>
        public TaskComponent TaskComponent
        {
            get { return taskComponent; }
            set
            {
                if (taskComponent != value)
                {
                    taskComponent = value;
                    id = taskComponent.Id;
                    Name = taskComponent.Name;
                    ReadOnly = taskComponent.ReadOnly;

                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region constructor

        public TaskComponentViewModel(TaskComponent component)
        {
            TaskComponent = component;
            Selected = false;
        }

        #endregion

        #region methods
        
        /// <summary>
        /// Gibt das ViewModel für die TaskComponent zurück.
        /// </summary>
        /// <param name="component"></param>
        /// <returns></returns>
        public static TaskComponentViewModel Parse(TaskComponent component)
        {
            return new TaskComponentViewModel(component);
        }

        #endregion
    }
}
