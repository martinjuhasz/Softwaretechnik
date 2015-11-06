using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using PikachuLib.Communication.Models.Workflow;
using PikachuLib.Models.TaskComponent;

namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Diese Klasse repräsentiert eine Python-Variable.
    /// Mit Hilfe dieser Klasse können Variablen für eine Decision oder ein Script-Item
    /// definiert werden.
    /// </summary>
    public class PythonVariable
    {
        /// <summary>
        /// Name der Python Variable
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// TaskId, aus der die Variable geholt werden soll.
        /// </summary>
        public int TaskId { get; set; }

        /// <summary>
        /// TaskComponentId, aus der die Variable geholt werden soll.
        /// </summary>
        public int TaskComponentId { get; set; }

        public PythonVariable()
        {
            Name = string.Empty;
        }

        public override bool Equals(object obj)
        {
            var other = (PythonVariable) obj;
            return other != null 
                && string.Equals(Name, other.Name) 
                && Equals(TaskId, other.TaskId) 
               && Equals(TaskComponentId, other.TaskComponentId);
        }

        /// <summary>
        /// Parsed ein Schnittstellen-Modell in eine Python-Variable
        /// </summary>
        /// <param name="taskResponse">Das Modell der Schnittstelle.</param>
        /// <returns>Ein PythonVariable-Objekt.</returns>
        public static PythonVariable Parse(PythonVariableResponse variable)
        {
            return new PythonVariable
            {
                Name = variable.Name,
                TaskId = variable.TaskId,
                TaskComponentId = variable.TaskComponentId
            };
        }
    }
}
