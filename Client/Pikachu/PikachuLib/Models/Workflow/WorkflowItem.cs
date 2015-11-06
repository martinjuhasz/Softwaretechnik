
using System.Collections.Generic;

namespace PikachuLib.Models.Workflow
{
    /// <summary>
    /// Repräsentation eines WorkflowItems
    /// </summary>
    public class WorkflowItem
    {
        /// <summary>
        /// ID eines WorkflowItems
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Der Typ eines WorkflowItems
        /// </summary>
        public WorkflowItemType Type { get; protected set; }

        /// <summary>
        /// Position eines WorkflowItems
        /// </summary>
        public Position Position { get; set; }

        /// <summary>
        /// Liste der nächsten Items des WorkflowItems
        /// </summary>
        public IList<int> NextItem { get; protected set; }

        /// <summary>
        /// Gibt an, ob ein WorkflowItem gelocked ist.
        /// </summary>
        public bool Lock { get; set; }

        public WorkflowItem()
        {
            Position = new Position();
            NextItem = new List<int>();
        }

        public WorkflowItem(int id, int positionX, int positionY, bool locked, IList<int> nextItem)
            : this(id, new Position(positionX, positionY), locked, nextItem)
        {
        }

        public WorkflowItem(int id, Position position, bool locked, IList<int> nextItem )
        {
            Id = id;
            this.Position = position;
            Lock = locked;
            NextItem = nextItem;
        }
    }
}
