using PikachuLib.Models.Workflow;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PikachuLib.Communication.Event
{
    /// <summary>
    /// Wrapper-Klasse zum übergeben des Payloads einer Workflow-Message
    /// </summary>
    public class WorkflowEventArgs : EventArgs
    {
        public object Sender { get; private set; }
        public Workflow Workflow { get; private set; }

        public WorkflowEventArgs(object sender, Workflow w)
            :base()
        {
            Workflow = w;
            Sender = sender;
        }
    }

    /// <summary>
    /// Wrapper-Klasse zum übergeben des Payloads einer Workflowänderungs-Message
    /// </summary>
    public class WorkflowChangedEventArgs : WorkflowEventArgs
    {
        public Workflow OldWorkflow { get; private set; }

        public WorkflowChangedEventArgs(object sender, Workflow oldWorkflow, Workflow newWorkflow)
            :base(sender, newWorkflow)
        {
            OldWorkflow = oldWorkflow;
        }
    }
}
