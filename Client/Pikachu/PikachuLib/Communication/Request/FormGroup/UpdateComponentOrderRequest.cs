using System;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using Newtonsoft.Json.Converters;
using PikachuLib.Models.TaskComponent;
using System.Collections.Generic;
using PikachuLib.Communication.Models.FormGroup;

namespace PikachuLib.Communication.Request.FormGroup
{
    /// <summary>
    /// Request-Klasse, welche den Payload und den Endpunk einer UpdateComponentOrder Anfrage enthält
    /// </summary>
    public class UpdateComponentOrderRequest : RequestBase
    {
        [JsonProperty("components")]
        public List<ComponentOrder> Components { get; set; }

        public UpdateComponentOrderRequest(int formGroupId, List<ComponentOrder> components)
        {
            Method = Method.POST;
            RestUrl = String.Format("formgroups/{0}/componentPosition", formGroupId);
            
            Components = components;
        }
    }
}
