using System.Collections.Generic;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using PikachuLib.Communication.Models.Workflow;
using PikachuLib.Models.Workflow;
using RestSharp;

namespace PikachuLib.Communication.Request.Script
{
    /// <summary>
    /// Request Klasse um ein Start Workflowitem zu aktualisieren.
    /// </summary>
    public class UpdateScriptRequest : RequestBase
    {
        [JsonProperty("script")]
        public string ScriptContent { get; set; }

        [JsonProperty("variables")]
        public List<PythonVariableResponse> Variables { get; set; }

        public UpdateScriptRequest(PikachuLib.Models.Workflow.Script script)
        {
            Method =  Method.POST;
            RestUrl = string.Format("workflowscript/{0}", script.Id);

            Variables = new List<PythonVariableResponse>();

            ScriptContent = script.ScriptContent;
            script.Variables.ForEach(v => Variables.Add(new PythonVariableResponse(v)));
        }
    }
}