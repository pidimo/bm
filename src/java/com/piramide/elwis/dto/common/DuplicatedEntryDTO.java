package com.piramide.elwis.dto.common;

import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Ivan
 * @version $Id: DuplicatedEntryDTO.java 1398 2004-06-29 16:24:45Z ivan $
 */
public interface DuplicatedEntryDTO {

    public HashMap getDuplicatedValues();

    public String getTableName();

    public void addDuplicatedMsgTo(ResultDTO resultDTO);

    public HashMap getPrimaryKey();
}
