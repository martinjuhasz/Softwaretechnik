using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;

namespace PikachuLib.Communication.Models.Request
{
    public class GetUsergroupsRequest : RequestBase
    {
        public GetUsergroupsRequest()
        {
            RestUrl = "usergroups/";
            Method = Method.GET;
        }
    }
}
