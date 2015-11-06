using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using System;

namespace PikachuLib.Communication.Request.UserManagement
{
    public class GetUserRequest : RequestBase
    {

        public GetUserRequest(int userid)
        {
            RestUrl = String.Format("user/{0}", userid);
            Method = Method.GET;
        }
    }
}
