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
    /// Repräsentation eines Set WorkflowItem NextItem Requests
    /// </summary>
    public class SetWorkflowItemNextItemRequest : RequestBase
    {
        [JsonProperty("nextItem")]
        public int NextItem { set; get; }

        public SetWorkflowItemNextItemRequest(WorkflowItem workflowItem)
        {
            this.NextItem = workflowItem.NextItem;

            RestUrl = String.Format("/workflowitems/{0}/nextitem", workflowItem.Id);
            Method = Method.POST;
        }
    }
}
