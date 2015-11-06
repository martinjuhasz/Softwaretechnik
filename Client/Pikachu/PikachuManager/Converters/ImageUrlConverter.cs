using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace PikachuManager.Converters
{
    /// <summary>
    /// Converter für Bilder.
    /// Wandelt einen Pfad (string) in eine ImageSource um.
    /// http://www.codeproject.com/Articles/484616/MVVM-Diagram-Designer
    /// </summary>
    [ValueConversion(typeof(string), typeof(ImageSource))]
    public class ImageUrlConverter : IValueConverter
    {
        static ImageUrlConverter()
        {
            Instance = new ImageUrlConverter();
        }

        public static ImageUrlConverter Instance
        {
            get;
            private set;
        }

        /// <summary>
        /// Konvertiert eine Uri zu einem Bild zu einer ImageSource.
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            Uri imagePath = new Uri(value.ToString(), UriKind.RelativeOrAbsolute);
            ImageSource source = new BitmapImage(imagePath);
            return source;
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
