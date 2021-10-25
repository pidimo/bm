package com.piramide.elwis.cmd.contactmanager.dataimport.configuration;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class Translation {
    private String language;
    private String text;

    public Translation(String language, String text) {
        this.language = language;
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
