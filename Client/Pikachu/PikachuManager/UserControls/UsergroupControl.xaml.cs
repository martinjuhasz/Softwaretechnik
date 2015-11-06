using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using PikachuLib.Models;
using PikachuLib.Models.Usermanagement;

namespace PikachuManager.UserControls
{
    /// <summary>
    /// Interaktionslogik für UsergroupControl.xaml
    /// </summary>
    public partial class UsergroupControl : UserControl
    {
        /// <summary>
        /// Liste alle verfügbaren Benutzergruppen.
        /// </summary>
        public ObservableCollection<UserGroup> Usergroups
        {
            get { return (ObservableCollection<UserGroup>) GetValue(UsergroupsProperty); }
            set { SetValue(UsergroupsProperty, value); }
        }

        /// <summary>
        /// Dependency Property für alle Benutzergruppen.
        /// </summary>
        public static readonly DependencyProperty UsergroupsProperty =
            DependencyProperty.Register("Usergroups", typeof (ObservableCollection<UserGroup>),
                typeof (UsergroupControl),
                new UIPropertyMetadata(null));


        /// <summary>
        /// Benutzergruppenliste, die alle hinzugefügt Benutzergruppen beinhaltet.
        /// </summary>
        public ObservableCollection<UserGroup> SelectedUserGroups
        {
            get { return (ObservableCollection<UserGroup>) GetValue(SelectedUserGroupsProperty); }
            set { SetValue(SelectedUserGroupsProperty, value); }
        }

        /// <summary>
        /// Dependency Property für die hinzugefügten Benutzergruppen.
        /// </summary>
        public static readonly DependencyProperty SelectedUserGroupsProperty =
            DependencyProperty.Register("SelectedUserGroups", typeof(ObservableCollection<UserGroup>),
                typeof (UsergroupControl),
                new UIPropertyMetadata(null));

        public UsergroupControl()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Remove-Button-Klick Event. Hier in der Codebehind, da die View als eigenständiges Control gesehen werden kann,
        /// dass Benutzergruppen zu einer Liste hinzufügen und löschen kann.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void OnRemoveUsergroupClick(object sender, RoutedEventArgs e)
        {
            var itemsToRemove = addedListView.SelectedItems.Cast<UserGroup>().ToList();

            foreach (var item in itemsToRemove)
            {
                SelectedUserGroups.Remove(item);
            }
        }
    }
}
