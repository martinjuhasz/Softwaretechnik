using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Mime;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Input;
using PikachuViewBase.Hilfsklassen;
using PikachuViewBase.ViewModel;
using PikachuLib.Models.Workflow;
using PikachuLib;

namespace PikachuManager.ViewModel
{
    /// <summary>
    /// Container-Klasse für die Manager-Funktionalitäten.
    /// </summary>
    public class ManagerContainerViewModel : ViewModelBase
    {
        #region properties

        // ViewModels für Benutzer- und WorkflowManager:
        public UserManagerBaseViewModel UserManagerContent { get; set; }

        public WorkflowManagerViewModel WorkflowManagerContent { get; set; }

        public NewWorkflowViewModel NewWorkflowContent { get; set; }

        private int selectedTabIndex;
        public int SelectedTabIndex
        {
            get { return selectedTabIndex; }
            set
            {
                if (selectedTabIndex != value)
                {
                    selectedTabIndex = value;
                    OnPropertyChanged();
                }
            }
        }
        
        #endregion

        #region contstructor

        public ManagerContainerViewModel()
        {
            UserManagerContent = new UserManagerBaseViewModel();
            WorkflowManagerContent = new WorkflowManagerViewModel();
            NewWorkflowContent = new NewWorkflowViewModel(this);
        }

        #endregion

        #region methods

        /// <summary>
        /// Wechselt zum Tab InhaltsEditor und selektiert den übergebenen Workflo
        /// </summary>
        /// <param name="workflow">der Workflow der ausgewählt werden soll</param>
        public void SwitchToWorkflow(Workflow workflow)
        {
            // zur "Workflow editieren"-Ansicht wechseln
            SelectedTabIndex = 1;

            ClientSession.Instance.WorkflowAdded(workflow);
        }

        #endregion
    }
}
