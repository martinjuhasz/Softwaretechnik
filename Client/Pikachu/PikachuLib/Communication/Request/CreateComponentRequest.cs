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
    public class CreateComponentRequest : RequestBase
    {
        /// <summary>
        /// Name
        /// </summary>
        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("comment")]
        public string Comment { get; set; }

        [JsonProperty("regex")]
        public string Regex { get; set; }

        [JsonProperty("required")]
        public bool Required { get; set; }

        public CreateComponentRequest(int formGroupId, string name, string comment, string regex, bool required)
        {
            Method = Method.PUT;
            RestUrl = String.Format("formgroups/{0}/components", formGroupId);
            
            Name = name;
            Comment = comment;
            Regex = regex;
            Required = required;
        }

    }
}
