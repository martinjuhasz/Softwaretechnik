using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuViewBase.Hilfsklassen;

namespace PikachuManager.ViewModel.DragNDrop
{
    /// <summary>
    /// Basisklasse für ConnectorViewModel und WorkflowItemViewModel.
    /// So können beide innerhalb einer Liste für das Canvas gehalten werden.
    /// </summary>
    public class CanvasObjectViewModel : ViewModelBase
    {
        private bool isSelected;
        /// <summary>
        /// Gibt an, ob ein Element gerade selektiert ist.
        /// </summary>
        public bool IsSelected
        {
            get { return isSelected; }
            set
            {
                if (!Equals(isSelected, value))
                {
                    isSelected = value;
                    OnPropertyChanged();
                }
            }
        }
    }
}
