using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using PikachuLib.Communication.Models;
using PikachuLib.Communication.Models.FormGroup;

namespace PikachuLib.Communication.CustomJsonConverter
{
    /// <summary>
    /// Klasse für das convertieren der einzelnen Formularfelder von JSON in das entsprechende C# Objekt.
    /// Wird benötigt, da es verschiedene Typen der Formularfelder gibt, diese werden hier identifiziert und entsprechend geparsed.
    /// </summary>
    public class TaskComponentJsonConverter : JsonConverter
    {

        /// <summary>
        /// JSON-Feld Name, in der die Typenangabe des Feldes steht.
        /// </summary>
        private const string TypeAttribute = "type";


        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {
            var jobj = (JObject)serializer.Deserialize(reader, typeof(JObject));
            var type = (ComponentType) Enum.Parse(typeof (ComponentType), jobj[TypeAttribute].ToString());
            var conType = GetTaskComponentType(type);

            return conType != null ? JsonConvert.DeserializeObject(jobj.ToString(), conType) : null;
        }

        public override bool CanConvert(Type objectType)
        {
            return objectType == typeof(TaskComponentResponse);
        }

        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {
            serializer.Serialize(writer, value);
        }


        /// <summary>
        /// Gibt den entsprechenden TaskComponent-Type des übergebenen ComponentTypes zurück.
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        private Type GetTaskComponentType(ComponentType type)
        {
            switch (type)
            {
                case ComponentType.TaskComponentText:
                    return typeof(TextInputResponse);

                case ComponentType.TaskComponentInteger:
                    return typeof (IntegerInputResponse);

                case ComponentType.TaskComponentFloat:
                    return typeof (FloatInputResponse);

                case ComponentType.TaskComponentDate:
                    return typeof (DateInputResponse);

                default:
                    throw new Exception(string.Format("Exception: {0} im case nicht definiert", type));
            }
        }
    }
}
