using PikachuLib.Communication.Models.FormGroup;
using PikachuLib.Models.TaskComponent;
using PikachuViewBase.Hilfsklassen;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuViewBase.ViewModel;

namespace PikachuManager.ViewModel
{
    /// <summary>
    /// Container Klasse die sich um die Views des ContentEditors kümmert
    /// </summary>
    public class WorkflowContentEditorBaseViewModel : ViewModelBase
    {

        #region properties

        /// <summary>
        /// Inhaltsbereich des WorkflowContentEditors
        /// </summary>
        private ViewModelBase content;

        /// <summary>
        /// Die WorkflowNavigations
        /// </summary>
        public WorkflowNavigationViewModel WorkflowNavigation { get; set; }

        /// <summary>
        /// Gibt die ObservableCollection für den Inhalt der Seite zurück.
        /// </summary>
        public ViewModelBase Content
        {
            get
            {
                return content;
            }
            set
            {
                if (value != content)
                {
                    content = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region Konstruktor

        public WorkflowContentEditorBaseViewModel(WorkflowNavigationViewModel workflowNavigation)
        {
            WorkflowNavigation = workflowNavigation;
            showGroupListView(null);
        }

        #endregion

        #region methods

        /// <summary>
        /// Zeigt die Liste der FormGroups mit zugehörigen Components an
        /// </summary>
        /// <param name="selectedGroup">Die zuvor ausgewählte Gruppe</param>
        public void showGroupListView(TaskComponentGroup selectedGroup)
        {
            Content = new WorkflowContentEditorViewModel(this, WorkflowNavigation, selectedGroup);
        }

        /// <summary>
        /// Zeigt die View zum erstellen einer neuen FormGroup an
        /// </summary>
        /// <param name="selectedGroup">Die zuvor ausgewählte Gruppe</param>
        public void showNewGroupListView(TaskComponentGroup selectedGroup)
        {
            Content = new WorkflowContentEditorNewGroupViewModel(this, selectedGroup);
        }

        /// <summary>
        /// Zeigt die View zum Erstellen einer neuen Component an
        /// </summary>
        /// <param name="selectedGroup">Die zugehörige Gruppe des Components</param>
        public void showNewComponentView(TaskComponentGroup selectedGroup, ComponentType componentType)
        {
            if (componentType == ComponentType.TaskComponentText)
            {
                Content = new WorkflowContentEditorNewTextComponentViewModel(this, selectedGroup);
            }
            else if (componentType == ComponentType.TaskComponentInteger)
            {
                Content = new WorkflowContentEditorNewIntegerComponentViewModel(this, selectedGroup);
            }
            else if (componentType == ComponentType.TaskComponentFloat)
            {
                Content = new WorkflowContentEditorNewFloatComponentViewModel(this, selectedGroup);
            }
            else if (componentType == ComponentType.TaskComponentDate)
            {
                Content = new WorkflowContentEditorNewDateComponentViewModel(this, selectedGroup);
            }
        }

        #endregion

    }
}
