using System.Collections.ObjectModel;
using PikachuClient.ViewModel;
using PikachuLib;
using PikachuLib.Communication;
using PikachuLib.Models;
using PikachuLib.Models.Usermanagement;
using PikachuManager.ViewModel;
using PikachuViewBase.Hilfsklassen;
using System;
using System.Linq;
using System.Configuration;
using System.Diagnostics;
using System.Windows.Input;
using PikachuViewBase.ViewModel;

/*! \mainpage Pikachu - Client
 * \section client Kurzanleitung zur Inbetriebnahme des Clients
 * - Visual Studio 2012 starten
 * - Unter dem Menüpunkt Datei > Subversion den Punkt „Open from Subversion“ auswählen.
 * - Auf das Symbol „Add Repository URL“ klicken und folgende URL angeben:
 *      -# https://scm.mi.hs-rm.de/svn/2014swtpro/2014swtpro01/Client/Pikachu/
 * - Evtl. Benutzername und Passwort auf Anfrage eingeben
 * - im Fileviewer die Datei „Pikachu.sln“ markieren und auf „Open“ klicken.
 * - Das darauf folgende Fenster mit „OK“ bestätigen.
 * - Falls eine Sicherheitswarnung für vertrauenswürdige Dateien auftaucht, diese bitte enthaken und mit „OK“ bestätigen.
 * - Das Projekt „Pikachu“ mit Rechtsklick als „Startprojekt festlegen“.
 * \subsection config Konfiguration
 * - Im Projektordner „Pikachu“ befindet sich die Datei „App.config“, in Ihr kann die Serververbindung konfiguriert werden.
 * - Für Server auf dem lokalen Host bitte „localhost“ als IP eintragen \n
 * z.B.: \n
 * <appSettings> \n
 * <add key="RestServerURL" value="http://192.168.178.24:8081" /> \n
 * <add key="MessageServerURL" value="tcp://192.168.178.24:9999" /> \n
 * </appSettings> \n
 * \subsection login Client Anmeldung
 * - Wenn der Server ohne Testdaten gestartet wurde, gibt es nur einen Admin
 *      -# user: admin01 pw: testpw
 * - Wenn der Server mit Testdaten gestartet wurde, gib es zusätzlich noch drei Testuser und einen Beispielworkflow. Dieser muss noch per Admin freigegeben werden
 *      -# user: user01, user02, user03 pw: testpw
 */

namespace Pikachu.ViewModel
{
    /// <summary>
    /// Basisklasse, die Navigation und den Content beinhaltet.
    /// Die Klasse hört auf den NavigationHandler um den Inhalt entsprechend auszutauschen.
    /// </summary>
    public class MainWindowViewModel : ViewModelBase
    {
        #region properties
        
        /// <summary>
        /// Gibt die ObservableCollection für den Inhalt der Seite zurück.
        /// </summary>
        private ViewModelBase content;
        public ViewModelBase Content
        {
            get
            {
                return content;
            }
            set
            {
                if (value != content)
                {
                    content = value;
                    OnPropertyChanged();
                }
            }
        }


