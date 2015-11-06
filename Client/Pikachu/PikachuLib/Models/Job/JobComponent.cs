using System;

namespace PikachuLib.Models.Job
{
    /// <summary>
    /// Abstrakte Basis-Klasse für die ausgefüllten Formularfelder des Jobs.
    /// </summary>
    public abstract class JobComponent
    {
        public int TaskComponentId { get; set; }
    }

    /// <summary>
    /// Wert eines Textfeldes.
    /// </summary>
    public class JobComponentText : JobComponent
    {
        public string Value { get; set; }
    }

    /// <summary>
    /// Wert eines Integer.
    /// </summary>
    public class JobComponentInteger : JobComponent
    {
        public int Value { get; set; }
    }

    /// <summary>
    /// Wert eines Floats.
    /// </summary>
    public class JobComponentFloat : JobComponent
    {
        public float Value { get; set; }
    }

    /// <summary>
    /// Wert eines DateTimes.
    /// </summary>
    public class JobComponentDate : JobComponent
    {
        public DateTime Value { get; set; }
    }
}
