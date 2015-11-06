using System;
using System.Collections.Generic;
using PikachuLib.Communication.Models;
using PikachuLib.Communication.Models.FormGroup;

namespace PikachuLib.Models.TaskComponent
{
    /// <summary>
    /// Repräsentation einer Formulargruppe.
    /// </summary>
    public class TaskComponentGroup
    {

        #region properties

        /// <summary>
        /// Die ID der Formulargruppe.
        /// </summary>
        public int Id { get; private set; }

        /// <summary>
        /// Name der Formulargruppe
        /// </summary>
        public string Name { get; private set; }

        /// <summary>
        /// Die enthaltenen Formularfelder.
        /// </summary>
        public List<TaskComponent> Components { get; private set; }

        #endregion


        #region constructors

        public TaskComponentGroup(int id, string name)
        {
            Id = id;
            Name = name;
            Components = new List<TaskComponent>();
        }

        #endregion

        #region methods
        /// <summary>
        /// Parsed ein Schnittstellen-Modell in einen Task
        /// </summary>
        /// <param name="taskComponentGroupResponse">Das Modell der Schnittstelle.</param>
        /// <returns>Ein Task-Objekt.</returns>
        public static TaskComponentGroup Parse(TaskComponentGroupResponse taskComponentGroupResponse)
        {
            var taskComponentGroup = new TaskComponentGroup(taskComponentGroupResponse.Id, taskComponentGroupResponse.Name);

            foreach (var item in taskComponentGroupResponse.Components)
            {
                if (item == null) continue;

                var component = GetTaskComponent(item);
                component.FormGroup = taskComponentGroup;
                taskComponentGroup.Components.Add(component);
            }

            return taskComponentGroup;
        }

        /// <summary>
        /// Gibt für ein Repräsntation eines Formularfeldes der Kommunikationsscshicht das richtige Model zurück.
        /// </summary>
        /// <param name="item">Das Model der Kommunikationsschicht.</param>
        /// <returns>Formularfeld-Model.</returns>
        private static TaskComponent GetTaskComponent(TaskComponentResponse item)
        {
            switch (item.Type)
            {
                case ComponentType.TaskComponentText:
                    return TaskComponentText.Parse((TextInputResponse)item);

                case ComponentType.TaskComponentInteger:
                    return TaskComponentInteger.Parse((IntegerInputResponse) item);

                case ComponentType.TaskComponentFloat:
                    return TaskComponentFloat.Parse((FloatInputResponse)item);

                case ComponentType.TaskComponentDate:
                    return TaskComponentDate.Parse((DateInputResponse) item);

                default:
                    throw new Exception(string.Format("Exception: {0} im case nicht definiert", item.Type));
            }
        }

        #endregion
    }
}
