using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using PikachuViewBase.Hilfsklassen;
using PikachuLib;
using PikachuLib.Communication.Event;
using System.Diagnostics;
using System.Windows.Data;
using PikachuLib.Communication;
using PikachuViewBase.ViewModel;
using PikachuLib.Models.TaskComponent;

namespace PikachuManager.ViewModel
{
    
    /// <summary>
    /// Model der View zum erstellen einer neuen FormGroup
    /// </summary>
    public class WorkflowContentEditorNewGroupViewModel : ViewModelBase
    {

        #region properties

        /// <summary>
        /// ausgewählte TaskComponentGroup.
        /// </summary>
        protected TaskComponentGroup selectedGroup;

        /// <summary>
        /// Gruppenname.
        /// </summary>
        private string componentGroupName;
        public string ComponentGroupName
        {
            get { return componentGroupName; }
            set
            {
                if (componentGroupName != value)
                {
                    componentGroupName = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region button actions

        private ICommand saveComponentGroupCommand;
        public ICommand SaveComponentGroupCommand
        {
            get
            {
                if (saveComponentGroupCommand == null)
                {
                    saveComponentGroupCommand = new ActionCommand(x =>
                    {
                        saveCompnentGroup();
                    });
                }

                return saveComponentGroupCommand;
            }
        }

        private ICommand cancelFormsCommand;
        public ICommand CancelFormsCommand
        {
            get
            {
                if (cancelFormsCommand == null)
                {
                    cancelFormsCommand = new ActionCommand(x =>
                    {
                        baseEditorViewModel.showGroupListView(selectedGroup);   
                    });
                }

                return cancelFormsCommand;
            }
        }

        private WorkflowContentEditorBaseViewModel baseEditorViewModel;

        #endregion

        #region Constructor

        public WorkflowContentEditorNewGroupViewModel(WorkflowContentEditorBaseViewModel baseEditorViewModel, TaskComponentGroup selectedGroup)
        {
            this.baseEditorViewModel = baseEditorViewModel;
            this.selectedGroup = selectedGroup;
        }

        #endregion

        #region methods

        /// <summary>
        /// Speichert die neue FormGroup
        /// </summary>
        private void saveCompnentGroup()
        {
            try
            {
                var workflowId = ClientSession.Instance.CurrentWorkflow.Id;
                RelaxoClient client = ClientSession.Instance.RelaxoClient;
                client.CreateComponentGroup(workflowId, ComponentGroupName);


                baseEditorViewModel.showGroupListView(selectedGroup);
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
