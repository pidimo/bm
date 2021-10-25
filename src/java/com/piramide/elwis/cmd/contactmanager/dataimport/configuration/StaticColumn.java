package com.piramide.elwis.cmd.contactmanager.dataimport.configuration;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class StaticColumn extends Column {
    private String resourceKey;
    private String ejbFieldName;

    public StaticColumn(Group group,
                        Integer columnId,
                        String resourceKey,
                        String ejbFieldName,
                        boolean selectableManyTimes,
                        boolean required,
                        ColumnType type) {
        super.setGroup(group);
        super.setColumnId(columnId);
        super.setType(type);
        this.ejbFieldName = ejbFieldName;
        this.resourceKey = resourceKey;
        super.setRequired(required);
        super.setSelectableManyTimes(selectableManyTimes);
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getEjbFieldName() {
        return ejbFieldName;
    }

    public void setEjbFieldName(String ejbFieldName) {
        this.ejbFieldName = ejbFieldName;
    }

    @Override
    public Column getCopy() {
        StaticColumn newStaticColumn = new StaticColumn(this.getGroup(),
                this.getColumnId(),
                this.resourceKey,
                this.ejbFieldName,
                this.isSelectableManyTimes(),
                this.isRequired(),
                this.getType());
        newStaticColumn.setValidators(this.getValidators());
        newStaticColumn.setConverter(this.getConverter());

        newStaticColumn.setPosition(this.getPosition());
        newStaticColumn.setImportColumnId(this.getImportColumnId());

        return newStaticColumn;
    }
}
