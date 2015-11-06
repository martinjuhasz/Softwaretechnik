using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuLib.Models.Job;
using PikachuLib.Models.TaskComponent;

namespace PikachuClient.ViewModel.ComponentTypes
{

    /// <summary>
    /// ViewModel für eine Float TaskComponent.
    /// </summary>
    public class FloatComponentViewModel : ComponentViewModelBase<TaskComponentFloat>, IDataErrorInfo
    {
        #region properties

        /// <summary>
        /// Die String-Repräsentation des Textfeldes in der View.
        /// </summary>
        private string value;
        public string Value
        {
            get { return value; }
            set
            {
                if (this.value != value)
                {
                    this.value = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region constructor

        public FloatComponentViewModel(TaskComponentFloat component)
        {
            CurrentComponent = component;
            Value = component.Value.ToString(CultureInfo.InvariantCulture);
        }

        #endregion

        #region methods

        public override void SetValues(JobComponent jobValue)
        {
            CurrentComponent.Value = ((JobComponentFloat)jobValue).Value;
            Value = CurrentComponent.Value.ToString(CultureInfo.InvariantCulture); ;
        }

        public override void SaveValues()
        {
            base.SaveValues();
            CurrentComponent.Value = float.Parse(Value);
        }
        #endregion

        #region IDataErrorInfo Member

        public string Error { get; private set; }

        public string this[string columnName]
        {
            get
            {
                Error = string.Empty;

                // Hier gibt es nur ein Formularfeld, darum muss nicht überprüft werden, um welche proberty es sich handelt.
                float crInput;
                if (float.TryParse(Value, out crInput))
                {
                    if (CurrentComponent.MinValue < CurrentComponent.MaxValue)
                    {                    
                        if (CurrentComponent.MinValue > crInput || crInput > CurrentComponent.MaxValue)
                        {
                            Error = string.Format("Die Zahl muss zwischen {0} und {1} liegen.", CurrentComponent.MinValue, CurrentComponent.MaxValue);
                        }
                    }
                    if (CurrentComponent.Scale > -1)
                    {
                        // Finde letzten seperator . oder , wegen englisch/deutsche standards.
                        int pointIndex = value.IndexOf(".", StringComparison.CurrentCulture);
                        int commaIndex = value.IndexOf(",", StringComparison.CurrentCulture);
                        int seperatorIndex = Math.Max(pointIndex, commaIndex);
                        if (seperatorIndex > 0)
                        {
                            if (value.Substring(seperatorIndex).Length-1 > CurrentComponent.Scale)
                            {
                                Error = string.Format("Die Zahl darf höchstens {0} Nachkommastellen haben.", CurrentComponent.Scale);
                            }
                        }
                    }
                }
                else
                {
                    Error = "Es wird eine Zahl erwartet!";
                }

                return Error;
            }
        }
        
        #endregion
    }   
}
