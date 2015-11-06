using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Repräsentation eines Create Workflow Requests
    /// </summary>
    public class CreateWorkflowRequest : RequestBase
    {
        /// <summary>
        /// Name des zu erstellenden Workflows
        /// </summary>
        [JsonProperty("name")]
        public string Name { get; set; }

        public CreateWorkflowRequest(string name) 
        {
            RestUrl = "workflows";
            Method = Method.PUT;

            Name = name;
        }
    }
}
