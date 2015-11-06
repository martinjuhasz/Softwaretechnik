using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using System;

namespace PikachuLib.Communication.Request.UserManagement
{
    public class GetUsersByUserGroupRequest : RequestBase
    {
        public GetUsersByUserGroupRequest(int groupId)
        {
            RestUrl = String.Format("usergroup/{0}", groupId);
            Method = Method.GET;
        }
    }
}
