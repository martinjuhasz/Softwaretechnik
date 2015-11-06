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
    public class WorkflowContentEditorNewTextComponentViewModel : WorkflowContentEditorNewComponentViewModel
    {

        #region properties

        /// <summary>
        /// Regulärer Ausdruck zum Formularfeld-Prüfen.
        /// </summary>
        private string componentRegex;
        public string ComponentRegex
        {
            get { return componentRegex; }
            set
            {
                if (componentRegex != value)
                {
                    componentRegex = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Defaultwert des Formularfeldes.
        /// </summary>
        private string componentDefaultValue;
        public string ComponentDefaultValue
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

        public WorkflowContentEditorNewTextComponentViewModel(WorkflowContentEditorBaseViewModel baseEditorViewModel, TaskComponentGroup selectedGroup)
            : base(baseEditorViewModel, selectedGroup)
        {
            
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
                client.CreateTextComponent(selectedGroup.Id, ComponentName, ComponentComment, ComponentRequired, ComponentDefaultValue, ComponentRegex);
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
