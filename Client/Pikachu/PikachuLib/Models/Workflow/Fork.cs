using System.Collections.Generic;

namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines WorkflowItems Fork.
    /// </summary>
    public class Fork : WorkflowItem
    {
        #region constructors

        public Fork()
        {
            Type = WorkflowItemType.FORK;
        }

        public Fork(int id, Position position, bool locked, IList<int> nextItem)
            : base(id, position, locked, nextItem)
        {
            Type = WorkflowItemType.FORK;
        }
        #endregion
    }
}
