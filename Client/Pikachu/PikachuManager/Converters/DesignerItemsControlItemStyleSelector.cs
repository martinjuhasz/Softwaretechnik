using PikachuManager.ViewModel.Workflowitems;
using System;
using System.Windows;
using System.Windows.Controls;
using PikachuManager.ViewModel.DragNDrop;

namespace PikachuManager.Converters
{
    /// <summary>
    /// Style Selector. 
    /// Checkt von welches ViewModel ein Objekt ist und gibt entsprechend einen Style zurück.
    /// So können 2 unterschiedliche Elemente in einer Liste gehalten werden und eine unterschiedliche Darstellung bekommen.
    /// Angelehnt an http://www.codeproject.com/Articles/484616/MVVM-Diagram-Designer
    /// </summary>
    public class DesignerItemsControlItemStyleSelector : StyleSelector
    {
        static DesignerItemsControlItemStyleSelector()
        {
            Instance = new DesignerItemsControlItemStyleSelector();
        }

        public static DesignerItemsControlItemStyleSelector Instance
        {
            get;
            private set;
        }


        public override Style SelectStyle(object item, DependencyObject container)
        {
            ItemsControl itemsControl = ItemsControl.ItemsControlFromItemContainer(container);
            if (itemsControl == null)
                throw new InvalidOperationException("DesignerItemsControlItemStyleSelector: Could not find ItemsControl");

            if (item is TaskViewModel)
            {
                return (Style)itemsControl.FindResource("taskItemStyle");
            }

            if (item is WorkflowItemViewModel)
            {
                return (Style)itemsControl.FindResource("workflowItemStyle");
            }

            if (item is ConnectorViewModel)
            {
                return (Style)itemsControl.FindResource("connectorStyle");
            }

            return null;
        }
    } 
}
