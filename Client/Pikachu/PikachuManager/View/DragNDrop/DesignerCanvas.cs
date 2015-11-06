using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using PikachuManager.DragNDrop;
using PikachuManager.ViewModel.DragNDrop;

namespace PikachuManager.View.DragNDrop
{
    /// <summary>
    /// Canvas, dass die Methoden OnDrop und MeasureOverride der Standard-Canvas Klasse überschreibt.
    /// Das Canvas kann sich so automatisch vergrößern wenn ein Item an den Rand gezogen wird.
    /// Es können Items aus der Toolbox auf dem Canvas gedropped werden.
    /// </summary>
    public class DesignerCanvas : Canvas
    {
        public DesignerCanvas()
        {
            this.AllowDrop = true;
        }

        /// <summary>
        /// Berechnet die Größe des Canvas, damit Scrollbars angezeigt werden können und die Arbeitsfläche
        /// quasi unendlich groß ist.
        /// </summary>
        /// <param name="constraint"></param>
        /// <returns>Größe des Canvas</returns>
        protected override Size MeasureOverride(Size constraint)
        {
            Size size = new Size();
            foreach (UIElement element in Children)
            {
                double left = Canvas.GetLeft(element);
                double top = Canvas.GetTop(element);
                left = double.IsNaN(left) ? 0 : left;
                top = double.IsNaN(top) ? 0 : top;

                element.Measure(constraint);

                Size desiredSize = element.DesiredSize;
                if (!double.IsNaN(desiredSize.Width) && !double.IsNaN(desiredSize.Height))
                {
                    size.Width = Math.Max(size.Width, left + desiredSize.Width);
                    size.Height = Math.Max(size.Height, top + desiredSize.Height);
                }
            }

            // füge etwas Platz hinzu, aus ästhetischen Gründen
            size.Width += 10;
            size.Height += 10;
            return size;
        }


        /// <summary>
        /// Wenn ein Item aus der Toolbox auf dem Canvas gedropped wird, wird diese Methode aufgerufen.
        /// Es wird die aktuelle Position ausgelesen und das entsprechende WorkflowItem wird dem Canvas hinzugefügt.
        /// </summary>
        /// <param name="e">Eventspezifische Infoss</param>
        protected override void OnDrop(DragEventArgs e)
        {
            base.OnDrop(e);

            // hole Drag Object
            DragObject dragObject = e.Data.GetData(typeof(DragObject)) as DragObject;
            if (dragObject != null)
            {
                Point position = e.GetPosition(this);
                var designerCanvas = DataContext as DesignerCanvasViewModel;
                var left = (int)position.X;
                var top = (int)position.Y;
                
                // erstelle WorkflowItem
                designerCanvas.AddWorkflowItem(dragObject.WorkflowItemType, left, top);
            }
            e.Handled = true;
        }
    }


}
