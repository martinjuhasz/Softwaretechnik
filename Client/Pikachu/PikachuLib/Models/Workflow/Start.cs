using System.Collections.Generic;

namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines WorkflowItems Start.
    /// </summary>
    public class Start : WorkflowItem
    {
        #region properties

        /// <summary>
        /// Liste der Benutzergruppen, die einen Job starten können.
        /// </summary>
        public List<int> Usergroups { get; set; }
        
        #endregion

        #region constructors

        public Start()
        {
            Type = WorkflowItemType.START;
        }

        public Start(int id, Position position, bool locked, IList<int> nextItem )
            : base(id, position, locked, nextItem)
        {
            Type = WorkflowItemType.START;
        }
        #endregion

        /// <summary>
        /// Parsed ein Schnittstellen-Modell in eine Start-Item
        /// </summary>
        /// <param name="response">Das Modell der Schnittstelle.</param>
        /// <returns>Ein Start Objekt</returns>
        public static Start Parse(Start startRespsponse)
        {
            return new Start{Id = startRespsponse.Id, Usergroups = new List<int>(startRespsponse.Usergroups)};
        }
    }
}
