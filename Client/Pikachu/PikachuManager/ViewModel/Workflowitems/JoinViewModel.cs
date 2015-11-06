using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuLib.Models.Workflow;

namespace PikachuManager.ViewModel.Workflowitems
{
    /// <summary>
    /// Repräsentation eines ViewModels für das Workflowitem Join
    /// </summary>
    public class JoinViewModel : WorkflowItemViewModel
    {
        public JoinViewModel(Join join)
            : base(join)
        {
            Image = "join.png";
            Width = 60;
            Height = 60;
        }

        public override void LoadDetails()
        {
            // Zum Join-WorkflowItem gibts nix zu laden bzw. aktualisieren.
        }

        public override void SaveDetails()
        {
            // Zum Join-WorkflowItem gibts nix zu laden bzw. aktualisieren.
        }
    }
}
