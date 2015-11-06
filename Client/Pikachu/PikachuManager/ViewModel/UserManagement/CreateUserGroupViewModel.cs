using System;
using System.Windows.Input;
using PikachuLib;
using PikachuLib.Communication;
using PikachuLib.Models.Usermanagement;
using PikachuViewBase.Hilfsklassen;

namespace PikachuManager.ViewModel.UserManagement
{
    public class CreateUserGroupViewModel:ViewModelBase
    {


        #region properties

        private UserManagerBaseViewModel baseViewModel;

        private String userGroupName;

        public String UserGroupName
        {
            get { return userGroupName; }
            set { userGroupName = value; }
        }
        private RelaxoClient client;

        private String errorMessage;

        public String ErrorMessage
        {
            get { return errorMessage; }
            set
            {
                errorMessage = value;
                OnPropertyChanged();   
            }
        }


        #endregion

        #region constructor

        public CreateUserGroupViewModel(UserManagerBaseViewModel baseView)
        {
            this.client = ClientSession.Instance.RelaxoClient;
            this.baseViewModel = baseView;
        }
        #endregion
        #region buttoncommands

        private ICommand backCommand;
        public ICommand BackCommand
        {
            get
            {
                if (backCommand == null)
                    backCommand = new ActionCommand(x =>
                    {
                        back();
                    });
                return backCommand;
            }

        }
        

        private ICommand saveCommand;
        public ICommand SaveCommand
        {
            get
            {
                if (saveCommand == null)
                {
                    saveCommand = new ActionCommand(x =>
                    {
                            save();
                    });
                }
                return saveCommand;
            }
        }

        

        #endregion


        #region methods

        private void back()
        {
            baseViewModel.showGroupUserGroupRelation();
        }
        private void save()
        {
            try
            {
                var group = new UserGroup(UserGroupName);
                client.CreateUserGroup(group);
                back();
            }
            catch (RESTException e)
            {
                ErrorMessage = e.Message;
            }
            catch (Exception)
            {
                ErrorMessage = "Es ist ein unerwarteter Fehler aufgetreten. Bitte kontaktiere den " +
                               "Kundensupport unter 112.";
            }
            
        }
        #endregion
    }
}
