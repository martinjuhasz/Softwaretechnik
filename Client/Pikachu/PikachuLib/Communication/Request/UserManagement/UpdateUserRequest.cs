using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using PikachuLib.Communication.Models.Request;
using PikachuLib.Models.Usermanagement;
using RestSharp;

namespace PikachuLib.Communication.Request.UserManagement
{
    /// <summary>
    /// Repräsentation eines Update User Requests
    /// </summary>
    public class UpdateUserRequest : RequestBase
    {
        #region properties
        [JsonProperty("username")]
        public string Username { get; set; }

        [JsonProperty("password")]
        public string Password { get; set; }

        [JsonProperty("prename")]
        public string Prename { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("admin")]
        public bool Admin { get; set; }

        [JsonProperty("active")]
        public bool Active { get; set; }
        #endregion

        public UpdateUserRequest(User user)
        {
            RestUrl = String.Format("user/{0}", user.Id);
            Method = Method.POST;

            Username = user.Username;
            Password = user.Password;
            Prename = user.Prename;
            Name = user.Name;
            Admin = user.IsAdmin;
            Active = user.IsActive;
        }
    }
}
