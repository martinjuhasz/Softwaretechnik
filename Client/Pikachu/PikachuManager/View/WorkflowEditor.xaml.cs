using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace PikachuManager.View
{
    /// <summary>
    /// Interaktionslogik für WorkflowEditor.xaml
    /// </summary>
    public partial class WorkflowEditor : UserControl
    {
        public WorkflowEditor()
        {
            InitializeComponent();
        }

        private void Label_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            Debug.Print("MouseDobble-Click-Codebehind!");
        }

        private void Label_MouseEnter(object sender, MouseEventArgs e)
        {
            Debug.Print("MouseEnter-Codebehind!");
        }
    }
}
