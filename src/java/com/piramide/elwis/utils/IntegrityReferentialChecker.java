package com.piramide.elwis.utils;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.ReferentialPK;
import com.piramide.elwis.dto.common.ReferentialSQL;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * /**
 * This class check referential integrity violations in one table before remove.
 *
 * @author Fernando Montaño
 * @version $Id: IntegrityReferentialChecker.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class IntegrityReferentialChecker {

    private Log log = LogFactory.getLog(this.getClass());
    private final String SELECT_CLAUSE = "SELECT COUNT(*) AS count FROM ";
    private final String WHERE_CLAUSE = " WHERE ";
    /**
     * Singleton instance.
     */
    public static final IntegrityReferentialChecker i = new IntegrityReferentialChecker();

    private IntegrityReferentialChecker() {
    }

    /**
     * Check if table relationated to dto is referenced by other table columns.
     *
     * @param dto       ComponentDTO that have the value of DTO to check
     * @param resultDTO ResultDTO that will receive result messages
     */
    public void check(ComponentDTO dto, ResultDTO resultDTO) {
        log.debug("Executing Integrity Referential Checker...");
        HashMap referentialHash = new HashMap();
        StringBuffer sql = null;
        List referencedByTables = new ArrayList();

        try {
            IntegrityReferentialDTO referentialDTO = null;
            try {
                referentialDTO = (IntegrityReferentialDTO) dto;
                referentialHash = referentialDTO.referencedValues();
            } catch (ClassCastException e) {
                log.debug("This DTO does not implements IntegrityReferentialDTO interface");
            }

            for (Iterator iterator = referentialHash.keySet().iterator(); iterator.hasNext();) {
                String tableKey = (String) iterator.next();
                sql = new StringBuffer();

                if (referentialHash.get(tableKey) instanceof ReferentialSQL) {
                    ReferentialSQL referentialSQL = (ReferentialSQL) referentialHash.get(tableKey);
                    sql.append(SELECT_CLAUSE)
                            .append(referentialSQL.getFromSQL())
                            .append(WHERE_CLAUSE)
                            .append(referentialSQL.getWhereSQL());
                    if (checkReferences(sql, null, referentialSQL.getParamList(dto))) {
                        referencedByTables.add(tableKey);
                    }

                } else if (referentialHash.get(tableKey) instanceof ReferentialPK) {//manage compound references
                    sql.append(SELECT_CLAUSE)
                            .append(tableKey)
                            .append(WHERE_CLAUSE);
                    log.debug("Compound primary key reference");
                    ReferentialPK pk = (ReferentialPK) referentialHash.get(tableKey);
                    sql.append(pk.getWhereClause(dto));

                    if (checkReferences(sql, null, pk.getParamList())) {
                        referencedByTables.add(tableKey);
                    }

                } else {
                    sql.append(SELECT_CLAUSE)
                            .append(tableKey)
                            .append(WHERE_CLAUSE);
                    //manage reference by multicolumn in same table
                    if (referentialHash.get(tableKey).toString().indexOf(",") > 0) {
                        boolean localReference = false;
                        log.debug("Multicolumn reference");
                        StringTokenizer st = new StringTokenizer(referentialHash.get(tableKey).toString(), ",");
                        while (st.hasMoreTokens()) {
                            sql = new StringBuffer();
                            sql.append(SELECT_CLAUSE)
                                    .append(tableKey)
                                    .append(WHERE_CLAUSE)
                                    .append(st.nextToken().trim())
                                    .append("=?");


                            if (checkReferences(sql, dto.getPrimKey(), null)) {
                                if (!referencedByTables.contains(tableKey)) {
                                    referencedByTables.add(tableKey);
                                }
                            }

                        }
                    } else {
                        log.debug("Simple column reference");
                        sql.append(referentialHash.get(tableKey))
                                .append("=?");
                        if (checkReferences(sql, dto.getPrimKey(), null)) {
                            referencedByTables.add(tableKey);
                        }

                    }
                }

            }
            if (!referencedByTables.isEmpty()) {
                referentialDTO.addReferencedMsgTo(resultDTO);
                resultDTO.put("referencedByTables", referencedByTables);
                resultDTO.setResultAsFailure();
            }

        } catch (Exception e) {
            log.error("Error checking integrity referential in: " + dto.getClass(), e);
        }
    }

    /**
     * Execute the query and check if result is 0 therefore is not referenced, otherwise is references
     *
     * @param sql       the sql to execute
     * @param param0    first param for query
     * @param paramList list of params for the query (Used in compund PK references)
     * @return true if is referenced, same value if not referenced.
     */
    private boolean checkReferences(StringBuffer sql, Object param0, List paramList) {
        List queryResult = null;
        if (param0 != null) {
            queryResult = QueryUtil.i.executeQuery(new String(sql), param0);
        } else {
            queryResult = QueryUtil.i.executeQuery(new String(sql), paramList);
        }

        Map countList = (Map) queryResult.get(0);
        int number = Integer.parseInt(countList.get("count").toString());
        if (number != 0) {
            return true;
        }
        return false;
    }

}
