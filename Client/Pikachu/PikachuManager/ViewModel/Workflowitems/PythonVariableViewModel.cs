using PikachuLib.Models.Workflow;
using PikachuViewBase.Hilfsklassen;

namespace PikachuManager.ViewModel.Workflowitems
{
    /// <summary>
    /// Viewmodel für eine Variable der Decision.
    /// </summary>
    public class PythonVariableViewModel : ViewModelBase
    {
        #region Properties

        private PythonVariable variable;

        /// <summary>
        /// Gibt den Name der Variable zurück.
        /// </summary>
        private string name;
        public string Name
        {
            get { return name; }
            set
            {
                if (!string.Equals(name, value))
                {
                    name = value;
                    variable.Name = name;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Die Task-ID um den Wert der TaskComponent eindeutig zu identifizieren.
        /// </summary>
        public int TaskId
        {
            get { return variable.TaskId; }
            set
            {
                if (variable.TaskId != value)
                {
                    variable.TaskId = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Die TaskComponent, von der der Wert der Variable gelesen wird.
        /// </summary>
        public int TaskComponentId
        {
            get { return variable.TaskComponentId; }
            set
            {
                if (variable.TaskComponentId != value)
                {
                    variable.TaskComponentId = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion

        #region constructor

        public PythonVariableViewModel()
            :this(new PythonVariable())
        {
        }

        public PythonVariableViewModel(PythonVariable variable)
        {
            this.variable = variable;
            Name = variable.Name;
            TaskId = variable.TaskId;
            TaskComponentId = variable.TaskComponentId;
        }

        #endregion
    }
}
