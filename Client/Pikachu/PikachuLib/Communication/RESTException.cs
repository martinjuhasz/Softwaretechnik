using System;

namespace PikachuLib.Communication
{
    /// <summary>
    /// Exception, die auftritt, wenn eine REST-Anfrage fehlschlägt.
    /// </summary>
    public class RESTException : Exception
    {
        /// <summary>
        /// Html-Status code des Fehlers.
        /// </summary>
        public string Code { get; private set; }

        public RESTException() { }
        public RESTException(string code, string message)
            : base(message)
        {
            Code = code;
        }
    }
}
