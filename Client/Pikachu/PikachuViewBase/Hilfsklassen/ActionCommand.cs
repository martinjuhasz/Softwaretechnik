using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace PikachuViewBase.Hilfsklassen
{
    /*
     * Standard-Basisklasse für Action-Commands
     */
    public class ActionCommand : ICommand
    {
        private readonly Action<object> _exec;
        private readonly Predicate<object> _canExec;

        public ActionCommand(Action<object> exec) : this(exec, null) { }
        public ActionCommand(Action<object> exec, Predicate<object> canExec)
        {
            if (exec == null)
            {
                throw new ArgumentNullException("execute");
            }
            _exec = exec;
            _canExec = canExec;
        }

        public bool CanExecute(object param)
        {
            return _canExec == null ? true : _canExec(param);
        }

        public event EventHandler CanExecuteChanged
        {
            add { CommandManager.RequerySuggested += value; }
            remove { CommandManager.RequerySuggested -= value; }
        }

        public void Execute(object param)
        {
            _exec(param);
        }
    }
}
