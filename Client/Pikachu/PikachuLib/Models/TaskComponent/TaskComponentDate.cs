using System;
using PikachuLib.Communication.Models.FormGroup;

namespace PikachuLib.Models.TaskComponent
{
    /// <summary>
    /// Repräsentation eines Datum-Formularfeldes.
    /// </summary>
    public class TaskComponentDate : TaskComponent
    {
        /// <summary>
        /// Wert des Formularfeldes.
        /// </summary>
        public DateTime Value { get; set; }

        public TaskComponentDate(string name, string comment, bool required, int order)
            :base(name, comment, required, order)
        {
        }

        /// <summary>
        /// Überführt ein Kommunikationsmodel in das Model
        /// </summary>
        /// <param name="input">Repräsentation in der Kommunikation.</param>
        /// <returns>Datums-Formularfeld</returns>
        public static TaskComponentDate Parse(DateInputResponse input)
        {
            var comp = new TaskComponentDate(input.Name, input.Comment, input.Required, input.Order)
            {
                Id = input.Id,
                Value = input.DefaultValue,
                ReadOnly = input.Readonly
            };

            return comp;
        }
    }
}
