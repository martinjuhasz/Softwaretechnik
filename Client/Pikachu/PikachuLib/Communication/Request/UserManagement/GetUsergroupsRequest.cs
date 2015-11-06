using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.UserManagement
{
    public class GetUsergroupsRequest : RequestBase
    {
        public GetUsergroupsRequest()
        {
            RestUrl = "usergroup";
            Method = Method.GET;
        }
    }
}
