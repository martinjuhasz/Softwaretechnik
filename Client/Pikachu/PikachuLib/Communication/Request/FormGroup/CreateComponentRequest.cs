using System;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.FormGroup;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using Newtonsoft.Json.Converters;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Request-Klasse, welche den Payload und den Endpunk einer CreateComponent Anfrage enthält
    /// </summary>
    public abstract class CreateComponentRequest : RequestBase
    {
        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("comment")]
        public string Comment { get; set; }

        [JsonProperty("required")]
        public bool Required { get; set; }

        [JsonProperty("type")]
        [JsonConverter(typeof(StringEnumConverter))]
        public ComponentType ComponentType { get; set; }

        [JsonProperty("formGroupId")]
        public int FormGroupId { get; set; }

        public CreateComponentRequest(int formGroupId, string name, string comment, bool required)
        {
            Method = Method.PUT;
            RestUrl = String.Format("formgroups/{0}/components", formGroupId);
            
            Name = name;
            Comment = comment;
            Required = required;
            FormGroupId = formGroupId;
        }

    }
}
