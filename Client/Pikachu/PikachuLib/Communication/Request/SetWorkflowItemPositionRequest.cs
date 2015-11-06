using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using RestSharp;
using PikachuLib.Models.Workflow;

namespace PikachuLib.Communication.Models.Request
{
    /// <summary>
    /// Repräsentation eines Set WorkflowItem Position Request
    /// </summary>
    public class SetWorkflowItemPositionRequest : RequestBase
    {
        [JsonProperty("position")]
        public Position Position { set; get; }

        public SetWorkflowItemPositionRequest(WorkflowItem workflowItem)
        {
            this.Position = new Position(workflowItem.Position.X, workflowItem.Position.Y);

            RestUrl = String.Format("/workflowitems/{0}/position", workflowItem.Id);
            Method = Method.POST;
        }
        
    }
}
