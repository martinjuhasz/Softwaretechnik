using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PikachuLib.Models.Usermanagement;
using PikachuManager.ViewModel.UserManagement;
using PikachuViewBase.Hilfsklassen;
using PikachuViewBase.ViewModel;
using Xceed.Wpf.AvalonDock.Layout;

namespace PikachuManager.ViewModel
{
    /// <summary>
    /// Container-Klasse für das verwalten von Benutzern.
    /// </summary>
    public class UserManagerBaseViewModel : ViewModelBase
    {
        private ViewModelBase content;

        /// <summary>
        /// Gibt die ObservableCollection für den Inhalt der Seite zurück.
        /// </summary>
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

        public UserManagerBaseViewModel()
        {
            showGroupUserGroupRelation();
        }

        /// <summary>
        /// Zeigt die die Nutzer-Nutzergruppen Verwaltung an.
        /// </summary>
        public void showGroupUserGroupRelation()
        {
            Content = new UserUserGroupRelationViewModel(this);
        }
        /// <summary>
        /// Zeigt die View an um einen Nutzer zu erstellen
        /// </summary>
        public void showCreateUser()
        {
            Content = new CreateUserViewModel(this);
        }

        public void showEditUser(User user)
        {
            Content = new CreateUserViewModel(this,user);
        }

        
        /// <summary>
        /// Zeigt die View an um eine Nutzergruppe zu erstellen
        /// </summary>
        public void showCreateUserGroup()
        {
            Content = new CreateUserGroupViewModel(this);
        }
    }
}
