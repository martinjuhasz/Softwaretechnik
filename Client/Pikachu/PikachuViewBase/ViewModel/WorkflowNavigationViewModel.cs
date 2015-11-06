using System;
using System.Collections.ObjectModel;
using System.Collections.Specialized;
using System.Diagnostics;
using PikachuLib;
using PikachuLib.Models.Workflow;
using PikachuViewBase.Hilfsklassen;
using System.Windows.Input;
using PikachuLib.Communication;
using PikachuLib.Communication.Event;

namespace PikachuViewBase.ViewModel
{
    /// <summary>
    /// ViewModel für die Navigationsleiste der Workflows.
    /// </summary>
    public class WorkflowNavigationViewModel : ViewModelBase
    {

        #region properties

        /// <summary>
        /// Liste der aktuellen Workflows des Benutzers.
        /// </summary>
        private ObservableCollection<Workflow> workflows;
        public ObservableCollection<Workflow> Workflows
        {
            get { return workflows; }
            set
            {
                workflows = value;
                OnPropertyChanged();
            }
        }

        /// <summary>
        /// Der aktuell ausgewählte Workflow
        /// </summary>
        private Workflow selectedWorkflow;
        public Workflow SelectedWorkflow
        {
            get { return selectedWorkflow; }
            set
            {
                if (selectedWorkflow == null || !selectedWorkflow.Equals(value))
                {
                    selectedWorkflow = value;
                    OnPropertyChanged();

                    WorkflowSelectionChanged();
                }
            }
        }

        #endregion

        #region button actions

        private ICommand unlockWorkflowCommand;
        public ICommand UnlockWorkflowCommand
        {
            get
            {
                if (unlockWorkflowCommand == null)
                {
                    unlockWorkflowCommand = new ActionCommand(x => UnlockWorkflow(),
                    o => ClientSession.Instance.CurrentWorkflow != null);
                }

                return unlockWorkflowCommand;
            }
        }

        #endregion

        public WorkflowNavigationViewModel()
        {
            Workflows = ClientSession.Instance.Workflows;
            SelectedWorkflow = ClientSession.Instance.CurrentWorkflow;

            ClientSession.Instance.CurrentWorkflowChangedEvent += CurrentWorkflowChangedHandler;
        }

        #region methods

        /// <summary>
        /// Delegate was ausgelöst wird, wenn der aktuelle Workflow sich ändert
        /// </summary>
        /// <param name="arg"></param>
        public void CurrentWorkflowChangedHandler(WorkflowChangedEventArgs arg)
        {
            SelectedWorkflow = ClientSession.Instance.CurrentWorkflow;
        }

        /// <summary>
        /// Wird ausgelöst wenn ein User die Auswahl in der Liste ändert
        /// </summary>
        private void WorkflowSelectionChanged()
        {
            if (SelectedWorkflow != null)
            {
                ClientSession.Instance.CurrentWorkflow = SelectedWorkflow;
            }

        }

        /// <summary>
        /// Gibt einen Workflow frei.
        /// </summary>
        private void UnlockWorkflow()
        {
            if (SelectedWorkflow == null)
            {
                return;
            }

            try
            {
                RelaxoClient client = ClientSession.Instance.RelaxoClient;
                client.UnlockWorkflow(SelectedWorkflow);
                ClientSession.Instance.RemoveWorkflow(SelectedWorkflow);
            }
            catch (RESTException e)
            {
                var errorMessage = String.Format("Error-Code: {0}\r\nMessage:{1}", e.Code, e.Message);
                Debug.WriteLine(errorMessage);
                Debug.WriteLine(e.StackTrace);
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.Message);
                Debug.WriteLine(e.StackTrace);
            }

        }

        #endregion
    }
}
