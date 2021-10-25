package com.piramide.elwis.dto.common;

import net.java.dev.strutsejb.dto.ComponentDTO;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * This class represents an structure to define a manually foreign key validation.
 * Allows you to write the sql FROM and WHERE parts giving parameters and then such sql is executed.
 *
 * @author Fernando Montaño
 * @version $Id: ReferentialSQL.java 9122 2009-04-17 00:31:07Z fernando $
 */

public class ReferentialSQL {

    private String fromSQL;
    private String whereSQL;
    private List dtoParamFields;

    /**
     * Constructor
     */
    public ReferentialSQL() {
        dtoParamFields = new LinkedList();
    }

    /**
     * Create one instance of ReferentialSQL class
     *
     * @return the ReferentialSQL instance
     */
    public static ReferentialSQL create() {
        return new ReferentialSQL();
    }

    /**
     * Define the from sql
     *
     * @param sql the sql string
     * @return the referenced instance
     */
    public ReferentialSQL setFromSQL(String sql) {
        this.fromSQL = sql;
        return this;
    }

    /**
     * Define the where conditions
     *
     * @param sql
     * @return the referenced instance
     */
    public ReferentialSQL setWhereSQL(String sql) {
        this.whereSQL = sql;
        return this;
    }


    /**
     * Add a parameter field name of the DTO
     *
     * @param dtoField the DTO field name
     * @return the current instance
     */
    public ReferentialSQL addParamDTO(String dtoField) {
        dtoParamFields.add(dtoField);
        return this;
    }

    /**
     * Return the list of the parameter values got from the DTO.
     *
     * @param dto the component dto that contains the values
     * @return the list of the parameter values
     */
    public List getParamList(ComponentDTO dto) {
        List params = new LinkedList();

        for (Iterator iterator = dtoParamFields.iterator(); iterator.hasNext();) {
            String dtoField = (String) iterator.next();
            params.add(dto.get(dtoField)); //add the value of the parameter
        }
        return params;
    }

    public String getFromSQL() {
        return fromSQL;
    }

    public String getWhereSQL() {
        return whereSQL;
    }


}
