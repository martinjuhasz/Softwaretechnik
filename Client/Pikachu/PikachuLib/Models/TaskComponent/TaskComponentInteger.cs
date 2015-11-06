using PikachuLib.Communication.Models.FormGroup;

namespace PikachuLib.Models.TaskComponent
{
    /// <summary>
    /// Repräsentation eines Ganzzahl Formularfeldes.
    /// </summary>
    public class TaskComponentInteger : TaskComponent
    {
        #region properties

        /// <summary>
        /// Wert des Formularfeldes.
        /// </summary>
        public int Value { get; set; }

        /// <summary>
        /// Minimumwert des Formularfeldes.
        /// </summary>
        public int MinValue { get; set; }

        /// <summary>
        /// Maximumwert des Formularfeldes
        /// </summary>
        public int MaxValue { get; set; }

        #endregion

        #region constructor

        public TaskComponentInteger(string name, string comment, bool required, int order)
            :base(name, comment, required, order)
        {
        }

        #endregion

        #region methods

        /// <summary>
        /// Überführt ein Kommunikationsmodel in das Model
        /// </summary>
        /// <param name="input">Das Model der Kommunikationsschicht.</param>
        /// <returns>Ein Ganzzahl-Formularfeld.</returns>
        public static TaskComponentInteger Parse(IntegerInputResponse input)
        {
            var comp = new TaskComponentInteger(input.Name, input.Comment, input.Required, input.Order)
            {
                Id = input.Id,
                Value = input.DefaultValue,
                MinValue = input.MinValue,
                MaxValue = input.MaxValue,
                ReadOnly =  input.Readonly
            };

            return comp;
        }
        #endregion
    }
}
