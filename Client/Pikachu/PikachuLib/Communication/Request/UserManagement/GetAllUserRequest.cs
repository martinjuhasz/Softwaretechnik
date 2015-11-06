using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;
using System;

namespace PikachuLib.Communication.Request.UserManagement
{
    public class GetAllUserRequest : RequestBase
    {
        public GetAllUserRequest()
        {
            RestUrl = String.Format("user");
            Method = Method.GET;
        }
    }
}
