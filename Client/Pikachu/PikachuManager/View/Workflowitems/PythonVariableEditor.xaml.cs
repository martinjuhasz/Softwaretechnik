using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using PikachuLib.Models.TaskComponent;
using PikachuLib.Models.Workflow;
using PikachuManager.ViewModel.Workflowitems;

namespace PikachuManager.View.Workflowitems
{
    /// <summary>
    /// Interaktionslogik für DecisionVariableEditor.xaml
    /// </summary>
    public partial class PythonVariableEditor : UserControl
    {
        public PythonVariableViewModel PythonVariable
        {
            get { return (PythonVariableViewModel)GetValue(PythonVariableProperty); }
            set { SetValue(PythonVariableProperty, value); }
        }

        public static readonly DependencyProperty PythonVariableProperty =
            DependencyProperty.Register("PythonVariable", typeof(PythonVariableViewModel), typeof(PythonVariableEditor), new PropertyMetadata(null));




        public ObservableCollection<Task> AllTasks
        {
            get { return (ObservableCollection<Task>)GetValue(AllTasksProperty); }
            set { SetValue(AllTasksProperty, value); }
        }

        // Using a DependencyProperty as the backing store for AllTasks.  This enables animation, styling, binding, etc...
        public static readonly DependencyProperty AllTasksProperty =
            DependencyProperty.Register("AllTasks", typeof(ObservableCollection<Task>), typeof(PythonVariableEditor), new PropertyMetadata(null));




        public ObservableCollection<TaskComponent> AllTaskComponents
        {
            get { return (ObservableCollection<TaskComponent>)GetValue(AllTaskComponentsProperty); }
            set { SetValue(AllTaskComponentsProperty, value); }
        }

        // Using a DependencyProperty as the backing store for MyProperty.  This enables animation, styling, binding, etc...
        public static readonly DependencyProperty AllTaskComponentsProperty =
            DependencyProperty.Register("AllTaskComponents", typeof(ObservableCollection<TaskComponent>), typeof(PythonVariableEditor), new PropertyMetadata(null));

        public Task SelectedTask
        {
            get { return (Task)GetValue(SelectedTaskProperty); }
            set { SetValue(SelectedTaskProperty, value); }
        }

        // Using a DependencyProperty as the backing store for MyProperty.  This enables animation, styling, binding, etc...
        public static readonly DependencyProperty SelectedTaskProperty =
            DependencyProperty.Register("SelectedTask", typeof(Task), typeof(PythonVariableEditor), new PropertyMetadata(null));

        public TaskComponent SelectedTaskComponent
        {
            get { return (TaskComponent)GetValue(SelectedTaskComponentProperty); }
            set { SetValue(SelectedTaskComponentProperty, value); }
        }

        // Using a DependencyProperty as the backing store for MyProperty.  This enables animation, styling, binding, etc...
        public static readonly DependencyProperty SelectedTaskComponentProperty =
            DependencyProperty.Register("SelectedTaskComponent", typeof(TaskComponent), typeof(PythonVariableEditor), new PropertyMetadata(null));




        public PythonVariableEditor()
        {
            InitializeComponent();
        }
    }
}
