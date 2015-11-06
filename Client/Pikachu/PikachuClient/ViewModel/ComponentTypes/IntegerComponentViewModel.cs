using System.ComponentModel;
using System.Globalization;
using PikachuLib.Models.Job;
using PikachuLib.Models.TaskComponent;

namespace PikachuClient.ViewModel.ComponentTypes
{
    /// <summary>
    /// ViewModel für eine Integer TaskComponent.
    /// </summary>
    public class IntegerComponentViewModel : ComponentViewModelBase<TaskComponentInteger>, IDataErrorInfo
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

        public IntegerComponentViewModel(TaskComponentInteger component)
        {
            CurrentComponent = component;
            Value = component.Value.ToString(CultureInfo.InvariantCulture);
        }

        #endregion

        #region methods

        public override void SetValues(JobComponent jobValue)
        {
            CurrentComponent.Value = ((JobComponentInteger)jobValue).Value;
            Value = CurrentComponent.Value.ToString(CultureInfo.InvariantCulture);
        }

        public override void SaveValues()
        {
            base.SaveValues();
            CurrentComponent.Value = int.Parse(Value);
        }

        #endregion

        #region IDataErrorInfo Member

        public string Error { get; private set; }

        public string this[string columnName]
        {
            get
            {
                Error = string.Empty;
                int crInput;
                if (int.TryParse(Value, out crInput))
                {
                    // Hier gibt es nur ein Formularfeld, darum muss nicht überprüft werden, um welche proberty es sich handelt.
                    if (CurrentComponent.MinValue < CurrentComponent.MaxValue)
                    {
                    
                        if (CurrentComponent.MinValue > crInput || crInput > CurrentComponent.MaxValue)
                        {
                            Error = string.Format("Die Zahl muss zwischen {0} und {1} liegen.", CurrentComponent.MinValue, CurrentComponent.MaxValue);
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
