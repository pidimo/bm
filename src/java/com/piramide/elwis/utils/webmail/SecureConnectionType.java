package com.piramide.elwis.utils.webmail;

/**
 *
 * @author Fernando Montaño
 */
public enum SecureConnectionType {
    SSL(1, "SSL"),
    STARTTLS(2, "STARTTLS");

    SecureConnectionType(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
    private Integer value;
    private String label;

    public String label() {
        return label;
    }
    public Integer value(){
        return value;
    }
}
