using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuViewBase.Hilfsklassen;
using PikachuViewBase.ViewModel;
using System.Windows.Input;
using PikachuLib.Communication;
using PikachuLib;
using System.Diagnostics;
using PikachuLib.Models.Workflow;

namespace PikachuManager.ViewModel
{
    /// <summary>
    /// Container-Klasse für das erstellen eines Workflows
    /// </summary>
    public class NewWorkflowViewModel : ViewModelBase
    {

        #region properties

        private string name;
        public string Name
        {
            get { return name; }
            set
            {
                if (name != value)
                {
                    name = value;
                    OnPropertyChanged();
                }
            }
        }

        private ManagerContainerViewModel managerViewModel;

        #endregion

        #region button actions

        /// <summary>
        /// Command zum speichern des Workflows
        /// </summary>
        private ICommand saveCommand;
        public ICommand SaveCommand
        {
            get
            {
                if (saveCommand == null)
                {
                    saveCommand = new ActionCommand(x =>
                    {
                        saveWorkflow();
                    });
                }

                return saveCommand;
            }
        }

        #endregion

        #region Constructor

        public NewWorkflowViewModel(ManagerContainerViewModel managerViewModel)
        {
            this.managerViewModel = managerViewModel;
        }


        #endregion

        #region methods

        /// <summary>
        /// Speichert den zu erstellenden Workflow ab
        /// </summary>
        private void saveWorkflow()
        {
            try
            {
                RelaxoClient client = ClientSession.Instance.RelaxoClient;
                Workflow newWorkflow = client.CreateWorkflow(Name);

                managerViewModel.SwitchToWorkflow(newWorkflow);
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
