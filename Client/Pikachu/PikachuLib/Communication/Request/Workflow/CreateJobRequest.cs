using System;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Repräsentation eines Create Job Request
    /// </summary>
    public class CreateJobRequest : RequestBase
    {
        public CreateJobRequest(int workflowId)
        {
            RestUrl = String.Format("workflows/{0}/jobs", workflowId);
            Method = Method.PUT;
        }
    }
}
