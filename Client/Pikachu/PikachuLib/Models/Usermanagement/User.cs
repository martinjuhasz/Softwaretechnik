using System;

namespace PikachuLib.Models.Usermanagement
{

    /// <summary>
    /// Repräsentation eines Users
    /// </summary>
    public class User
    {
        #region properties
        /// <summary>
        /// Id eines Users
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Benutzername eines Users
        /// </summary>
        public string Username { get; set; }

        /// <summary>
        /// Passwort eines Users
        /// </summary>
        public string Password { get; set; }

        /// <summary>
        /// Vorname eines Users
        /// </summary>
        public string Prename { get; set; }
        
        /// <summary>
        /// Nachname eines Users
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Gibt an, ob ein User Admin ist
        /// </summary>
        public bool IsAdmin { get; set; }

        /// <summary>
        /// Gibt an, ob ein User aktiv ist
        /// </summary>
        public bool IsActive { get; set; }

        /// <summary>
        /// Token eines Users. Dient zur Authentifizierung am Server.
        /// </summary>
        public string Token { get; set; }

        #endregion

        #region constructors

        public User() { }

        public User(string username, string prename, string name,string password ,bool isAdmin, bool isActive)
        {
            Username = username;
            Prename = prename;
            Name = name;
            IsAdmin = isAdmin;
            IsActive = isActive;
            Password = password;

        }

        public User(string username, int id, string token, bool isAdmin)
        {
            Username = username;
            Id = id;
            Token = token;
            IsAdmin = isAdmin;
        }

        public User(int id, string username, string prename, string name, bool isAdmin, bool isActive)
        {
            Id = id;
            Username = username;
            Prename = prename;
            Name = name;
            IsAdmin = isAdmin;
            IsActive = isActive;
        }

        public User(int id, string username, string password, string prename, string name, bool isAdmin, bool isActive)
        {
            Id = id;
            Username = username;
            Password = password;
            Prename = prename;
            Name = name;
            IsAdmin = isAdmin;
            IsActive = isActive;
        }

        #endregion

        #region methods
       
        /// <summary>
        /// Überprüfung ob ein User eingeloggt ist. 
        /// Eingeloggte User besitzen ein Token
        /// </summary>
        /// <returns>True wenn eingeloggt, andernfalls false</returns>
        public bool LoggedIn()
        {
            if(Token == null)
            {
                return false;
            }
            return true;
        }
        #endregion
    }
}
