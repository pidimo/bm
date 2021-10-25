package com.piramide.elwis.cmd.contactmanager.dataimport.configuration;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class CategoryDinamicColumn extends DinamicColumn {
    public CategoryDinamicColumn(Group group,
                                 Integer columnId,
                                 String identifierField,
                                 boolean selectableManyTimes,
                                 boolean required,
                                 ColumnType type) {
        super(group, columnId, identifierField, selectableManyTimes, required, type);
    }

    @Override
    public Column getCopy() {
        CategoryDinamicColumn newColumn = new CategoryDinamicColumn(this.getGroup(),
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
