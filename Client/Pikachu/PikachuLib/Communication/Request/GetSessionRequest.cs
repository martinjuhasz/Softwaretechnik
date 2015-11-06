using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using RestSharp;

namespace PikachuLib.Communication.Models.Request
{
    public class GetSessionRequest : RequestBase
    {
        #region properties
        [JsonProperty("username")]
        public string Username { set; get; }
        [JsonProperty("password")]
        public string Password { set; get; }
        #endregion

        #region constructors
        public GetSessionRequest(string username, string password)
        {
            this.RestUrl = "sessions";
            this.Method = Method.POST;
            this.Username = username;
            this.Password = password;
        }
        #endregion
    }
}
