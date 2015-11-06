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
    public class WorkflowContentEditorNewFloatComponentViewModel : WorkflowContentEditorNewComponentViewModel
    {

        #region properties

        /// <summary>
        /// Defaultwert des float Formularfeldes.
        /// </summary>
        private float componentDefaultValue;
        public float ComponentDefaultValue
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

        /// <summary>
        /// Minimum Wert.
        /// </summary>
        private float componentMinValue;
        public float ComponentMinValue
        {
            get { return componentMinValue; }
            set
            {
                if (componentMinValue != value)
                {
                    componentMinValue = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Maximumwert.
        /// </summary>
        private float componentMaxValue;
        public float ComponentMaxValue
        {
            get { return componentMaxValue; }
            set
            {
                if (componentMaxValue != value)
                {
                    componentMaxValue = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Anzahl der Nachkommastellen.
        /// </summary>
        private int componentScale;
        public int ComponentScale
        {
            get { return componentScale; }
            set
            {
                if (componentScale != value)
                {
                    componentScale = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region button actions

     

        #endregion

        #region Constructor

        public WorkflowContentEditorNewFloatComponentViewModel(WorkflowContentEditorBaseViewModel baseEditorViewModel, TaskComponentGroup selectedGroup)
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
                RelaxoClient client = ClientSession.Instance.RelaxoClient;
                client.CreateFloatComponent(selectedGroup.Id, ComponentName, ComponentComment, ComponentRequired, ComponentDefaultValue, ComponentMinValue, ComponentMaxValue, ComponentScale);
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
