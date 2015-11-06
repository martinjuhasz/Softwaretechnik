using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuManager.ViewModel.Workflowitems;

namespace PikachuManager.ViewModel.DragNDrop
{
    /// <summary>
    /// Stellt alle Informationen für die Verbindungslinien bereit.
    /// Erbt von CanvasObjectViewModel, damit WorkflowItemViewModels und ConnectorViewModels in einer Liste gehalten werden können.
    /// </summary>
    public class ConnectorViewModel : CanvasObjectViewModel
    {
        public ConnectorViewModel(WorkflowItemViewModel start, WorkflowItemViewModel end)
        {
            Start = start;
            End = end;

            // setze Werte
            X1 = Start.Left;
            Y1 = Start.Top;
            X2 = End.Left;
            Y2 = End.Top;

            // bei Property Changed Events anmelden
            Start.PropertyChanged += StartOnPropertyChanged;
            End.PropertyChanged += EndOnPropertyChanged;
        }

        /// <summary>
        /// Wird bei PropertyChanged des WorkflowItems Start aufgerufen.
        /// Aktualisiert die Werte X1 und Y1, also den Start der Linie.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        private void StartOnPropertyChanged(object sender, PropertyChangedEventArgs args)
        {
            X1 = Start.Left;
            Y1 = Start.Top;
        }

        /// <summary>
        /// Wird bei PropertyChanged des WorkflowItems End aufgerufen.
        /// Aktualisiert die Werte X2 und Y2, also das Ende der Linie.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        private void EndOnPropertyChanged(object sender, PropertyChangedEventArgs args)
        {
            X2 = End.Left;
            Y2 = End.Top;
        }

        private WorkflowItemViewModel start;
        /// <summary>
        /// WorkflowItemViewModel, bei dem die Linie anfangen soll
        /// </summary>
        public WorkflowItemViewModel Start
        {
            get { return start; }
            set
            {
                if (!Equals(start, value))
                {
                    start = value;
                    OnPropertyChanged();
                }
            }
        }

        private WorkflowItemViewModel end;
        /// <summary>
        /// WorkflowItemViewModel, bei dem die Linie enden soll
        /// </summary>
        public WorkflowItemViewModel End
        {
            get { return end; }
            set
            {
                if (!Equals(end, value))
                {
                    end = value;
                    OnPropertyChanged();
                }
            }
        }

        private int x1;
        /// <summary>
        /// X-Koordinate des Startpunkts der Linie
        /// </summary>
        public int X1
        {
            get
            {
                // x-Position - hälfte der Breite - Border
                int middle = x1 + Start.Width / 2 + Start.BorderWidth;
                return middle;
             
            }
            set
            {
                if (!Equals(x1, value))
                {
                    x1 = value;
                    OnPropertyChanged();
                }
            }
        }

        private int y1;
        /// <summary>
        /// Y-Koordinate des Startpunkts der Linie
        /// </summary>
        public int Y1
        {
            get
            {
                int middle;
                // y-Position - hälfte der Höhe - Border
                if (Start is TaskViewModel)
                {
                    middle = y1 + Start.Height / 2 + Start.BorderWidth + 25;
                    return middle;
                }
                
                middle = y1 + Start.Height/2 + Start.BorderWidth;
                return middle;

            }
            set
            {
                if (!Equals(y1, value))
                {
                    y1 = value;
                    OnPropertyChanged();
                }
            }
        }

        private int x2;
        /// <summary>
        /// X-Koordinate des Endpunkts der Linie
        /// </summary>
        public int X2
        {
            get
            {
                // x-Position - hälfte der Breite - Border
                int middle = x2 + End.Width/2 + End.BorderWidth;
                return middle;   
            }
            set
            {
                if (!Equals(x2, value))
                {
                    x2 = value;
                    OnPropertyChanged();
                }
            }
        }

        private int y2;
        /// <summary>
        /// Y-Koordinate des Endpunkts der Linie
        /// </summary>
        public int Y2
        {
            get 
            {
                int middle;
                // y-Position - hälfte der Höhe - Border
                if (End is TaskViewModel)
                {
                    middle = y2 + End.Height / 2 + End.BorderWidth + 25;
                    return middle;
                }

                middle = y2 + End.Height / 2 + End.BorderWidth;
                return middle;    
            }
            set
            {
                if (!Equals(y2, value))
                {
                    y2 = value;
                    OnPropertyChanged();
                }
            }
        }


    }
}
