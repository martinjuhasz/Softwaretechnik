using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuLib.Models.Workflow;

namespace PikachuManager.ViewModel.Workflowitems
{
    /// <summary>
    /// Repräsentation eines ViewModels für das Workflowitem Fork
    /// </summary>
    public class ForkViewModel : WorkflowItemViewModel
    {
        public ForkViewModel(Fork fork) : base(fork)
        {
            Image = "fork.png";
            Width = 60;
            Height = 60;
        }

        public override void LoadDetails()
        {
            // Zum Fork-WorkflowItem gibts nix zu laden bzw. aktualisieren.
        }

        public override void SaveDetails()
        {
            // Zum Fork-WorkflowItem gibts nix zu laden bzw. aktualisieren.
        }
    }
}
