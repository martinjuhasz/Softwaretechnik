using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using System;

namespace PikachuLib.Communication.Request.UserManagement
{
    public class RemoveUserFromUserGroupRequest : RequestBase
    {
        public RemoveUserFromUserGroupRequest(int groupId, int userId)
        {
            RestUrl = String.Format("usergroup/{0}/user/{1}", groupId, userId);
            Method = Method.DELETE;
        }
    }
}
