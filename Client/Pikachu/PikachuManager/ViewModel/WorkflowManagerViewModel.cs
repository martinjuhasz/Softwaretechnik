using PikachuViewBase.Hilfsklassen;
using PikachuViewBase.ViewModel;

namespace PikachuManager.ViewModel
{
    /// <summary>
    /// ViewModel für das bearbeiten von Workflows.
    /// </summary>
    public class WorkflowManagerViewModel : ViewModelBase
    {
        #region properties

        /// <summary>
        /// ViewModel des Workflowitem-Editors
        /// </summary>
        public WorkflowEditorViewModel WorkflowEditor { get; set; }

        /// <summary>
        /// ViewModel des Inhaltseditors.
        /// </summary>
        public WorkflowContentEditorBaseViewModel WorkflowContentEditorBase { get; set; }

        /// <summary>
        /// ViewModel für die Workflow-Navigation.
        /// </summary>
        public WorkflowNavigationViewModel WorkflowNavigation { get; set; }

        #endregion

        #region constructor

        public WorkflowManagerViewModel()
        {
            WorkflowNavigation = new WorkflowNavigationViewModel();
            WorkflowContentEditorBase = new WorkflowContentEditorBaseViewModel(WorkflowNavigation);
            WorkflowEditor = new WorkflowEditorViewModel(WorkflowNavigation);
        }        

        #endregion
    }
}
