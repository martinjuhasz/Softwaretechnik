using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using PikachuLib.Communication.CustomJsonConverter;

namespace PikachuLib.Communication.Models.FormGroup
{
    /// <summary>
    /// Enum für die Eingabefeld-Typen eines Formulars.
    /// </summary>
    public enum ComponentType
    {
        TaskComponentText,
        TaskComponentInteger,
        TaskComponentFloat,
        TaskComponentDate,
        /*
        TaskComponentImage,
        TaskComponentSelection,
        TaskComponentTextarea
        */
    }

    /// <summary>
    /// Basisklasse für alle Formularfelder.
    /// </summary>
    public abstract class TaskComponentResponse
    {
        /// <summary>
        /// Die ID des Textfeldes.
        /// </summary>
        [JsonProperty("id")]
        public int Id { get; set; }

        /// <summary>
        /// Der Name des Formularfeldes.
        /// </summary>
        [JsonProperty("name")]
        public string Name { get; set; }

        /// <summary>
        /// Ein Kommentar zum Formularfeld.
        /// </summary>
        [JsonProperty("comment")]
        public string Comment { get; set; }

        /// <summary>
        /// Gibt an, ob das Feld ein Pflichtfeld ist.
        /// </summary>
        [JsonProperty("required")]
        public bool Required { get; set; }

        /// <summary>
        /// Gibt die Position im aktuellen Task an.
        /// </summary>
        [JsonProperty("order")]
        public int Order { get; set; }

        [JsonProperty("readOnly")]
        public bool Readonly { get; set; }

        /// <summary>
        /// Der Typ des Formularfeldes.
        /// </summary>
        [JsonProperty("type")]
        [JsonConverter(typeof(StringEnumConverter))]
        public ComponentType Type { get; set; }
    }


    /// <summary>
    /// Formularfeld für ein einzeiliges Textfeld.
    /// </summary>
    public class TextInputResponse : TaskComponentResponse
    {
        /// <summary>
        /// Der Typ des Formularfeldes.
        /// </summary>
        public const ComponentType TypeName = ComponentType.TaskComponentText;

        /// <summary>
        /// Gibt den default-Wert zurück.
        /// </summary>
        [JsonProperty("default")]
        public string DefaultValue { get; set; }

        /// <summary>
        /// Gibt einen Regulären Ausdruck zurück.
        /// </summary>
        [JsonProperty("regex")]
        public string RegexPattern { get; set; }
    }

    /// <summary>
    /// Formularfeld für Ganzzahlen.
    /// </summary>
    public class IntegerInputResponse : TaskComponentResponse
    {
        /// <summary>
        /// Der Typ des Formularfeldes.
        /// </summary>
        public const ComponentType TypeName = ComponentType.TaskComponentInteger;

        /// <summary>
        /// Der Minimalwert der Zahl.
        /// </summary>
        [JsonProperty("minValue")]
        public int MinValue { get; set; }

        /// <summary>
        /// Der Maximalwert der Zahl.
        /// </summary>
        [JsonProperty("maxValue")]
        public int MaxValue { get; set; }

        /// <summary>
        /// Gibt den default-Wert zurück.
        /// </summary>
        [JsonProperty("default")]
        public int DefaultValue { get; set; }
    }

    public class FloatInputResponse : TaskComponentResponse
    {
        /// <summary>
        /// Der Typ des Formularfeldes.
        /// </summary>
        public const ComponentType TypeName = ComponentType.TaskComponentFloat;

        /// <summary>
        /// Der Minimalwert der Zahl.
        /// </summary>
        [JsonProperty("minValue", DefaultValueHandling = DefaultValueHandling.Ignore)]
        public float? MinValue { get; set; }

        /// <summary>
        /// Der Maximalwert der Zahl.
        /// </summary>
        [JsonProperty("maxValue", DefaultValueHandling = DefaultValueHandling.Ignore)]
        public float? MaxValue { get; set; }

        /// <summary>
        /// Die Anzahl der Nachkommastellen
        /// </summary>
        [JsonProperty("scale", DefaultValueHandling = DefaultValueHandling.Ignore)]
        public int Scale { get; set; }

        /// <summary>
        /// Gibt den default-Wert zurück.
        /// </summary>
        [JsonProperty("default")]
        public float DefaultValue { get; set; }

        public FloatInputResponse()
        {
            MinValue = 0f;
            MaxValue = 0f;
            Scale = -1;
            DefaultValue = 0f;
        }
    }

    public class DateInputResponse : TaskComponentResponse
    {
        /// <summary>
        /// Der Typ des Formularfeldes.
        /// </summary>
        public const ComponentType TypeName = ComponentType.TaskComponentDate;

        /// <summary>
        /// Gibt den default-Wert zurück.
        /// </summary>
        [JsonProperty("default")]
        [JsonConverter(typeof(IsoDateTimeConverterAdapter))]
        public DateTime DefaultValue { get; set; } 
    }
}
