using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuViewBase.Hilfsklassen;

namespace PikachuManager.ViewModel.DragNDrop
{
    /// <summary>
    /// Singleton zum Austausch von Fehlermeldungen zwischen verschiedenen ViewModels.
    /// </summary>
    public class WorkflowEditorDesignerCanvasErrorViewModel : ViewModelBase
    {
        private static WorkflowEditorDesignerCanvasErrorViewModel instance;
        public static WorkflowEditorDesignerCanvasErrorViewModel Instance
        {
            get
            {
                if (instance != null)
                {
                    return instance;
                }
                else
                {
                    instance = new WorkflowEditorDesignerCanvasErrorViewModel();
                    return instance;
                }
            }
        }

        private string error;
        public string Error
        {
            get { return error; }
            set
            {
                if(!Equals(error, value))
                {
                    error = value;
                    OnPropertyChanged();
                }
            }
        }
    }
}
