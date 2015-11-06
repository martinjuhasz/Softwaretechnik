using PikachuLib.Communication.Models.FormGroup;

namespace PikachuLib.Models.TaskComponent
{
    /// <summary>
    /// Repräsentation eines Text-Formularfeldes.
    /// </summary>
    public class TaskComponentText : TaskComponent
    {

        #region properties
        
        /// <summary>
        /// Der Wert des Textfeldes.
        /// </summary>
        public string Value { get; set; }

        /// <summary>
        /// Gibt bzw. legt den aktuellen Regex-Ausdruck fest für die validierung.
        /// </summary>
        public string RegexExpression { get; set; }

        #endregion

        #region constructors

        public TaskComponentText()
        {
        }

        public TaskComponentText(string name, string comment, bool required, int order)
            :base(name, comment, required, order)            
        {
        }

        public TaskComponentText(int id)
        {
            Id = id;
        }

        #endregion
        
        #region methods

        /// <summary>
        /// Parsed eine Schnittstellen-Klasse in ein TaskComponentText.
        /// </summary>
        /// <param name="item">Die Klasse der Schnittstelle.</param>
        /// <returns>Ein TaskComponent-Objekt.</returns>
        public static TaskComponent Parse(TextInputResponse item)
        {
            var result = new TaskComponentText(item.Name, item.Comment, item.Required, item.Order)
            {
                RegexExpression = item.RegexPattern,
                Value = item.DefaultValue,
                Id = item.Id,
                ReadOnly = item.Readonly
            };
            return result;
        }

        #endregion
    }
}
