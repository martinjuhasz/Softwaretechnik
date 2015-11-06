using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using PikachuLib.Communication.Models.Request;
using PikachuLib.Models.Usermanagement;
using RestSharp;

namespace PikachuLib.Communication.Request.UserManagement
{
    /// <summary>
    /// Repräsentation eines Create UserGroup Requests
    /// </summary>
    public class CreateUserGroupRequest : RequestBase
    {
        #region properties
        [JsonProperty("name")]
        public string Name { get; set; }
        #endregion

        public CreateUserGroupRequest(UserGroup usergroup)
        {
            RestUrl = String.Format("usergroup");
            Method = Method.PUT;

            Name = usergroup.Name;
        }
    }
}
