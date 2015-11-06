using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Session
{
    /// <summary>
    /// Klasse die eine DELETE Request anfrage an den Server Repräsentiert.
    /// </summary>
    public class DeleteSessionRequest : RequestBase
    {
        public DeleteSessionRequest()
        {
            Method = Method.DELETE;
            RestUrl = "sessions";
        }
    }
}