        /// <summary>
        /// Zeigt Fehlermeldungen an.
        /// </summary>
        private string errorMessage;
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
        /// Gibt an, ob ein Benutzer eingeloggt ist.
        /// </summary>
        private bool userLoggedIn;
        public bool UserLoggedIn
        {
            get { return userLoggedIn; }
            set
            {
                if (userLoggedIn != value)
                {
                    userLoggedIn = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Gibt Hallo + den Benutzernamen zurück.
        /// </summary>
        private string helloContent;
        public string HelloContent
        {
            get { return helloContent; }
            set
            {
                if (helloContent != value)
                {
                    helloContent = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Der Titel des Programms.
        /// </summary>
        private string titleContent;
        public string TitleContent
        {
            get { return titleContent; }
            set
            {
                if (titleContent != value)
                {
                    titleContent = value;
                    OnPropertyChanged();
                }
            }
        }

        #endregion


        #region commands

        /// <summary>
        /// Gibt den Command zurück, der nach dem Laden der Hauptseite aufgerufen wird.
        /// </summary>
        public ICommand LoadedCommand
        {
            get
            {
                return new ActionCommand(OnWindowLoaded);
            }
        }

        /// <summary>
        /// Gibt den Command zurück, der nach dem Schließen der Hauptseite aufgerufen wird.
        /// </summary>
        public ICommand ClosedCommand
        {
            get
            {
                return new ActionCommand(o =>
                {
                    if (ClientSession.Instance.MessageClient != null)
                    {
                        ClientSession.Instance.MessageClient.Disconnect();
                    }
                });
            }
        }


        /// <summary>
        /// Command für den Logout-Button.
        /// </summary>
        public ICommand LogoutCommand
        {
            get { return new ActionCommand(o => Logout()); }
        }

        #endregion


        #region constructor

        public MainWindowViewModel()
        {
            var loginVM = new LogInViewModel();
            loginVM.UserLoggedIn += LoggedIn;
            Content = loginVM;
            HelloContent = string.Empty;

            ClientSession.Instance.UserInvalidEvent += OnUserInvalid;
        }

        #endregion


        #region methods

        /// <summary>
        /// Methode, die aufgerufen wird, wenn das Fenster geladen wurde.
        /// Stellt die Serververbindungen her.
        /// </summary>
        /// <param name="o"></param>
        private void OnWindowLoaded(object o)
        {
            try
            {
                var restUrl = ConfigurationManager.AppSettings.Get("RestServerURL");
                var messageUrl = ConfigurationManager.AppSettings.Get("MessageServerURL");

                ClientSession.Instance.RelaxoClient = new RelaxoClient(restUrl);

                var client = new MessagingClient();
                client.Connect(messageUrl);
                ClientSession.Instance.MessageClient = client;
            }
            catch (Exception e)
            {
                ErrorMessage = e.Message;
                Debug.WriteLine(e.Message);
                Debug.WriteLine(e.StackTrace);
            }
        }

        /// <summary>
        /// Initiiert die Clientsession mit den benötigten Werten und setzt den richtigen Content für den User.
        /// </summary>
        /// <param name="user">Der Benutzer der eingeloggt wurde.</param>
        private void LoggedIn(User user)
        {
            // Benutzer ist eingelogged -> Setzen des aktuellen Benutzers und der MainView bescheid sagen.
            ClientSession.Instance.User = user;
            ClientSession.Instance.UpdateWorkflows(null);
            ClientSession.Instance.CurrentWorkflow = ClientSession.Instance.Workflows.ElementAtOrDefault(0);

            if (user.IsAdmin)
            {
                ClientSession.Instance.SubscribeToUserMessages();
                Content = new ManagerContainerViewModel();
                TitleContent = "Editor";
            }
            else
            {
                //ClientSession.Instance.subscribeToJobMessages();
                Content = new ClientContainerViewModel();
                TitleContent = "Client";
            }

            // Setzt den ersten workflow als current workflow.
            HelloContent = "Hallo " + user.Username;
            UserLoggedIn = true;
        }

        /// <summary>
        /// Methode die aufgerufen wird, wenn sich der Benutzer ausloggt.
        /// Sendet Delete-Request zum Server und räumt den Client auf.
        /// </summary>
        private void Logout()
        {
            try
            {
                // Current workflow muss auf null gesetzt werden, damit die ViewModels der Workflowitems geschlossen werden können.
                // Ansonsten bleiben Workflowitems gelockt, wenn sie in bearbeitung waren.
                ClientSession.Instance.CurrentWorkflow = null;
                ClientSession.Instance.Workflows.Clear();
                ClientSession.Instance.RelaxoClient.Logout();
            }
            catch (Exception ex)
            {
                Debug.WriteLine(ex);
            }
            finally
            {
                ClientSession.Instance.ResetListeners();

                HelloContent = string.Empty;
                TitleContent = string.Empty;
                UserLoggedIn = false;
                var loginVM = new LogInViewModel();
                loginVM.UserLoggedIn += LoggedIn;
                Content = loginVM;
            }
        }

        private void OnUserInvalid(EventArgs eventArgs)
        {
            ClientSession.Instance.CurrentWorkflow = null;
            ClientSession.Instance.ResetListeners();

            HelloContent = string.Empty;
            TitleContent = string.Empty;
            UserLoggedIn = false;
            var loginVM = new LogInViewModel();
            loginVM.UserLoggedIn += LoggedIn;
            Content = loginVM;
        }

        #endregion
    }
}
