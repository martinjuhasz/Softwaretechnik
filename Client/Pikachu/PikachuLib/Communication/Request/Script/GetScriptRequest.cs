using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Script
{
    /// <summary>
    /// Request Klasse um ein Decision Workflowitem zu holen.
    /// </summary>
    public class GetScriptRequest : RequestBase
    {
        public GetScriptRequest(int id)
        {
            Method = Method.GET;
            RestUrl = string.Format("workflowscript/{0}", id);
        }
    }
}