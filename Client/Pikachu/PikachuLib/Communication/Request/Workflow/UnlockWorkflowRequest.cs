using System;
using Newtonsoft.Json;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflow
{
    public class UnlockWorkflowRequest : RequestBase
    {
        public UnlockWorkflowRequest(int workflowId)
        {
            RestUrl = String.Format("workflows/{0}/lock", workflowId);
            Method = Method.POST;
        }
    }
}
