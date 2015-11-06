using System.Collections.Generic;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using PikachuLib.Communication.Models.Workflow;
using PikachuLib.Models.Workflow;
using RestSharp;

namespace PikachuLib.Communication.Request.WorkflowDecision
{
    /// <summary>
    /// Request Klasse um ein Decision Workflowitem zu aktualisieren.
    /// </summary>
    public class UpdateDecisionRequest : RequestBase
    {
        [JsonProperty("condition")]
        public string Condition { get; set; }

        [JsonProperty("variables")]
        public List<PythonVariableResponse> Variables { get; set; }

        [JsonProperty("nextWorkflowItemOnTrue")]
        public int NextWorkflowItemOnTrue { get; set; }

        public UpdateDecisionRequest(Decision decision)
        {
            Method =  Method.POST;
            RestUrl = string.Format("workflowdecision/{0}", decision.Id);

            Variables = new List<PythonVariableResponse>();

            Condition = decision.Condition;
            NextWorkflowItemOnTrue = decision.NextItemTrue;
            decision.Variables.ForEach(v => Variables.Add(new PythonVariableResponse(v)));
        }
    }
}