using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Session
{
    /// <summary>
    /// Klasse die eine REST Requestabfrage auf sessions repräsentiert.
    /// </summary>
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
