using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Data;
using GongSolutions.Wpf.DragDrop;
using PikachuLib.Models;

namespace PikachuManager.UserControls
{
    /// <summary>
    /// ViewModel für das Verwalten von Benutzergruppen für Workflowitems.
    /// </summary>
    public class UsergroupControlViewModel
    {
        /// <summary>
        /// Die DropHandler Klasse für das Drag&Drop.
        /// </summary>
        private class CustomDropHandler : DefaultDropHandler
        {
            public override void Drop(IDropInfo dropInfo)
            {
                if (dropInfo.TargetCollection.Cast<object>().Contains(dropInfo.Data))
                {
                    return;
                }

                base.Drop(dropInfo);
            }
        }


        /// <summary>
        /// Gibt den Drop-Handler zurück.
        /// </summary>
        public DefaultDropHandler SelectedUserDropDropHandler
        {
            get { return new CustomDropHandler(); }
        }
        
    }
}
