using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using RestSharp;
using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Request
{
    /// <summary>
    /// Basis Klasse für einen Request. 
    /// So können alle relevanten Daten für einen Request im jeweiligen Objekt gekapselt werden.
    /// </summary>
    public abstract class RequestBase
    {
        /// <summary>
        /// Die HTTP-Request-Methode der Anfrage.
        /// </summary>
        [JsonIgnore]
        public Method Method { get; set; }

        /// <summary>
        /// Die URL die beim Request angefragt werden soll.
        /// </summary>
        [JsonIgnore]
        public string RestUrl { get; set; }

       
    }
}
