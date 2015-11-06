using System;
using System.ComponentModel;
using System.Text.RegularExpressions;
using PikachuLib.Models.Job;
using PikachuLib.Models.TaskComponent;

namespace PikachuClient.ViewModel.ComponentTypes
{
    /// <summary>
    /// ViewModel das ein Textfeld in einem Formular repräsentiert.
    /// </summary>
    public class TextComponentViewModel : ComponentViewModelBase<TaskComponentText>, IDataErrorInfo
    {
        #region properties

        /// <summary>
        /// Der Inhalt des Text-Formularfeldes.
        /// </summary>
        private string value;
        public string Value
        {
            get { return value; }
            set
            {
                if (String.CompareOrdinal(this.value, value) == 0) return;
                this.value = value;
                OnPropertyChanged();
            }
        }

        #endregion

        #region constructor

        public TextComponentViewModel(TaskComponentText taskText)
        {
            CurrentComponent = taskText;
            value = taskText.Value;
        }

        #endregion

        #region methods

        public override void SaveValues()
        {
            base.SaveValues();
            CurrentComponent.Value = Value;
        }

        public override void SetValues(JobComponent jobValue)
        {
            if(jobValue == null) return;

            var val = (JobComponentText) jobValue;
            Value = val.Value;
        }

        #endregion

        #region IDataErrorInfo Members

        public string Error { get; private set; }

        public string this[string columnName]
        {
            get
            {
                Error = string.Empty;

                // Es gibt nur ein Feld, das geprüft werden muss!
                if (Required && String.IsNullOrEmpty(Value))
                {
                    Error = "Das ist ein Pflichtfeld!";
                }
                else if (!String.IsNullOrEmpty(CurrentComponent.RegexExpression))
                {
                    var regex = new Regex(CurrentComponent.RegexExpression);
                    if (!regex.IsMatch(value))
                    {
                        Error = "Der Reguläre Ausdruck stimmt nicht überein!";
                    }
                }

                return Error;
            }
        }

        #endregion
    }
}
