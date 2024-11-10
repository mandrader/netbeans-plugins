package org.mandrader.sv.filesystem.tools.model;

import java.io.Serializable;

public class ComboItem implements Serializable {

    private static final long serialVersionUID = 7336217810005800679L;

    private String label;
    private String code;
    private Object value;

    public ComboItem(String label, String code, Object value) {
        this.label = label;
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
