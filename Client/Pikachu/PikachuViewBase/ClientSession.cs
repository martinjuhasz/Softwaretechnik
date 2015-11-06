using PikachuLib.Communication;
using PikachuLib.Models;
using PikachuLib.Models.Workflow;

namespace PikachuViewBase
{
    /// <summary>
    /// Alle Eigenschaften die für die aktuelle Session in der GUI notwendig sind, werden hier gehalten.
    /// </summary>
    public class ClientSession
    {
        /// <summary>
        /// Gibt den aktuell angemeldeten Benutzer zurück.
        /// </summary>
        public User User { get; set; }

        /// <summary>
        /// Gibt den verwendeten Client zur Kommunikation zurück.
        /// </summary>
        public RelaxoClient RelaxoClient { get; set; }

        /// <summary>
        /// Der aktuelle Workflow, der in bearbeitung ist.
        /// </summary>
        public Workflow CurrentWorkflow { get; set; }



        private static ClientSession instance;
        /// <summary>
        /// Gibt die Instanz der aktuellen GUI-Session zurück (Singleton).
        /// </summary>
        public static ClientSession Instance
        {
            get { return instance ?? (instance = new ClientSession()); }
        }

        private ClientSession() { }
    }
}
