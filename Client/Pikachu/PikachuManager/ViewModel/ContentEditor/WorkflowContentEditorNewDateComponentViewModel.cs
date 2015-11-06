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
    /// Model der View zum Erstellen eines neuen Components
    /// </summary>
    public class WorkflowContentEditorNewDateComponentViewModel : WorkflowContentEditorNewComponentViewModel
    {

        #region properties

        /// <summary>
        /// Default-Wert des WorkflowitemEditors.
        /// </summary>
        private DateTime componentDefaultValue;
        public DateTime ComponentDefaultValue
        {
            get { return componentDefaultValue; }
            set
            {
                if (componentDefaultValue != value)
                {
                    componentDefaultValue = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region button actions

     

        #endregion

        #region Constructor

        public WorkflowContentEditorNewDateComponentViewModel(WorkflowContentEditorBaseViewModel baseEditorViewModel, TaskComponentGroup selectedGroup)
            : base(baseEditorViewModel, selectedGroup)
        {
            ComponentDefaultValue = DateTime.Today;
        }

        #endregion

        #region methods

        /// <summary>
        /// Speichert den Component
        /// </summary>
        protected override void saveComponent()
        {
            try
            {
                var workflowId = ClientSession.Instance.CurrentWorkflow.Id;
                RelaxoClient client = ClientSession.Instance.RelaxoClient;
                client.CreateDateComponent(selectedGroup.Id, ComponentName, ComponentComment, ComponentRequired, ComponentDefaultValue);
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
