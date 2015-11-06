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
    /// Repräsentation eines Script Response
    /// </summary>
    public class ScriptResponse
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("script")]
        public string ScriptContent { get; set; }

        [JsonProperty("variables")]
        public List<PythonVariableResponse> Variables { get; set; }

        public ScriptResponse()
        {
            Variables = new List<PythonVariableResponse>();
        }

        public ScriptResponse(Script script)
            :this()
        {
            Id = script.Id;
            ScriptContent = script.ScriptContent;
            script.Variables.ForEach(v=> Variables.Add(new PythonVariableResponse(v)));
        }
    }
}
