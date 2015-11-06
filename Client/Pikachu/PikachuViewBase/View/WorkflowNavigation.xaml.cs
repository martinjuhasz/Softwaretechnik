using System.Windows;
using System.Windows.Controls;

namespace PikachuViewBase.View
{
	/// <summary>
	/// Interaktionslogik für WorkflowNavigation.xaml
	/// </summary>
	public partial class WorkflowNavigation : UserControl
	{



        public Visibility ShowPublishWorkflow
        {
            get { return (Visibility)GetValue(ShowPublishWorkflowProperty); }
            set { SetValue(ShowPublishWorkflowProperty, value); }
        }

        // Using a DependencyProperty as the backing store for ShowPublishWorkflow.  This enables animation, styling, binding, etc...
        public static readonly DependencyProperty ShowPublishWorkflowProperty =
            DependencyProperty.Register("ShowPublishWorkflow", typeof(Visibility), typeof(WorkflowNavigation), new PropertyMetadata(null));



		public WorkflowNavigation()
		{
			this.InitializeComponent();
		}
	}
}