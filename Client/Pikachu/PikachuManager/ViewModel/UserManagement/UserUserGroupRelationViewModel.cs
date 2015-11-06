using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;
using PikachuLib;
using PikachuLib.Communication;
using PikachuLib.Models.Usermanagement;
using PikachuViewBase.Hilfsklassen;
using RestSharp.Extensions;
using GongSolutions.Wpf.DragDrop;
using DragDrop = GongSolutions.Wpf.DragDrop.DragDrop;

namespace PikachuManager.ViewModel.UserManagement
{
    class UserUserGroupRelationViewModel : ViewModelBase, IDropTarget
    {

        #region properties

        private RelaxoClient client;

        private string errorMessage ;

        public string ErrorMessage
        {
            get { return errorMessage; }
            set
            {   
                errorMessage = value;
                OnPropertyChanged();
            }
        }



        /// <summary>
        /// Liste aller Nutzergruppen
        /// </summary>
        private List<UserGroup> userGroups;
        public List<UserGroup> UserGroups
        {
            get { return userGroups; }
            set
            {
                if (!Equals(value, userGroups))
                {
                    userGroups = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Momentan ausgewählte Nutzergruppe
        /// </summary>
        private UserGroup selectedUserGroup;
        public UserGroup SelectedUserGroup
        {
            get { return selectedUserGroup; }
            set
            {
                if (!Equals(value, selectedUserGroup))
                {
                    selectedUserGroup = value;
                    ErrorMessage = null;
                    OnPropertyChanged();
                    if (selectedUserGroup != null)
                    {
                        SelectedUserGroupName = selectedUserGroup.Name;
                    }
                    else
                    {
                        SelectedUserGroupName = null;
                    }
                    GroupSelectionChanged();
                }
            }
        }

        private string selectedUserGroupName;

        public string SelectedUserGroupName
        {
            get
            {
                return selectedUserGroupName;
            }
            set
            {
                selectedUserGroupName = value;
                OnPropertyChanged();
            }
        }

        /// <summary>
        /// Nutzerliste der ausgewählten Gruppe
        /// </summary>
        private ObservableCollection<User> usersOfSelectedGroup;
        public ObservableCollection<User> UsersOfSelectedGroup {
            get
            {
                return usersOfSelectedGroup;
            }

            set
            {
                if (!Equals(value, usersOfSelectedGroup))
                {
                    usersOfSelectedGroup = value;
                    OnPropertyChanged();
                }
            } 
        }

        /// <summary>
        /// Ausgewählter Nutzer innerhalb der "ausgewählten Gruppe"-Liste
        /// </summary>
        private User selectedUserInGroup;
        public User SelectedUserInGroup
        {
            get { return selectedUserInGroup; }
            set
            {
                if (!Equals(value, selectedUserInGroup))
                {
                    selectedUserInGroup = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Alle Nutzer
        /// </summary>
        private List<User> users;
        public List<User> Users
        {
            get { return users; }
            set
            {
                if (!Equals(value, users))
                {
                    users = value;
                    OnPropertyChanged();
                }
            }
        }

        /// <summary>
        /// Ausgewählter Nutzer in der "Alle Nutzer"-Liste
        /// </summary>
        private User selectedUser;
        public User SelectedUser
        {
            get { return selectedUser; }
            set
            {
                if (!Equals(value, selectedUser))
                {
                    selectedUser = value;
                    OnPropertyChanged();
                }
            }
        }


        /// <summary>
        /// Context in dem Fenster ausgetauscht werden
        /// </summary>
        private UserManagerBaseViewModel baseViewModel;

        #endregion


        #region buttoncommands

        private ICommand editUserCommand;
        public ICommand EditUserCommand
        {
            get
            {
                if (editUserCommand == null)
                {
                    editUserCommand = new ActionCommand(EditUser);
                }
                return editUserCommand;

            }
        }

        private ICommand openUserGroupCreationCommand;
        public ICommand OpenUserGroupCreationCommand
        {
            get
            {
                if (openUserGroupCreationCommand == null)
                    openUserGroupCreationCommand = new ActionCommand(x =>
                    {
                        baseViewModel.showCreateUserGroup();
                    });
                return openUserGroupCreationCommand;
            }
      
        }

        private ICommand openUserCreationCommand;
        public ICommand OpenUserCreationCommand
        {
            get
            {
                if (openUserCreationCommand == null)
                    openUserCreationCommand = new ActionCommand(x =>
                    {
                        baseViewModel.showCreateUser();
                    });
                return openUserCreationCommand;
            }

        }

        private ICommand removeUserFromUserGroupCommand;

        public ICommand RemoveUserFromUserGroupCommand
        {
            get
            {
                if (removeUserFromUserGroupCommand == null)
                {
                    removeUserFromUserGroupCommand = new ActionCommand(RemoveUserFromUserGroup);
                }
                return removeUserFromUserGroupCommand;
            }
        }


        #endregion

        #region constructor


        public UserUserGroupRelationViewModel(UserManagerBaseViewModel baseViewModel)
        {
            this.baseViewModel = baseViewModel;
            UserGroups = ClientSession.Instance.RelaxoClient.GetAllUserGroups();
            Users = ClientSession.Instance.RelaxoClient.GetAllUsers();
            client = ClientSession.Instance.RelaxoClient;
            ClientSession.Instance.UserUserGroupUpdatedEvent += OnUserUserGroupUpdatedEvent;
        }

        private void OnUserUserGroupUpdatedEvent(EventArgs eventArgs)
        {
            var currentSelection = SelectedUserGroup;

            Users = ClientSession.Instance.RelaxoClient.GetAllUsers();
            UserGroups = ClientSession.Instance.RelaxoClient.GetAllUserGroups();

            Application.Current.Dispatcher.Invoke(() =>
            {
                if (currentSelection != null)
                {
                    SelectedUserGroup = UserGroups.SingleOrDefault(u => u.Id == currentSelection.Id);
                }
            });
            
            
            
        }

        #endregion

        #region methods

        /// <summary>
        /// Wird aufgerufen wenn sich die Auswahl der Gruppe ändert
        /// </summary>
        private void GroupSelectionChanged()
        {
            if (SelectedUserGroup != null)
            {
                var temp =  ClientSession.Instance.RelaxoClient.GetUsersByUserGroup(SelectedUserGroup.Id);
                temp.Sort((x, y) => x.Name.CompareTo(y.Name));
                UsersOfSelectedGroup = new ObservableCollection<User>(temp);
            }
            else
            {
                UsersOfSelectedGroup = null;
            }
            
        }

        private void EditUser(Object o)
        {
            var user = o as User;
            baseViewModel.showEditUser(user);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="dropInfo"></param>
        public void DragOver(IDropInfo dropInfo)
        {
            DragDrop.DefaultDropHandler.DragOver(dropInfo);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="dropInfo">Infos zum gedroppten Element</param>
        void IDropTarget.Drop(IDropInfo dropInfo)
        {

            // default Drop Handler
            ErrorMessage = null;
            var user = dropInfo.Data as User;
            if (UsersOfSelectedGroup == null)
            {
                ErrorMessage = "Bitte wählen Sie erst eine UserGroup aus bevor Sie einen User hinzufügen.";
                return;
            }

            if (SelectedUserGroup == null)
            {
                ErrorMessage = "Bitte wählen Sie erst eine UserGroup aus bevor Sie einen User hinzufügen.";
                return;
            }

            if (UsersOfSelectedGroup.SingleOrDefault(x => x.Id == user.Id) == null)
            {
                //UsersOfSelectedGroup.Add(user);
                client.AddUserToUserGroup(SelectedUserGroup.Id, user.Id);
            }
            else
            {
                ErrorMessage = "Dieser User ist bereits in dieser UserGroup";
            }

        }
       
        void RemoveUserFromUserGroup(Object o)
        {
            if (SelectedUserInGroup != null && SelectedUserGroup != null)
            {
                client.RemoveUserFromUserGroup(selectedUserGroup.Id, selectedUserInGroup.Id);
                UsersOfSelectedGroup.Remove(selectedUserInGroup);
            } 
        }

        #endregion
    }
}
