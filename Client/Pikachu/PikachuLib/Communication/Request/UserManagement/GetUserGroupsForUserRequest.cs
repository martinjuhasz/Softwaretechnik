using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using System;

namespace PikachuLib.Communication.Request.UserManagement
{
    public class GetUserGroupsForUserRequest : RequestBase
    {
        public GetUserGroupsForUserRequest(int userId)
        {
            RestUrl = String.Format("user/{0}/usergroup", userId);
            Method = Method.GET;
        }
    }
}
