using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines Create Workflow Response
    /// </summary>
    public class CreateWorkflowResponse
    {
        [JsonProperty("id")]
        public int WorkflowId { get; set; }

        [JsonProperty("name")]
        public string WorkflowName { get; set; }
    }
}
