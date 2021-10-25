package com.piramide.elwis.dto.common;

import net.java.dev.strutsejb.dto.ComponentDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Represents a Map of PrimaryKey values used for compound primary keys in integrity referential util checker.
 *
 * @author Fernando Montaño
 * @version $Id: ReferentialPK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class ReferentialPK {

    private Log log = LogFactory.getLog(this.getClass());
    private HashMap keyFields;
    private List paramList;

    /**
     * Constructor
     */
    public ReferentialPK() {
        keyFields = new HashMap();
        paramList = new LinkedList();
    }

    /**
     * Create one instance of ReferentialPK
     *
     * @return the ReferentialPK instance
     */

    public static ReferentialPK create() {
        return new ReferentialPK();
    }

    /**
     * Adds a primary key field
     *
     * @param dtoField the DTO key field name
     * @param dbField  the respective column name for DTO field
     * @return a ReferemtialPK with new key added.
     */

    public ReferentialPK addKey(String dtoField, String dbField) {
        keyFields.put(dtoField, dbField);
        return this;
    }

    /**
     * Generate the where clause part for compound PK
     *
     * @param dto the component dto that contains the values
     * @return the String where clause
     */
    public String getWhereClause(ComponentDTO dto) {
        log.debug("Generating the where clause for compound PK");
        StringBuffer result = new StringBuffer();
        log.debug("keyFields Size= " + keyFields.size());

        int i = 0;
        for (Iterator iterator = keyFields.keySet().iterator(); iterator.hasNext();) {
            String dtoField = (String) iterator.next();
            result.append(keyFields.get(dtoField)).append("=? ");

            if (i < keyFields.size() - 1) {
                result.append("AND ");
            }

            paramList.add(dto.get(dtoField)); //add the value
            i++;
        }
        return result.toString();
    }

    /**
     * The param list for where sql generated
     *
     * @return the list of params
     */
    public List getParamList() {
        return paramList;
    }


}
