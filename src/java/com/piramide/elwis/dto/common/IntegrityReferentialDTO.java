package com.piramide.elwis.dto.common;

import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;


/**
 * Represents a builder for Integrity referential purposes that creates a List of keys
 * (tables in that DTO is referenced) and values that are the column name in those table.
 *
 * @author Fernando Montaño®
 * @version $Id: IntegrityReferentialDTO.java 9122 2009-04-17 00:31:07Z fernando $
 */
public interface IntegrityReferentialDTO {

    /**
     * Return a HashMap with the tables and columns whose references this.
     * This HashMap contains table name as Key, and column mane as value of key.
     */
    public HashMap referencedValues();

    /**
     * Add ResultMessage to indicate that this DTO is referenced
     */
    public void addReferencedMsgTo(ResultDTO resultDTO);

}
