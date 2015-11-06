using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Input;
using PikachuManager.ViewModel;

namespace PikachuManager.DragNDrop
{
    /// <summary>
    /// Attached Property für Drag'n'Drop für Items aus der Toolbox.
    /// Angelehnt an http://www.codeproject.com/Articles/484616/MVVM-Diagram-Designer
    /// </summary>
    public static class DragAndDropProps     
    {
        #region EnabledForDrag
        public static readonly DependencyProperty EnabledForDragProperty =
            DependencyProperty.RegisterAttached("EnabledForDrag", typeof(bool), typeof(DragAndDropProps),
                new FrameworkPropertyMetadata((bool)false,
                    new PropertyChangedCallback(OnEnabledForDragChanged)));

        public static bool GetEnabledForDrag(DependencyObject d)
        {
            return (bool)d.GetValue(EnabledForDragProperty);
        }

        public static void SetEnabledForDrag(DependencyObject d, bool value)
        {
            d.SetValue(EnabledForDragProperty, value);
        }

        private static void OnEnabledForDragChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
        {
            FrameworkElement fe = (FrameworkElement) d;


            if((bool)e.NewValue)
            {
                fe.PreviewMouseDown += Fe_PreviewMouseDown;
                fe.MouseMove += Fe_MouseMove;
            }
            else
            {
                fe.PreviewMouseDown -= Fe_PreviewMouseDown;
                fe.MouseMove -= Fe_MouseMove;
            }
        }
        #endregion

        #region DragStartPoint

        public static readonly DependencyProperty DragStartPointProperty =
            DependencyProperty.RegisterAttached("DragStartPoint", typeof(Point?), typeof(DragAndDropProps));

        public static Point? GetDragStartPoint(DependencyObject d)
        {
            return (Point?)d.GetValue(DragStartPointProperty);
        }


        public static void SetDragStartPoint(DependencyObject d, Point? value)
        {
            d.SetValue(DragStartPointProperty, value);
        }

        #endregion

        /// <summary>
        /// Wickelt den Drag aus der Toolbox ab.
        /// Der Typ des gedraggten Items wird in dem Wrapper-Objekt DataObject gespeichert.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        static void Fe_MouseMove(object sender, System.Windows.Input.MouseEventArgs e)
        {
            Point? dragStartPoint = GetDragStartPoint((DependencyObject)sender);

            if (e.LeftButton != MouseButtonState.Pressed)
                dragStartPoint = null;

            if (dragStartPoint.HasValue)
            {
                // erzeuge DragObject, dass Type des gedraggten WorkflowItems enthält
                DragObject dataObject = new DragObject();
                // setze WorkflowItemType
                dataObject.WorkflowItemType = (((FrameworkElement)sender).DataContext as WorkflowToolItemViewModel).GetWorkflowItemType();
                DragDrop.DoDragDrop((DependencyObject)sender, dataObject, DragDropEffects.Copy);
                e.Handled = true;
            }
        }

        static void Fe_PreviewMouseDown(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            SetDragStartPoint((DependencyObject)sender, e.GetPosition((IInputElement)sender));
        }
    }
}

