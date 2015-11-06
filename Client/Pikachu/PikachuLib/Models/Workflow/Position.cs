namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Repräsentation einer Position mit X,Y Koordinaten
    /// </summary>
    public class Position
    {
        /// <summary>
        /// X-Koordinate
        /// </summary>
        public int X { get; set; }

        /// <summary>
        /// Y-Koordinate
        /// </summary>
        public int Y { get; set; }

        public Position()
        {
        }

        public Position(int x, int y)
        {
            this.X = x;
            this.Y = y;
        }
    }
}
