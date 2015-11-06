using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Workflow
{
    /// <summary>
    /// Repräsentation einer Position für die REST-Schnittstelle
    /// </summary>
    public class Position
    {
        [JsonProperty("x")]
        public int X { get; set; }
        [JsonProperty("y")]
        public int Y { get; set; }

        public Position(int x, int y)
        {
            this.X = x;
            this.Y = y;
        }
    }
}
