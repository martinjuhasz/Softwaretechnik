using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Media;
using PikachuManager.ViewModel.DragNDrop;
using PikachuManager.ViewModel.Workflowitems;

namespace PikachuManager.View.DragNDrop
{
    /// <summary>
    /// Ermöglicht die Verschiebung eines WorkflowItems (DesignerItems, ContentControl) auf einem Canvas.
    /// Angelehnt an: http://www.codeproject.com/Articles/484616/MVVM-Diagram-Designer
    /// </summary>
    public class MoveThumb : Thumb
    {
        /// <summary>
        /// DesignerCanvas, wird benötigt um die Zeichenfläche automatisch zu vergrößern.
        /// </summary>
        private DesignerCanvas designerCanvas;

        /// <summary>
        /// Merke Y-Koordinate zu Beginn des Drags
        /// </summary>
        private int startTop;
        /// <summary>
        /// Merke X-Koordinate zu Beginn des Drags
        /// </summary>
        private int startLeft;

        /// <summary>
        /// aktuelles WorkflowItemViewModel
        /// </summary>
        private WorkflowItemViewModel workflowItemViewModel;

        public MoveThumb()
        {
            // füge Eventhandler hinzu
            DragStarted += startDrag;
            DragDelta += CalculateMove;
            DragCompleted += endDrag;
        }


        /// <summary>
        /// Setzt designerCanvas und workflowItemViewModel und stößt lock des aktuellen WorkflowIteman.
        /// Wird beim Start des Drags aufgerufen.
        /// </summary>
        /// <param name="sender">MoveThumb</param>
        /// <param name="e">Eventspezifische Infos</param>
        private void startDrag(object sender, DragStartedEventArgs e)
        {
            // hole ContentControl, das derzeit bewegt wird
            Control designerItem = (Control)DataContext;
            
            if (designerItem != null)
            {
                // hole Item von aktuell bewegtem ContentControl
                workflowItemViewModel = (WorkflowItemViewModel)designerItem.DataContext;

                // merke Koordinaten zu Beginn des Drags
                startLeft = workflowItemViewModel.Left;
                startTop = workflowItemViewModel.Top;

                // Lock workflowItem - falls Item nicht gesperrt werden kann -> designerCanvas = null
                try
                {
                    workflowItemViewModel.LockDrag();

                    // finde DesignerCanvas, damit Größenberechnung neu angestoßen werden kann
                    DependencyObject parent = VisualTreeHelper.GetParent(designerItem);
                    while (parent != null && !(parent is DesignerCanvas))
                    {
                        parent = VisualTreeHelper.GetParent(parent);
                    }
                    if (parent is DesignerCanvas)
                    {
                        designerCanvas = (DesignerCanvas)parent;
                    }
                }
                catch (Exception)
                {
                    WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = "Das Item konnte nicht gesperrt werden. Bitte noch einmal versuchen.";
                }
            }
        }


        /// <summary>
        /// Berechnet und updated die Position des aktuell bewegten Items.
        /// Wird während des Drags aufgerufen.
        /// </summary>
        /// <param name="sender">MoveThumb</param>
        /// <param name="e">Eventspezifische Infos</param>
        private void CalculateMove(object sender, DragDeltaEventArgs e)
        {
            // nur ausführen, wenn Item gesperrt werden konnte
            if (workflowItemViewModel != null && designerCanvas != null)
            {
                var newLeft = (int) (workflowItemViewModel.Left + e.HorizontalChange);
                var newTop = (int) (workflowItemViewModel.Top + e.VerticalChange);
                // aktualisiere die Position des aktuellen Items
                workflowItemViewModel.Left = newLeft > 0 ? newLeft : 0;
                workflowItemViewModel.Top = newTop > 0 ? newTop : 0;

                designerCanvas.InvalidateMeasure();
                e.Handled = true;
            }
        }


        /// <summary>
        /// Stößt senden der Position und unlock an.
        /// Wird beim Ende des Drags aufgerufen.
        /// </summary>
        /// <param name="sender">MoveThumb</param>
        /// <param name="e">Eventspezifische Infos</param>
        private void endDrag(object sender, DragCompletedEventArgs e)
        {
            // nur ausführen, wenn Item gesperrt werden konnte
            if (workflowItemViewModel != null && designerCanvas != null)
            {
                // Unlock WorkflowItem und setze Position
                try
                {
                    workflowItemViewModel.PushPositionToServer();
                }
                catch (Exception)
                {
                    WorkflowEditorDesignerCanvasErrorViewModel.Instance.Error = "Das Item konnte nicht verschoben werden. Bitte noch einmal versuchen.";
                    workflowItemViewModel.Left = startLeft;
                    workflowItemViewModel.Top = startTop;
                }
                
                workflowItemViewModel.UnlockDrag();
            }
        }
    }
}
