using System;
using PikachuLib.Models.Job;
using PikachuLib.Models.TaskComponent;
using PikachuViewBase.Hilfsklassen;

namespace PikachuClient.ViewModel.ComponentTypes
{
    /// <summary>
    /// Generic-Basis-Klasse für ein ComponentViewModel
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public abstract class ComponentViewModelBase<T> : ComponentViewModelBase where T : TaskComponent
    {
        /// <summary>
        /// Gibt das aktuelle TaskComponent-Model zurück.
        /// </summary>
        public new T CurrentComponent 
        {
            get { return (T) base.CurrentComponent; }
            set { base.CurrentComponent = value; }
        }
    }

    /// <summary>
    /// Basisklasse für alle TaskComponentViewModels
    /// </summary>
    public abstract class ComponentViewModelBase : ViewModelBase
    {
        #region properties

        /// <summary>
        /// Gibt das aktuelle TaskComponent-Model zurück.
        /// </summary>
        private TaskComponent currentComponent;
        public TaskComponent CurrentComponent
        {
            get { return currentComponent; }
            set
            {
                currentComponent = value;

                Id = currentComponent.Id;
                Name = currentComponent.Name;
                Comment = currentComponent.Comment;
                Required = currentComponent.Required;
                Order = currentComponent.Order;
                groupName = currentComponent.FormGroup.Name;
                readOnly = currentComponent.ReadOnly;
            }
        }

        /// <summary>
        /// Gibt die ID des Formularfeldes zurück.
        /// </summary>
        private int id;
        public int Id
        {
            get { return id; }
            set
            {
                if (id != value)
                {
                    id = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Der Name des Eingabefeldes.
        /// </summary>
        private string name;
        public string Name  
        {
            get { return name; }
            set
            {
                if (String.CompareOrdinal(name, value) == 0) return;
                name = value;
                OnPropertyChanged();
            }
        }

        /// <summary>
        /// Zusätzliche Information zur Eingabe.
        /// </summary>
        private string comment;
        public string Comment
        {
            get { return comment; }
            set
            {
                if (String.CompareOrdinal(comment, value) == 0) return;
                comment = value;
                OnPropertyChanged();
            }
        }

        /// <summary>
        /// Gibt an, ob das Feld ein Pflichtfeld ist.
        /// </summary>
        private bool required;
        public bool Required
        {
            get { return required; }
            set
            {
                if (required != value)
                {
                    required = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Gibt die Position in der Reihenfolge an.
        /// </summary>
        private int order;
        public int Order
        {
            get { return order; }
            set
            {
                if (order != value)
                {
                    order = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Der Name der Gruppe.
        /// </summary>
        private string groupName;
        public string GroupName
        {
            get { return groupName; }
        }


        /// <summary>
        /// Der Name der Gruppe.
        /// </summary>
        private bool readOnly;
        public bool CanEdit
        {
            get { return !readOnly; }
        }

        #endregion

        #region constructor

        protected ComponentViewModelBase() { }

        protected ComponentViewModelBase(TaskComponent component)
        {
            CurrentComponent = component;
        }

        /// <summary>
        /// Übernimmt die Werte des ViewModels in das Model.
        /// </summary>
        public virtual void SaveValues()
        {
            currentComponent.Id = Id;
            currentComponent.Name = Name;
            currentComponent.Comment = Comment;
            currentComponent.Required = Required;
            currentComponent.Order = Order;            
        }

        #endregion

        #region method

        /// <summary>
        /// Läd die werte eines Models in das ViewModel.
        /// </summary>
        /// <param name="jobValue">Die Werte eines Jobs.</param>
        public abstract void SetValues(JobComponent jobValue);

        #endregion
    }
}
