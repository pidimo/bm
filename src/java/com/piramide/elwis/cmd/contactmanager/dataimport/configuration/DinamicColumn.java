package com.piramide.elwis.cmd.contactmanager.dataimport.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class DinamicColumn extends Column {

    private String identifierField;
    private String defaultText;
    List<Translation> translations = new ArrayList<Translation>();

    public DinamicColumn(Group group,
                         Integer columnId,
                         String identifierField,
                         boolean selectableManyTimes,
                         boolean required,
                         ColumnType type) {

        super.setGroup(group);
        super.setColumnId(columnId);
        super.setSelectableManyTimes(selectableManyTimes);
        super.setRequired(required);
        super.setType(type);
        this.identifierField = identifierField;
    }

    public String getIdentifierField() {
        return identifierField;
    }

    public void setIdentifierField(String identifierField) {
        this.identifierField = identifierField;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

    public void addTranslation(Translation translation) {
        translations.add(translation);
    }

    public Translation getTranslation(String language) {
        for (Translation translation : translations) {
            if (translation.getLanguage().equals(language)) {
                return translation;
            }
        }

        return null;
    }

    @Override
    public Column getCopy() {
        DinamicColumn newColumn = new DinamicColumn(this.getGroup(),
                this.getColumnId(),
                this.getIdentifierField(),
                this.isSelectableManyTimes(),
                this.isRequired(),
                this.getType());
        newColumn.setTranslations(this.getTranslations());
        newColumn.setValidators(this.getValidators());
        newColumn.setConverter(this.getConverter());
        newColumn.setDefaultText(this.getDefaultText());

        newColumn.setPosition(this.getPosition());
        newColumn.setImportColumnId(this.getImportColumnId());

        return newColumn;
    }
}

