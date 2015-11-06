using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuLib.Communication.Models.Workflow;

namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Ein Workflowitem, mit dem man ein ausführbares Python-Skript definieren kann.
    /// </summary>
    public class Script : WorkflowItem
    {
        #region properties

        /// <summary>
        /// Variablen des Scripts für Python.
        /// </summary>
        public List<PythonVariable> Variables { get; set; }

        /// <summary>
        /// Der ausführbare code in python.
        /// </summary>
        public string ScriptContent { get; set; }

        #endregion

        #region constructor

        public Script()
        {
            Variables = new List<PythonVariable>();
            Type = WorkflowItemType.SCRIPT;
        }

        public Script(int id, Position position, bool locked, List<int> list)
            : base(id, position, locked, list)
        {
            Variables = new List<PythonVariable>();
            Type = WorkflowItemType.SCRIPT;
        }

        #endregion

        #region methods

        /// <summary>
        /// Parsed ein Schnittstellen-Modell in ein Script-Itme
        /// </summary>
        /// <param name="taskResponse">Das Modell der Schnittstelle.</param>
        /// <returns>Ein Script-Objekt.</returns>
        public static Script Parse(ScriptResponse response)
        {
            var script = new Script();
            script.Id = response.Id;
            script.ScriptContent = response.ScriptContent;

            response.Variables.ForEach(v => script.Variables.Add(PythonVariable.Parse(v)));

            return script;
        }

        #endregion
    }
}
