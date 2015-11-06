using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using PikachuLib.Communication.Models;
using PikachuLib.Communication.Models.Job;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Job
{
    /// <summary>
    /// Klasse um einen Job zu aktualisieren.
    /// </summary>
    public class UpdateJobRequest : RequestBase
    {
        /// <summary>
        /// Task ID der Felder, die ausgefüllt wurden.
        /// </summary>
        [JsonProperty("taskId")]
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
