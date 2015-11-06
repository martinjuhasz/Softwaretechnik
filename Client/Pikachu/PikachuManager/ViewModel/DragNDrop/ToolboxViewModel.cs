using PikachuLib.Models;
using PikachuLib.Models.Workflow;
using PikachuViewBase.Hilfsklassen;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuViewBase.ViewModel;
using Task = PikachuLib.Models.Workflow.Task;

namespace PikachuManager.ViewModel.DragNDrop
{
    /// <summary>
    /// ViewModel für die Toolbox des Workfloweditors.
    /// </summary>
    public class ToolboxViewModel : ViewModelBase
    {
        /// <summary>
        /// Liste der Items, die angezeigt werden sollen.
        /// </summary>
        public List<WorkflowToolItemViewModel> ToolboxItems { get; private set; }

        
        public ToolboxViewModel()
        {
            ToolboxItems = new List<WorkflowToolItemViewModel>
            {
                new WorkflowToolItemViewModel<Start>("Start", "start.png"),
                new WorkflowToolItemViewModel<End>("End", "end.png"),
                new WorkflowToolItemViewModel<Task>("Task", "task.png"),
                new WorkflowToolItemViewModel<Decision>("Decision", "decision.png"),
                new WorkflowToolItemViewModel<Script>("Script", "script.png"),
                new WorkflowToolItemViewModel<Fork>("Fork", "fork.png"),
                new WorkflowToolItemViewModel<Join>("Join", "join.png")
            };
        }

    }
}
