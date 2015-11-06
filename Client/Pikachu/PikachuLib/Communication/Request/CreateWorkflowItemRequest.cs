using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using RestSharp;
using PikachuLib.Models.Workflow;
using PikachuLib.Communication.Models;

namespace PikachuLib.Communication.Models.Request
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
            this.Position = new Position(workflowItem.Position.X, workflowItem.Position.Y);

            RestUrl = String.Format("/workflows/{0}/workflowitems", workflowId);
            Method = Method.PUT;
        }
    }
}
