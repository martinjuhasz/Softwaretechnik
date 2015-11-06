namespace PikachuLib.Models.TaskComponent
{
    /// <summary>
    /// Basisklasse für eine Repräsentation eines Formularfeldes.
    /// </summary>
    public abstract class TaskComponent
    {
        #region properties

        /// <summary>
        /// Gibt die ID des Formularfeldes zurück.
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Der Name des Eingabefeldes.
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Zusätzliche Information zur Eingabe.
        /// </summary>
        public string Comment { get; set; }

        /// <summary>
        /// Gibt an, ob das Feld ein Pflichtfeld ist.
        /// </summary>
        public bool Required { get; set; }

        /// <summary>
        /// Gibt die Position in der Reihenfolge an.
        /// </summary>
        public int Order { get; set; }

        /// <summary>
        /// Gibt True  zurück, wenn das Feld nur gelesen werden darf.
        /// </summary>
        public bool ReadOnly { get; set; }

        /// <summary>
        /// Gibt die Forumlargruppe zurück, in der die Komponente zugeordnet ist.
        /// </summary>
        public TaskComponentGroup FormGroup { get; set; }

        #endregion

        #region constructors

        protected TaskComponent() { }

        protected TaskComponent(string name, string comment, bool required, int order)
        {
            Name = name;
            Comment = comment;
            Required = required;
            Order = order;
            ReadOnly = false;
        }

        protected TaskComponent(string name, string comment, bool required, int order, bool isReadonly)
        : this(name, comment, required, order)
        {
            ReadOnly = isReadonly;
        }

        #endregion

        #region methods

        public override string ToString()
        {
            return Name;
        }

        public override bool Equals(object obj)
        {
            var other = obj as TaskComponent;
            return other != null && Id == other.Id;
        }

        #endregion
    }
}
