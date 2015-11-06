using System.Collections.Generic;

namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines WorkflowItems End.
    /// </summary>
    public class End : WorkflowItem
    {
        #region properties
        
        #endregion

        #region constructors

        public End()
        {
            Type = WorkflowItemType.END;
        }

        public End(int id, Position position, bool locked, IList<int> nextItem)
            : base(id, position, locked, nextItem)
        {
            Type = WorkflowItemType.END;
        }
        #endregion
    }
}
