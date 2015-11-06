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
    /// Repräsentation einer Variable Response
    /// </summary>
    public class PythonVariableResponse
    {
        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("taskId")]
        public int TaskId { get; set; }

        [JsonProperty("taskComponentId")]
        public int TaskComponentId { get; set; }


        public PythonVariableResponse()
        {}

        public PythonVariableResponse(PythonVariable variable)
        {
            Name = variable.Name;
            TaskId = variable.TaskId;
            TaskComponentId = variable.TaskComponentId;
        }
    }
}
