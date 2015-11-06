using System;
using System.Diagnostics;
using PikachuLib;
using PikachuLib.Models.Usermanagement;
using PikachuViewBase.Hilfsklassen;
using System.Windows.Input;
using PikachuLib.Communication;
using System.Windows.Controls;

namespace Pikachu.ViewModel
{
    public delegate void LoggedInDelegate(User user);

    /// <summary>
    /// ViewModel für den Login-Prozess.
    /// </summary>
    public class LogInViewModel : ViewModelBase
    {
        /// <summary>
        /// Event das gefeuert wird, wenn der Login erfolgreich war.
        /// </summary>
        public event LoggedInDelegate UserLoggedIn;

        #region properties

        private string errorMessage;
        /// <summary>
        /// Gibt eine Fehlermeldung zurück.
        /// </summary>
        public string ErrorMessage
        {
            get { return errorMessage; }
            set
            {
                if (errorMessage != value)
                {
                    errorMessage = value;
                    OnPropertyChanged();
                }
            }
        }


        /// <summary>
        /// Der Benutzername vom Loginfeld.
        /// </summary>
        public string Username { get; set; }

        #endregion

        #region commands

        private ICommand loginCommand;
        /// <summary>
        /// Login-Command.
        /// </summary>
        public ICommand LogInCommand
        {
            get
            {
                if (loginCommand == null)
                {
                    loginCommand = new ActionCommand(x=>LogIn(Username, ((PasswordBox)x).Password), x=>ValidateInput());
                }

                return loginCommand;
            }
        }


        #endregion

        #region constructor
        
        public LogInViewModel()
        {
            //Username = "jwolf001";
        }

        #endregion

        #region methods

        /// <summary>
        /// Validiert, ob die Formularfelder richtig ausgefüllt wurden.
        /// </summary>
        /// <returns></returns>
        private bool ValidateInput()
        {
            return (
                ClientSession.Instance.MessageClient != null &&
                ClientSession.Instance.MessageClient.IsConnected &&
                ClientSession.Instance.RelaxoClient != null &&
                !String.IsNullOrWhiteSpace(Username));
        }

        /// <summary>
        /// Methode Versucht einen Benutzer am Server anzumelden.
        /// </summary>
        /// <param name="username">Der Benutzername.</param>
        /// <param name="password">Das Passwort des Benutzers.</param>
        private void LogIn(string username, string password)
        {
            try
            {
                RelaxoClient client = ClientSession.Instance.RelaxoClient;
                User user = client.LogIn(username, password);
                if (user != null)
                {
                    UserLoggedIn(user);
                }
            }
            catch (RESTException e)
            {
                ErrorMessage = String.Format("Error-Code: {0}\r\nMessage:{1}", e.Code, e.Message);
                Debug.WriteLine(ErrorMessage);
                Debug.WriteLine(e.StackTrace);
            }
            catch (Exception e)
            {
                ErrorMessage = e.Message;
                Debug.WriteLine(ErrorMessage);
                Debug.WriteLine(e.StackTrace);
            }
        }
        
        #endregion
    }
}
