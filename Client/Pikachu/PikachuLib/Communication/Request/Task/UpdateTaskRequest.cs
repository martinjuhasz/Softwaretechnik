using System.Collections.Generic;
using System.Threading.Tasks;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using PikachuLib.Models.TaskComponent;
using RestSharp;

namespace PikachuLib.Communication.Request.Task
{
    /// <summary>
    /// Request Klasse um eine TaskComponent einem Task hinzuzufügen.
    /// </summary>
    public class UpdateTaskComponentToTaskRequest
    {
        [JsonProperty("id")]
        public int Id { get; set; }

        [JsonProperty("readOnly")]
        public bool Readonly { get; set; }

        public UpdateTaskComponentToTaskRequest(TaskComponent component)
        {
            Id = component.Id;
            Readonly = component.ReadOnly;
        }

    }

    /// <summary>
    /// Request Klasse um einen Task zu aktualisieren.
    /// </summary>
    public class UpdateTaskRequest : RequestBase
    {

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("usergroups")]
        public List<int> Usergroups { get; set; }

        [JsonProperty("taskComponentsForTask")]
        public List<UpdateTaskComponentToTaskRequest> TaskComponents { get; set; }

        public UpdateTaskRequest(int taskId)
        {
            RestUrl = string.Format("tasks/{0}", taskId);
            Method = Method.POST;
        }
    }
}
