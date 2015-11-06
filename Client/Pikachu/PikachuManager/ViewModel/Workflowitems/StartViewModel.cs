using System.Collections.ObjectModel;
using System.Linq;
using PikachuLib;
using PikachuLib.Models.Usermanagement;
using PikachuLib.Models.Workflow;

namespace PikachuManager.ViewModel.Workflowitems
{
    /// <summary>
    /// Repräsentation eines ViewModels für das Workflowitem Start
    /// </summary>
    public class StartViewModel : WorkflowItemViewModel
    {
        #region properties

        /// <summary>
        /// Liste aller verfügbaren Benutzergrupppen.
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
        /// Liste aller Benutzergruppen, die dem workflow item zugeordnet wurden.
        /// </summary>
        private ObservableCollection<UserGroup> selectedGroups;
        public ObservableCollection<UserGroup> SelectedGroups
        {
            get { return selectedGroups; }
            set
            {
                if (selectedGroups != value)
                {
                    selectedGroups = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region constructor

        public StartViewModel(Start start) : base(start)
        {
            Image = "start.png";
            Width = 60;
            Height = 60;
        }
        #endregion

        #region methods

        public override void LoadDetails()
        {
            var client = ClientSession.Instance.RelaxoClient;
            Usergroups = new ObservableCollection<UserGroup>(client.GetAllUserGroups());
            SelectedGroups = new ObservableCollection<UserGroup>();

            Start start = client.GetWorkflowStart(Id);

            var groups = from ug in Usergroups where start.Usergroups.Contains(ug.Id) select ug;
            SelectedGroups = new ObservableCollection<UserGroup>(groups);
        }

        public override void SaveDetails()
        {
            var client = ClientSession.Instance.RelaxoClient;

            var start = (Start) WorkflowItem;
            start.Usergroups = selectedGroups.Select(userGroup => userGroup.Id).ToList();

            client.UpdateWorkflowStart(start);
        }

        #endregion
    }
}
