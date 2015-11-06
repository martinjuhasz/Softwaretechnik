using System.Collections.Generic;

namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Repräsenation eines WorkflowItems Join.
    /// </summary>
    public class Join : WorkflowItem
    {
        #region constructors

        public Join()
        {
            Type = WorkflowItemType.JOIN;
        }

        public Join(int id, Position position, bool locked, IList<int> nextItem)
            : base(id, position, locked, nextItem)
        {
            Type = WorkflowItemType.JOIN;
        }
        #endregion
    }
}
