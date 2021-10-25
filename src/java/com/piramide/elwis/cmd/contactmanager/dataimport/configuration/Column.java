package com.piramide.elwis.cmd.contactmanager.dataimport.configuration;

import com.piramide.elwis.cmd.contactmanager.dataimport.filemanager.DataRow;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.Converter;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.RequiredValidator;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.ValidationException;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.Validator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class Column {
    private Log log = LogFactory.getLog(Column.class);

    public static enum ColumnType {
        ADDRESS(1),
        CONTACT_PERSON(2),
        CUSTOMER(3);

        private Integer constant;

        private ColumnType(Integer constant) {
            this.constant = constant;
        }

        public Integer getConstant() {
            return constant;
        }
    }

    private Integer columnId;
    private Integer position;
    private boolean selectableManyTimes;
    private List<Validator> validators = new ArrayList<Validator>();
    private Converter converter;
    private boolean required;
    private Group group;
    private ColumnType type;
    private Integer uiPosition;

    private Integer importColumnId;

    public Integer getColumnId() {
        return columnId;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public boolean isSelectableManyTimes() {
        return selectableManyTimes;
    }

    public void setSelectableManyTimes(boolean selectableManyTimes) {
        this.selectableManyTimes = selectableManyTimes;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<Validator> getValidators() {
        return validators;
    }

    public void setValidators(List<Validator> validator) {
        this.validators = validator;
    }


    public void addValidator(Validator validator) {
        validators.add(validator);
    }

    public Validator getValidator(String className) {
        for (Validator validator : validators) {
            if (validator.getClass().getName().equals(className)) {
                return validator;
            }
        }

        return null;
    }

    public void validate(DataImportConfiguration configuration, DataRow data) throws ValidationException {
        if (validators.isEmpty()) {
            return;
        }

        if (null == position) {
            log.warn("The attribute position for Column columnId=" + columnId + "isn't defined, and cannot execute the validators");
            return;
        }

        String value = data.getValue(position);

        for (Validator validator : validators) {
            if (validator instanceof RequiredValidator) {
                validator.validate(configuration, this, data.getRowNumber(), value);
            } else if (value != null && !"".equals(value.trim())) {
                validator.validate(configuration, this, data.getRowNumber(), value);
            }
        }

    }

    public Object applyConverter(DataImportConfiguration configuration, String value) {
        if (null == converter) {
            return value;
        }
        if (null != value && !"".equals(value.trim())) {
            return converter.parse(configuration, value);
        }

        return value;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public Column getCopy() {
        return new Column();
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public Integer getUiPosition() {
        return uiPosition;
    }

    public void setUiPosition(Integer uiPosition) {
        this.uiPosition = uiPosition;
    }

    @Override
    public String toString() {
        return columnId + "-" + position;
    }

    public Integer getImportColumnId() {
        return importColumnId;
    }

    public void setImportColumnId(Integer importColumnId) {
        this.importColumnId = importColumnId;
    }
}
