using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using PikachuLib.Models.Workflow;

namespace PikachuLib.Communication.Models.Workflow
{
    /// <summary>
    /// Repräsentation einer Decision Response
    /// </summary>
    public class DecisionResponse
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("condition")]
        public string Condition { get; set; }

        [JsonProperty("nextWorkflowItemOnTrue")]
        public int? NextWorkflowItemOnTrue { get; set; }

        [JsonProperty("variables")]
        public List<PythonVariableResponse> Variables { get; set; }

        public DecisionResponse()
        {}

        public DecisionResponse(Decision decision)
        {
            Variables = new List<PythonVariableResponse>();

            Id = decision.Id;
            Condition = decision.Condition;
            decision.Variables.ForEach(v=> Variables.Add(new PythonVariableResponse(v)));
        }
    }
}
