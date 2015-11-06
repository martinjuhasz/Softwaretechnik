using System;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using PikachuLib.Models.Workflow;
using RestSharp;
using Position = PikachuLib.Communication.Models.Workflow.Position;

namespace PikachuLib.Communication.Request.Workflowitem
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
