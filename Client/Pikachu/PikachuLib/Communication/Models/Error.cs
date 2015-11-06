using Newtonsoft.Json;

namespace PikachuLib.Communication.Models
{
    /// <summary>
    /// Ein Error-Objekt stellt die Struktur eines Error Responses vom Relaxo Server da.
    /// Es enthält zwei Key-Value Paare. Den Error-Code sowie die Error-Message.
    /// </summary>
    public class Error
    {
        private string code;

        #region properties

        [JsonProperty("code")]
        public string Code
        {
            get { return code; }
            set { code = value; }
        }


        private string message;

        [JsonProperty("message")]
        public string Message
        {
            get { return message; }
            set { message = value; }
        }

        #endregion
    }
}
