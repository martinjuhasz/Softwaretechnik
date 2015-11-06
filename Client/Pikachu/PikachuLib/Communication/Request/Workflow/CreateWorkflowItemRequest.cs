using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using PikachuLib.Communication.Models.Request;
using PikachuLib.Models.Workflow;
using RestSharp;
using Position = PikachuLib.Communication.Models.Workflow.Position;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Repräsentation eines Create Workflow Item Requests
    /// </summary>
    public class CreateWorkflowItemRequest : RequestBase
    {
        #region properties

        [JsonProperty("type")]
        [JsonConverter(typeof(StringEnumConverter))]
        public WorkflowItemType Type { set; get; }

        [JsonProperty("position")]
        public Position Position { get; set; }

        #endregion

        public CreateWorkflowItemRequest(int workflowId, WorkflowItem workflowItem)
        {
            Type = workflowItem.Type;
            Position = new Position(workflowItem.Position.X, workflowItem.Position.Y);

            RestUrl = String.Format("/workflows/{0}/workflowitems", workflowId);
            Method = Method.PUT;
        }
    }
}
