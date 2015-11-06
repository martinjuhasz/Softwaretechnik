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
    public class CreateFormGroupRequest : RequestBase
    {
        /// <summary>
        /// Name der zu erstellenden FormGroup
        /// </summary>
        [JsonProperty("name")]
        public string Name { get; set; }

        public CreateFormGroupRequest(int workflowId, string name)
        {
            Method = Method.PUT;
            RestUrl = String.Format("workflows/{0}/formgroups", workflowId);
            Name = name;
        }
    }
}
