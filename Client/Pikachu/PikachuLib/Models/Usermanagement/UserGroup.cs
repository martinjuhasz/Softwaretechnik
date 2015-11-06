using System;

namespace PikachuLib.Models.Usermanagement
{
    /// <summary>
    /// Repräsentation einer Benutzergruppe
    /// </summary>
    public class UserGroup
    {
        /// <summary>
        /// Id der Benutzergruppe
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Name der Benutzergruppe
        /// </summary>
        public string Name { get; set; }

        public UserGroup(int id, string name)
        {
            Id = id;
            Name = name;
        }

        public UserGroup(String name)
        {
            Name = name;
        }
    }
}
