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
    /// ViewModel für ein Datumsfeld
    /// </summary>
    public class DateComponentViewModel : ComponentViewModelBase<TaskComponentDate>, IDataErrorInfo
    {
        #region properties

        /// <summary>
        /// Die String-Repräsentation des Textfeldes in der View.
        /// </summary>
        private DateTime value;
        public DateTime Value
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

        public DateComponentViewModel(TaskComponentDate component)
        {
            CurrentComponent = component;
            Value = component.Value;
        }

        #endregion

        #region methods

        public override void SetValues(JobComponent jobValue)
        {
            CurrentComponent.Value = ((JobComponentDate)jobValue).Value;
            Value = CurrentComponent.Value;
        }

        public override void SaveValues()
        {
            base.SaveValues();
            CurrentComponent.Value = Value;
        }

        #endregion

        #region IDataErrorInfo Member

        public string Error { get { return string.Empty; } }

        public string this[string columnName]
        {
            // in das Datumsfeld können keine falschen werte eingegeben werden.
            get { return string.Empty; }
        }
        
        #endregion
    }
}
