using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.Response;
using RestSharp;

namespace PikachuLib.Communication.Models.Request
{
    public class UpdateJobRequest : RequestBase
    {
        /// <summary>
        /// Task ID der Felder, die ausgefüllt wurden.
        /// </summary>
        [JsonProperty("taskID")]
        public int TaskId { get; set; }

        /// <summary>
        /// Die Ausgefüllten Formularfelder des Jobs.
        /// </summary>
        [JsonProperty("components")]
        public List<JobComponentResponse> Components { get; set; }

        public UpdateJobRequest(int id)
        {
            Method = Method.POST;
            RestUrl = String.Format("jobs/{0}", id);
            Components = new List<JobComponentResponse>();
        }
    }
}
