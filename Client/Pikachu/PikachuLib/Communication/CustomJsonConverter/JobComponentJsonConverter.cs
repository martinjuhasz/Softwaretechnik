using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using PikachuLib.Communication.Models;
using PikachuLib.Communication.Models.FormGroup;
using PikachuLib.Communication.Models.Job;

namespace PikachuLib.Communication.CustomJsonConverter
{
    /// <summary>
    /// Klasse für das convertieren der einzelnen Formularfelder von JSON in das entsprechende C# Objekt.
    /// Wird benötigt, da es verschiedene Typen der Formularfelder gibt, diese werden hier identifiziert und entsprechend geparsed.
    /// </summary>
    public class JobComponentJsonConverter : JsonConverter
    {
        /// <summary>
        /// JSON-Feld Name, in der die Typenangabe des Feldes steht.
        /// </summary>
        private const string TypeAttribute = "type";



        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {
            serializer.Serialize(writer, value);
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {
            var jobj = (JObject)serializer.Deserialize(reader, typeof(JObject));
            var type = (ComponentType) Enum.Parse(typeof (ComponentType), jobj[TypeAttribute].ToString());
            Type conType = GetJobComponentType(type);

            return conType != null ? JsonConvert.DeserializeObject(jobj.ToString(), conType) : null;
        }

        public override bool CanConvert(Type objectType)
        {
            return objectType == typeof(JobComponentResponse);
        }

        /// <summary>
        /// Gibt den entsprechenden JobComponent-Type des übergebenen ComponentTypes zurück.
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        private Type GetJobComponentType(ComponentType type)
        {
            switch (type)
            {
                case ComponentType.TaskComponentText:
                    return typeof(JobComponentResponseText);

                case ComponentType.TaskComponentInteger:
                    return typeof (JobComponentResponseInteger);

                case ComponentType.TaskComponentFloat:
                    return typeof (JobComponentResponseFloat);

                case ComponentType.TaskComponentDate:
                    return typeof(JobComponentResponseDate);

                default:
                    throw new Exception(string.Format("Exception: {0} im case nicht definiert", type));
            }
        }
    }
}
