using PikachuLib.Communication.Models.FormGroup;

namespace PikachuLib.Models.TaskComponent
{
    public class TaskComponentFloat : TaskComponent
    {
        /// <summary>
        /// Gibt den Wert zurück.
        /// </summary>
        public float Value { get; set; }

        /// <summary>
        /// Der Minimalwert der Zahl.
        /// </summary>
        public float MinValue { get; set; }

        /// <summary>
        /// Der Maximalwert der Zahl.
        /// </summary>
        public float MaxValue { get; set; }

        /// <summary>
        /// Die Anzahl der Nachkommastellen
        /// </summary>
        public int Scale { get; set; }

        public TaskComponentFloat(string name, string comment, bool required, int order)
            :base(name, comment, required, order)
        { }


        /// <summary>
        /// Überführt ein Kommunikationsmodel in das Model
        /// </summary>
        /// <param name="input">Das Model der Kommunikationsschicht.</param>
        /// <returns>Ein Flieskommazahl-Formularfeld.</returns>
        public static TaskComponentFloat Parse(FloatInputResponse input)
        {
            var comp = new TaskComponentFloat(input.Name, input.Comment, input.Required, input.Order)
            {
                Id = input.Id,
                Value = input.DefaultValue,
                MinValue = input.MinValue ?? 0,
                MaxValue = input.MaxValue ?? 0,
                ReadOnly =  input.Readonly,
                Scale = input.Scale
            };

            return comp;
        }
    }
}