using PikachuLib.Models.Workflow;

namespace PikachuManager.ViewModel.Workflowitems
{
    /// <summary>
    /// Factory für das Erstellen des entsprechende WorkflowitemViewModels für das Model.
    /// </summary>
    public class WorkflowitemFactory
    {
        /// <summary>
        /// Erstellt eine neue Instanz eines ViewModel entsprechend dem übergebenen WorkflowItem-Model.
        /// </summary>
        /// <param name="item">Das Workflowitem.</param>
        /// <returns>Das dem Workflowitem entsprechende ViewModel.</returns>
        public static WorkflowItemViewModel CreateWorkflowitemViewModel(WorkflowItem item)
        {
            if (item is Task)
            {
                return new TaskViewModel((Task)item);
            }

            if (item is Start)
            {
                return new StartViewModel((Start)item);
            }

            if (item is End)
            {
                return new EndViewModel((End)item);
            }
            if (item is Decision)
            {
                return new DecisionViewModel((Decision)item);
            }
            if (item is Script)
            {
                return new ScriptViewModel((Script)item);
            }
            if (item is Fork)
            {
                return new ForkViewModel((Fork)item);
            }
            if (item is Join)
            {
                return new JoinViewModel((Join)item);
            }

            return null;
        }
    }
}
