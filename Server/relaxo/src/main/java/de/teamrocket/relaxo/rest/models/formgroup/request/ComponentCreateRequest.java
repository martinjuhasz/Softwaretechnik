package de.teamrocket.relaxo.rest.models.formgroup.request;



/**
 * Request Objekt aller TaskComponents
 */
public class ComponentCreateRequest {

    private String name;
    private String comment;
    private boolean required;
    private String type;
    private int formGroupId;

    private String defaultValue;

    private String regex;

    private String minValue;
    private String maxValue;
    
    private int scale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFormGroupId() {
        return formGroupId;
    }

    public void setFormGroupId(int formGroupId) {
        this.formGroupId = formGroupId;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
}
