using System;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.FormGroup;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using Newtonsoft.Json.Converters;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Request-Klasse, welche den Payload und den Endpunk einer CreateTextComponent Anfrage enthält
    /// </summary>
    public class CreateTextComponentRequest : CreateComponentRequest
    {
        [JsonProperty("defaultValue")]
        public string DefaultValue { get; set; }

        [JsonProperty("regex")]
        public string Regex { get; set; }

        public CreateTextComponentRequest(int formGroupId, string name, string comment, bool required,string defaultValue, string regex)
            :base(formGroupId, name, comment, required)
        {
            DefaultValue = defaultValue;
            Regex = regex;
            ComponentType = ComponentType.TaskComponentText;
        }

    }
}
