using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuLib.Communication.Models.Workflow;

namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines WorkflowItems Decision.
    /// Man kann mit Hilfe eines Python-Ausdrucks Felder validieren.
    /// </summary>
    public class Decision : WorkflowItem
    {
        #region properties

        /// <summary>
        /// Variablen der Decision.
        /// </summary>
        public List<PythonVariable> Variables { get; set; }

        /// <summary>
        /// Die if abfrage der decision in pyhton-code.
        /// </summary>
        public string Condition { get; set; }

        /// <summary>
        /// Definiert das nexte Workflowitem, wenn die Bedinung erfüllt ist.
        /// </summary>
        public int NextItemTrue { get; set; }

        #endregion

        #region constructor

        public Decision()
        {
            Variables = new List<PythonVariable>();
            Type = WorkflowItemType.DECISION;
        }

        public Decision(int id, Position position, bool locked, List<int> list)
            : base(id, position, locked, list)
        {
            Type = WorkflowItemType.DECISION;
        }

        #endregion

        #region methods
        /// <summary>
        /// Parsed ein Schnittstellen-Modell in eine Decision
        /// </summary>
        /// <param name="response">Das Modell der Schnittstelle.</param>
        /// <returns>Ein Decision Objekt</returns>
        public static Decision Parse(DecisionResponse response)
        {
            var decision = new Decision();
            decision.Id = response.Id;
            decision.Condition = response.Condition;
            if (response.NextWorkflowItemOnTrue != null)
            {
                decision.NextItemTrue = (int) response.NextWorkflowItemOnTrue;
            }
            else
            {
                decision.NextItemTrue = -1;
            }

            response.Variables.ForEach(v => decision.Variables.Add(PythonVariable.Parse(v)));

            return decision;
        }

        #endregion
    }
}
