namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Mögliche Typen von WorkflowItems
    /// </summary>
    public enum WorkflowItemType
    {
        START,
        END,
        TASK,
        FORK,
        JOIN,
        DECISION,
        SCRIPT
    };
}
