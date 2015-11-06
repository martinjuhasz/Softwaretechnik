using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using PikachuLib.Models.Workflow;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflowitem
{
    /// <summary>
    /// Repräsentation eines Set WorkflowItem NextItem Requests
    /// </summary>
    public class SetWorkflowItemNextItemRequest : RequestBase
    {
        [JsonProperty("nextItem")]
        public IList<int> NextItem { set; get; }

        public SetWorkflowItemNextItemRequest(WorkflowItem workflowItem)
        {
            this.NextItem = workflowItem.NextItem;

            RestUrl = String.Format("/workflowitems/{0}/nextitem", workflowItem.Id);
            Method = Method.POST;
        }
    }
}
