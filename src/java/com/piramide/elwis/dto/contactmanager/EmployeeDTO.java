package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.ReferentialSQL;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Employee DTO represents data from Employee Bean
 *
 * @author Titus
 * @version EmployeeDTO.java, v 2.0 May 12, 2004 6:56:58 PM
 */

public class EmployeeDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_EMPLOYEEID = "employeeId";

    /**
     * Creates an instance.
     */
    public EmployeeDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public EmployeeDTO(DTO dto) {
        super.putAll(dto);
    }

    public EmployeeDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_EMPLOYEEID;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_EMPLOYEE;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("Employee.Duplicated", get("name1") + "" +
                ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        String message = "";
        if (get("name1") != null) {
            message = get("name1") + "" +
                    (get("name2") != null && !"".equals(get("name2")) ? ", " + get("name2") : "");
        } else if (get("employeeName") != null) {
            message = get("employeeName").toString();
        }

        if (!"".equals(message)) {
            resultDTO.addResultMessage("customMsg.NotFound", message);
        } else {
            resultDTO.addResultMessage("generalMsg.NotFound");
        }

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        String message = "";
        if (get("name1") != null) {
            message = get("name1") + "" +
                    (get("name2") != null && !"".equals(get("name2")) ? ", " + get("name2") : "");
        } else if (get("employeeName") != null) {
            message = message = get("employeeName").toString();
        }
        resultDTO.addResultMessage("customMsg.Referenced", message);
    }

    public ComponentDTO createDTO() {
        return new EmployeeDTO();
    }

    /**
     * parse values nesecary
     */
    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {

        HashMap tables = new HashMap();
/*Contact module*/
        tables.put(ContactConstants.TABLE_OFFICE, "employeeid");
        tables.put(ContactConstants.TABLE_CONTACT, "employeeid");
        tables.put(ContactConstants.TABLE_CUSTOMER, "employeeid");
/*campaign module*/
        tables.put(ContactConstants.TABLE_CAMPAIGN, "employeeid");
/*Sales Process module*/
        tables.put(SalesConstants.TABLE_SALESPROCESS, "employeeid");
        tables.put(SalesConstants.TABLE_SALE, "sellerid");
/*report module*/
        tables.put(ReportConstants.TABLE_REPORT, "employeeid");
/*Admin module*/
        tables.put(ContactConstants.TABLE_USER,
                ReferentialSQL.create()
                        .setFromSQL("elwisuser")
                        .setWhereSQL("elwisuser.type=1 and elwisuser.addressid=?")
                        .addParamDTO("employeeId"));
        /*
        tables.put(ContactConstants.TABLE_USER,
                        ReferentialPK.create()
                        .addKey("employeeId", "addressid")
                        .addKey("userTypeValue", "type"));
        */

        return tables;
    }
}