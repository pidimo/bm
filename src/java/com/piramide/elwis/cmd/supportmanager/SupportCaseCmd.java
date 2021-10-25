package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.supportmanager.*;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.dto.supportmanager.StateDTO;
import com.piramide.elwis.dto.supportmanager.SupportCaseActivityDTO;
import com.piramide.elwis.dto.supportmanager.SupportCaseDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Date;

/**
 * User: yumi
 * Date: Aug 12, 2005
 * Time: 4:40:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class SupportCaseCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());
    SessionContext ctx;

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        log.debug("Executing  ... SupportCaseCmd     ...");
        this.ctx = ctx;
        if (CRUDDirector.OP_CREATE.equals(getOp())) {
            create();
        } else if (CRUDDirector.OP_UPDATE.equals(getOp())) {
            update();
        } else {
            read();
        }
    }

    public void create() throws AppLevelException {
        log.debug(" ... create supportCase function execute ...");
        SupportCaseDTO dto = new SupportCaseDTO(paramDTO);
        dto.put("descriptionId", createDescription());
        Integer companyId = (Integer) dto.get("companyId");

        if (AdminConstants.EXTERNAL_USER.equals(paramDTO.get("userType"))) {
            //paramDTO.put("isExternal", true);

            try {
                State state = (State) EJBFactory.i.callFinder(new StateDTO(), "findByOpenStage", new Object[]{companyId, new Integer(SupportConstants.SUPPORT_TYPE_STATE)});
                dto.put("stateId", state.getStateId());
            } catch (EJBFactoryException e) {
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage("SupportCase.error.internalUser.stateNotFound");
                return;
            }
        }//else paramDTO.put("isExternal", false);

        AutoAssignateUserCmd cmd = new AutoAssignateUserCmd();
        paramDTO.remove("userType");

        Integer toUserId = cmd.getPossibleUserForAssignate(paramDTO);
        int userTypeSelected = cmd.getUserType();

        if (toUserId == null) {
            resultDTO.setResultAsFailure();
            log.debug("FAILED ASSIGNED USER!!!");
            //resultDTO.setForward("Fail");
            resultDTO.addResultMessage("SupportCase.error.internal.userNotFound");
            return;
        }

        dto.put("toUserId", toUserId);

        String newNumber = InvoiceUtil.i.getSupportCaseNumber(companyId);
        if (null != newNumber) {
            dto.put("number", newNumber);
        }

        SupportCase supportCase = (SupportCase) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
        if (null == supportCase.getNumber() || "".equals(supportCase.getNumber().trim())) {
            supportCase.setNumber(supportCase.getCaseId().toString());
        }

        SupportCaseActivityCmd activityCmd = new SupportCaseActivityCmd();
        activityCmd.putParam(paramDTO);
        activityCmd.setCtx(ctx);
        SupportCaseActivity activity = activityCmd.create(supportCase);
        if (!activity.getToUserId().equals(paramDTO.get("USER_SESSIONID"))) {
            activityCmd.extractNotificationInformation(activity, activity.getToUserId(), (Integer) paramDTO.get("USER_SESSIONID"));
            if (activityCmd.getResultDTO().containsKey("TEMPLATE_PAMAMETERS")) {
                if (userTypeSelected != AutoAssignateUserCmd.SUPPORT_USER) {
                    resultDTO.put("userTypeSelected", new Integer(userTypeSelected));
                }
                resultDTO.put("TEMPLATE_PAMAMETERS", activityCmd.getResultDTO().get("TEMPLATE_PAMAMETERS"));
                resultDTO.put("MAIL_PAMAMETERS", activityCmd.getResultDTO().get("MAIL_PAMAMETERS"));
            }
        }
        resultDTO.put("caseId", supportCase.getCaseId());
        resultDTO.put("activityId", activityCmd.getResultDTO().get("activityId"));
        if (supportCase.getCloseDate() != null) {
            resultDTO.put("IS_CLOSED", "true");
        }
    }


    private Integer createDescription() throws AppLevelException {

        SupportFreeTextHome freeTextHome = (SupportFreeTextHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_FREETEXT);
        String description = (String) paramDTO.get("caseDescription");
        Integer companyId = (Integer) paramDTO.get("companyId");
        log.debug("Description:" + description);
        try {
            SupportFreeText freeText = freeTextHome.create(description.getBytes(), companyId, new Integer(FreeTextTypes.FREETEXT_SUPPORT));
            return freeText.getFreeTextId();
        } catch (CreateException e) {
            throw new AppLevelException(e.getMessage());
        }
    }

    public void update() throws AppLevelException {
        log.debug("Update supportCase");
        SupportCaseDTO dto = new SupportCaseDTO(paramDTO);
        SupportCaseHome caseHome = (SupportCaseHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CASE);
        SupportCase supportCase = null;
        try {
            supportCase = caseHome.findByPrimaryKey(new Integer(paramDTO.get("caseId").toString()));
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
            resultDTO.setForward("Fail");
            dto.addNotFoundMsgTo(resultDTO);
        }
        if (supportCase != null) {
            SupportCaseActivity caseActivity = findActivity(supportCase.getCaseId(), SupportConstants.OPENTYPE_OPEN);

            if (paramDTO.containsKey("isClosed") && !paramDTO.containsKey("reopenCase")) {
                log.debug("Not found open activity");
                State state = (State) EJBFactory.i.findEJB(new StateDTO(new Integer(paramDTO.getAsString("stateId"))));
                boolean isCloseCase = state.getStageType().intValue() == SupportConstants.CLOSE_STATE;
                if (!isCloseCase) {
                    resultDTO.put("reopenCase", "true");
                    resultDTO.put("stateId", state.getStateId());
                    resultDTO.setForward("GoPage");
                    resultDTO.addResultMessage("SupportCase.msg.reopenDescription");
                    return;
                }
            }

            supportCase = (SupportCase) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, null);
            if (supportCase != null && !resultDTO.isFailure()) {
                String description = paramDTO.getAsString("caseDescription");

                if (description != null) {
                    supportCase.getDescriptionText().setFreeTextValue(description.getBytes());
                }
                State state = (State) EJBFactory.i.findEJB(new StateDTO(supportCase.getStateId()));

                boolean isReopenCase = false;
                boolean isCloseCase = state.getStageType().intValue() == SupportConstants.CLOSE_STATE;
                if (caseActivity == null) {//el caso esta cerrado
                    log.debug("Not found open activity");
                    isReopenCase = true;
                    caseActivity = findActivity(supportCase.getCaseId(), SupportConstants.OPENTYPE_CASECLOSE);

                    if (isReopenCase && !isCloseCase) {
                        reOpenCase(supportCase, caseActivity);
                        return;
                    }
                }

                if (isCloseCase) {
                    caseActivity.setCloseDate(DateUtils.dateToInteger(new Date()));
                    caseActivity.setIsOpen(new Integer(SupportConstants.OPENTYPE_CASECLOSE));
                    supportCase.setCloseDate(caseActivity.getCloseDate());
                }

                caseActivity.setStateId(supportCase.getStateId());
                caseActivity.setWorkLevelId(supportCase.getWorkLevelId());

                //log.debug("userAssigned:" + userAssigned + " - toUser:" + supportCase.getToUserId());

                if (paramDTO.containsKey("new_activity") && !isCloseCase) {
                    log.debug("Change assigned user!!!");
                    Integer userAssigned = (Integer) paramDTO.get("toUserId");
                    SupportCaseActivityCmd activityCmd = new SupportCaseActivityCmd();
                    activityCmd.setCtx(ctx);
                    activityCmd.putParam("USER_SESSIONID", paramDTO.get("USER_SESSIONID"));
                    activityCmd.changeAsignedUser(caseActivity, userAssigned);
                    if (activityCmd.getResultDTO().containsKey("TEMPLATE_PAMAMETERS")) {
                        resultDTO.put("TEMPLATE_PAMAMETERS", activityCmd.getResultDTO().get("TEMPLATE_PAMAMETERS"));
                        resultDTO.put("MAIL_PAMAMETERS", activityCmd.getResultDTO().get("MAIL_PAMAMETERS"));
                    }
                    resultDTO.put("activityId", activityCmd.getResultDTO().get("activityId"));
                }
            } else {
                read();
            }
        }
    }

    private void reOpenCase(SupportCase supportCase, SupportCaseActivity caseActivity) throws AppLevelException {
        log.debug("reOpenCase.....");
        supportCase.setCloseDate(null);
        caseActivity.setIsOpen(new Integer(SupportConstants.OPENTYPE_CLOSE));

        SupportCaseActivityCmd activityCmd = new SupportCaseActivityCmd();
        activityCmd.setCtx(ctx);
        activityCmd.putParam("toUserId", paramDTO.get("openUserId"));
        SupportCaseActivity temporalActivity = activityCmd.createChild(caseActivity, paramDTO.getAsString("reopenDescription"));
        temporalActivity.setIsOpen(new Integer(SupportConstants.OPENTYPE_CLOSE));
        temporalActivity.setCloseDate(DateUtils.dateToInteger(new Date()));

        log.debug("Second create activity");
        activityCmd.getParamDTO().clear();
        activityCmd.putParam("toUserId", paramDTO.get("toUserId"));
        activityCmd.putParam("newWorkLevelId", supportCase.getWorkLevelId());
        activityCmd.putParam("newStateId", supportCase.getStateId());
        activityCmd.putParam("USER_SESSIONID", paramDTO.get("USER_SESSIONID"));
        SupportCaseActivity newActivity = activityCmd.createChild(temporalActivity, "");

        if (activityCmd.getResultDTO().containsKey("TEMPLATE_PAMAMETERS")) {
            resultDTO.put("TEMPLATE_PAMAMETERS", activityCmd.getResultDTO().get("TEMPLATE_PAMAMETERS"));
            resultDTO.put("MAIL_PAMAMETERS", activityCmd.getResultDTO().get("MAIL_PAMAMETERS"));
        }

        supportCase.setToUserId(newActivity.getToUserId());
        supportCase.setFromUserId(newActivity.getFromUserId());

        resultDTO.put("activityId", newActivity.getActivityId());
    }

    private SupportCaseActivity findActivity(Integer caseId, int openType) {
        try {
            return (SupportCaseActivity) EJBFactory.i.callFinder(new SupportCaseActivityDTO(),
                    "findByTypeOpen",
                    new Object[]{caseId, new Integer(openType)});
        } catch (EJBFactoryException e) {
            return null;
        }
    }

    public void read() {
        SupportCaseDTO dto = new SupportCaseDTO(paramDTO);
        log.debug("READ CMD:" + dto);
        SupportCase supportCase = (SupportCase) ExtendedCRUDDirector.i.read(dto, resultDTO, true);

        if (supportCase != null) {
            if (supportCase.getState().getStageType().intValue() == SupportConstants.CLOSE_STATE) {
                resultDTO.put("isClosed", "true");
            }
            Product product = (Product) EJBFactory.i.findEJB(new ProductDTO(supportCase.getProductId()));
            resultDTO.put("productName", product.getProductName());
            resultDTO.put("caseDescription", new String(supportCase.getDescriptionText().getFreeTextValue()));
            resultDTO.put("userAssigned", supportCase.getToUserId());

            resultDTO.put("cd", resultDTO.remove("closeDate"));
            resultDTO.put("n", resultDTO.remove("number"));
            if (supportCase.getAddressId() != null) {
                Address address = (Address) EJBFactory.i.findEJB(new AddressDTO(supportCase.getAddressId()));
                resultDTO.put("contact", address.getName());
            }
            readUserName(supportCase.getFromUserId(), "fromUserName");
            if (AdminConstants.EXTERNAL_USER.equals(paramDTO.get("userType"))) // only external users
            {
                resultDTO.put("stn", supportCase.getState().getStateName());
            }
            if (AdminConstants.INTERNAL_USER.equals(paramDTO.get("userType"))) // only internal users
            {
                readUserName(supportCase.getOpenByUserId(), "byUserName");
            }
        }

    }

    private void readUserName(Integer userId, String field) {
        User user = (User) EJBFactory.i.findEJB(new UserDTO(userId));
        Address address = (Address) EJBFactory.i.findEJB(new AddressDTO(user.getAddressId()));
        resultDTO.put(field, address.getName());
    }


    public boolean isStateful() {
        return false;
    }
}
