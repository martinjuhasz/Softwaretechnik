using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Start
{
    /// <summary>
    /// Request Klasse um ein Start Workflowitem zu aktualisieren.
    /// </summary>
    public class UpdateStartRequest : RequestBase
    {
        [JsonProperty("usergroups")]
        public List<int> Usergroups { get; set; }

        public UpdateStartRequest(int workflowId)
        {
            Method = Method.POST;
            RestUrl = string.Format("/workflowstart/{0}", workflowId);
        }
    }
}
