using PikachuLib.Models.Workflow;

namespace PikachuManager.ViewModel.Workflowitems
{
    /// <summary>
    /// Repräsentation eines ViewModels für das Workflowitem End
    /// </summary>
    public class EndViewModel : WorkflowItemViewModel
    {
        public EndViewModel(End end) : base(end)
        {
            Image = "end.png";
            Width = 60;
            Height = 60;
        }

        public override void LoadDetails()
        {
            // Zum Ende-WorkflowItem gibts nix zu laden bzw. aktualisieren.
        }

        public override void SaveDetails()
        {
            // Zum -WorkflowItem gibts nix zu laden bzw. aktualisieren.
        }
    }
}
