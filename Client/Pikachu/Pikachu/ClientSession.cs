using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuLib.Models;
using PikachuLib.Communication;
using PikachuLib.Models.Workflow;

namespace Pikachu
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
        /// Gibt die Instanz der aktuellen GUI-Session zurück.
        /// </summary>
        public static ClientSession Instance
        {
            get 
            {
                if (instance == null) { instance = new ClientSession(); }
                return instance;
            }
        }
        

        private ClientSession() { }
    }
}
