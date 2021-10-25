package com.piramide.elwis.utils;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Utility to check if one name is duplicated in database, through a query to DataBase.
 *
 * @author Ivan
 * @version $Id: DuplicatedEntryChecker.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class DuplicatedEntryChecker {
    private Log log = LogFactory.getLog(this.getClass());
    private final String SELECT_CLAUSULE = "SELECT COUNT(*) AS count FROM ";
    private final String WHERE_CLAUSE = " WHERE ";

    /*
    * Singlenton instance
    */

    public static final DuplicatedEntryChecker i = new DuplicatedEntryChecker();

    private DuplicatedEntryChecker() {

    }

    /**
     * Check the name field in the component and verifies that not exists
     * duplicated entries in this field
     *
     * @param dto       ComponentDTO that have the value of DTO to check
     * @param resultDTO ResultDTO that will receive results
     * @param command   The command that execute the operation
     */
    public void check(ComponentDTO dto, ResultDTO resultDTO, EJBCommand command) {
        log.debug("Executing the Duplicated control checker...");
        HashMap entry = null;
        boolean isEntryDuplicated = false;
        List queryResult = null;
        String sql = null;
        String tableName = null; // table name
        HashMap keys = null;  // primary key of the table
        try {
            DuplicatedEntryDTO entryDTO = null;
            try {
                entryDTO = (DuplicatedEntryDTO) dto;
                entry = entryDTO.getDuplicatedValues();
                tableName = entryDTO.getTableName();
                keys = entryDTO.getPrimaryKey();

            } catch (ClassCastException e) {
                log.debug("This DTO do not implements DuplicatedEntryDTO interface");
            }
            ArrayList valuesList = new ArrayList();
            String names = "";
            String nameKeys = "";
            for (Iterator iterator = entry.keySet().iterator(); iterator.hasNext();) {
                String val = (String) iterator.next();
                // if the value not is null then sql use equal operator
                if ((dto.get(val) != null) && !"".equals(dto.get(val)) && !"".equals(dto.get(val))) {
                    String value = dto.get(val).toString();
                    if (value.indexOf("'") >= 0) {
                        //this is necessary if the value will be between simple qoutes (')
                        value = value.replaceAll("'", "''");
                    }
                    names += entry.get(val) + " = '" + value + "'";
                } else {
                    names += entry.get(val) + " is null"; // use "is null"
                }

                if (iterator.hasNext()) {
                    names += " AND ";
                }
                valuesList.add(entry.get(val));
                valuesList.add(val);
            }

            for (Iterator iterator = keys.keySet().iterator(); iterator.hasNext();) {
                String val = (String) iterator.next();
                nameKeys += keys.get(val) + " <> '" + dto.get(val) + "'";
                if (iterator.hasNext()) {
                    nameKeys += " AND ";
                }
            }

            if (!"create".equals(command.getOp())) {

                sql = SELECT_CLAUSULE + tableName + WHERE_CLAUSE +
                        names + " AND " + nameKeys +
                        /*dto.getPrimKeyName().toLowerCase() + " <> '" + dto.get(dto.getPrimKeyName()) +*/ " AND " +
                        Constants.COMPANYID.toLowerCase() + " = '" + dto.get(Constants.COMPANYID) + "';";

                log.debug("SQL ... " + sql);
                queryResult = QueryUtil.i.executeQuery(sql);
            } else {
                sql = SELECT_CLAUSULE + tableName + WHERE_CLAUSE +
                        names + " AND " +
                        Constants.COMPANYID.toLowerCase() + " = '" + dto.get(Constants.COMPANYID) + "';";
                log.debug("SQL ... " + sql);
                queryResult = QueryUtil.i.executeQuery(sql);
            }
            Map countList = (Map) queryResult.get(0);
            int number = Integer.parseInt(countList.get("count").toString());
            if (number != 0) {
                isEntryDuplicated = true;
            }
            if (isEntryDuplicated) {
                try {
                    entryDTO.addDuplicatedMsgTo(resultDTO);
                } catch (AbstractMethodError ex) {
                    resultDTO.addResultMessage("Common.DuplicatedEntry.message");
                }
                if (!"create".equals(command.getOp())) {
                    command.setOp("");
                    resultDTO.isClearingForm = true;
                }
                resultDTO.setResultAsFailure();
            }
        } catch (Exception e) {
            log.error("Error checking duplicated entry in DTO: " + dto.getClass(), e);
        }
    }


    /**
     * Check the name field in the component and verifies that not exists
     * duplicated entries in this field
     *
     * @param dto       ComponentDTO that have the value of DTO to check
     * @param resultDTO ResultDTO that will receive results
     * @param op        Operation of the command
     */
    public void check(ComponentDTO dto, ResultDTO resultDTO, String op) {
        log.debug("Executing the Duplicated control checker...");
        HashMap entry = null;
        boolean isEntryDuplicated = false;
        List queryResult = null;
        String sql = null;
        String tableName = null; // table name
        HashMap keys = null;  // primary key of the table
        try {
            DuplicatedEntryDTO entryDTO = null;
            try {
                entryDTO = (DuplicatedEntryDTO) dto;
                entry = entryDTO.getDuplicatedValues();
                tableName = entryDTO.getTableName();
                keys = entryDTO.getPrimaryKey();

            } catch (ClassCastException e) {
                log.debug("This DTO do not implements DuplicatedEntryDTO interface");
                return;
            }
            ArrayList valuesList = new ArrayList();
            String names = "";
            String nameKeys = "";
            for (Iterator iterator = entry.keySet().iterator(); iterator.hasNext();) {
                String val = (String) iterator.next();
                // if the value not is null then sql use equal operator
                if ((dto.get(val) != null) && !"".equals(dto.get(val)) && !"".equals(dto.get(val))) {
                    String value = dto.get(val).toString();
                    if (value.indexOf("'") >= 0) {
                        //this is necessary if the value will be between simple qoutes (')
                        value = value.replaceAll("'", "''");
                    }
                    names += entry.get(val) + " = '" + value + "'";
                } else {
                    names += entry.get(val) + " is null"; // use "is null"
                }

                if (iterator.hasNext()) {
                    names += " AND ";
                }
                valuesList.add(entry.get(val));
                valuesList.add(val);
            }

            for (Iterator iterator = keys.keySet().iterator(); iterator.hasNext();) {
                String val = (String) iterator.next();
                nameKeys += keys.get(val) + " <> '" + dto.get(val) + "'";
                if (iterator.hasNext()) {
                    nameKeys += " AND ";
                }
            }

            if (!"create".equals(op)) {

                sql = SELECT_CLAUSULE + tableName + WHERE_CLAUSE +
                        names + " AND " + nameKeys +
                        /*dto.getPrimKeyName().toLowerCase() + " <> '" + dto.get(dto.getPrimKeyName()) +*/ " AND " +
                        Constants.COMPANYID.toLowerCase() + " = '" + dto.get(Constants.COMPANYID) + "';";

                log.debug("SQL ... " + sql);
                queryResult = QueryUtil.i.executeQuery(sql);
            } else {
                sql = SELECT_CLAUSULE + tableName + WHERE_CLAUSE +
                        names + " AND " +
                        Constants.COMPANYID.toLowerCase() + " = '" + dto.get(Constants.COMPANYID) + "';";
                log.debug("SQL ... " + sql);
                queryResult = QueryUtil.i.executeQuery(sql);
            }
            Map countList = (Map) queryResult.get(0);
            int number = Integer.parseInt(countList.get("count").toString());
            if (number != 0) {
                isEntryDuplicated = true;
            }
            if (isEntryDuplicated) {
                try {
                    entryDTO.addDuplicatedMsgTo(resultDTO);
                } catch (AbstractMethodError ex) {
                    resultDTO.addResultMessage("Common.DuplicatedEntry.message");
                }
                resultDTO.setResultAsFailure();
            }
        } catch (Exception e) {
            log.error("Error checking duplicated entry in DTO: " + dto.getClass(), e);
        }
    }
}
