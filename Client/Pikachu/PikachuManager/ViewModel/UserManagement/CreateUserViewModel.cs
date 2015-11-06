using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
using System.Windows.Input;
using PikachuLib;
using PikachuLib.Communication;
using PikachuLib.Models.Usermanagement;
using PikachuViewBase.Hilfsklassen;

namespace PikachuManager.ViewModel.UserManagement
{
    class CreateUserViewModel:ViewModelBase
    {
        #region Properties
        private UserManagerBaseViewModel baseViewModel;

        public User User { get; set; }

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


        #region Constructors

        public CreateUserViewModel(UserManagerBaseViewModel baseViewModel)
        {   
            this.baseViewModel = baseViewModel;
            this.client = ClientSession.Instance.RelaxoClient;
            this.User = new User();
        }


        public CreateUserViewModel(UserManagerBaseViewModel baseViewModel,User user):this(baseViewModel)
        {
            this.User = user;
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
                        if (User.Id == 0)
                        {
                            save(((PasswordBox) x).Password);
                        }
                        else
                        {
                            saveUpdate(((PasswordBox)x).Password);
                        }
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
        private void save(String password)
        {
            try
            {
                User.Password = password;
                client.CreateUser(User);
                back();
            }
            catch (RESTException e)
            {
                ErrorMessage = e.Message;
            }
            catch (Exception e)
            {
                ErrorMessage = "Es ist ein unerwarteter Fehler aufgetreten. Bitte kontaktiere den " +
                               "Kundensupport unter 112.";
            }

        }

        private void saveUpdate(String password)
        {
            if (password != "")
            {
                User.Password = password;
            }
            client.UpdateUser(User);
            back();
        }

        #endregion




    }
}
