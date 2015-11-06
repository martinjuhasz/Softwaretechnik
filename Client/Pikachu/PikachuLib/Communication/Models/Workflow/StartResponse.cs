using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines Start Response
    /// </summary>
    public class StartResponse
    {
        [JsonProperty("usergroups")]
        public List<int> Usergroups { get; set; }

        public StartResponse()
        {
            Usergroups = new List<int>();
        }
    }
}
