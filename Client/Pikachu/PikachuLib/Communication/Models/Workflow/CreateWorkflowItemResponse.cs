using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines Create Workflow Item Response
    /// </summary>
    public class CreateWorkflowItemResponse
    {
        [JsonProperty("id")]
        public int WorkflowItemId { get; set; }
    }
}
