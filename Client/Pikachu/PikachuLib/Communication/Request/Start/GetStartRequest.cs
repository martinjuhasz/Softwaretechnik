using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Start
{
    /// <summary>
    /// Request Klasse um ein Start Workflowitem zu holen.
    /// </summary>
    public class GetStartRequest : RequestBase
    {
        public GetStartRequest(int workflowId)
        {
            Method = Method.GET;
            RestUrl = string.Format("workflowstart/{0}", workflowId);
        }
    }
}
