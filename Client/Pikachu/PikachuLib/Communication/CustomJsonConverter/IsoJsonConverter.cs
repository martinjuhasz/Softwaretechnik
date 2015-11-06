using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Converters;

namespace PikachuLib.Communication.CustomJsonConverter
{
    /// <summary>
    /// Klasse für das Konvertieren von Datums-Formate in JSON.
    /// </summary>
    public class IsoDateTimeConverterAdapter : DateTimeConverterBase
    {
        private readonly IsoDateTimeConverter converter;

        public IsoDateTimeConverterAdapter()
        {
            converter = new IsoDateTimeConverter {DateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss"};
        }

        public override object ReadJson(Newtonsoft.Json.JsonReader reader, Type objectType, object existingValue, Newtonsoft.Json.JsonSerializer serializer)
        {
            return converter.ReadJson(reader, objectType, existingValue, serializer);
        }

        public override void WriteJson(Newtonsoft.Json.JsonWriter writer, object value, Newtonsoft.Json.JsonSerializer serializer)
        {
            converter.WriteJson(writer, value, serializer);
        }
    }
}
