package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.financemanager.SequenceRuleCmd;
import com.piramide.elwis.domain.financemanager.SequenceRule;
import com.piramide.elwis.domain.financemanager.SequenceRuleHome;
import com.piramide.elwis.domain.salesmanager.ActionType;
import com.piramide.elwis.domain.salesmanager.ActionTypeSequence;
import com.piramide.elwis.domain.salesmanager.ActionTypeSequenceHome;
import com.piramide.elwis.dto.financemanager.SequenceRuleDTO;
import com.piramide.elwis.dto.salesmanager.ActionTypeDTO;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the sales process action type.
 *
 * @author Fernando Montaño
 * @version $Id: ActionTypeCmd.java 12682 2017-05-22 22:58:15Z miguel $
 */


public class ActionTypeCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ActionTypeCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        if ("create".equals(getOp())) {
            isRead = false;
            ActionTypeDTO actionTypeDTO = getActionTypeDTO();
            SequenceRuleDTO sequenceRuleDTO = getSequenceRuleDTO();
            create(actionTypeDTO, sequenceRuleDTO, ctx);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            ActionTypeDTO actionTypeDTO = getActionTypeDTO();
            SequenceRuleDTO sequenceRuleDTO = getSequenceRuleDTO();
            update(actionTypeDTO, sequenceRuleDTO, ctx);
        }

        if ("delete".equals(getOp())) {
            isRead = false;
            ActionTypeDTO actionTypeDTO = getActionTypeDTO();
            delete(actionTypeDTO, ctx);
        }

        if (isRead) {
            boolean withReferences = null != paramDTO.get("withReferences")
                    && "true".equals(paramDTO.get("withReferences").toString());
            ActionTypeDTO actionTypeDTO = getActionTypeDTO();
            read(actionTypeDTO, withReferences, ctx);
        }

    }

    private void create(ActionTypeDTO actionTypeDTO, SequenceRuleDTO sequenceRuleDTO, SessionContext ctx) {
        ActionType actionType =
                (ActionType) ExtendedCRUDDirector.i.create(actionTypeDTO, resultDTO, false);

        Integer numberId = createSequenceRule(sequenceRuleDTO, ctx);

        createActionTypeSequence(actionType.getActionTypeId(), numberId, actionType.getCompanyId(), ctx);

    }

    private void update(ActionTypeDTO actionTypeDTO, SequenceRuleDTO sequenceRuleDTO, SessionContext ctx) {
        ActionType actionType =
                (ActionType) ExtendedCRUDDirector.i.update(actionTypeDTO, resultDTO, false, true, false, "Fail");

        //The ActionType was delete by other user
        if (null == actionType) {
            return;
        }

        //version error
        if (resultDTO.isFailure()) {
            read(actionTypeDTO, false, ctx);
            return;
        }

        SequenceRule sequenceRule = getSequenceRule(actionType.getActionTypeId(), actionType.getCompanyId());

        //the ActionType doesn't relate to any sequencerule then create the relation.
        if (null == sequenceRule) {
            Integer numberId = createSequenceRule(sequenceRuleDTO, ctx);
            createActionTypeSequence(actionType.getActionTypeId(), numberId, actionType.getCompanyId(), ctx);
        } else {
            sequenceRuleDTO.put("version", sequenceRule.getVersion());
            sequenceRuleDTO.put("lastNumber", sequenceRule.getLastNumber());
            sequenceRuleDTO.put("lastDate", sequenceRule.getLastDate());
            updateSequenceRule(sequenceRuleDTO, ctx);
        }
    }

    private void delete(ActionTypeDTO actionTypeDTO, SessionContext ctx) {
        ActionType actionType = (ActionType) ExtendedCRUDDirector.i.read(actionTypeDTO, resultDTO, true);

        //ActionType was delete by other user or contain relationships
        if (null == actionType) {
            resultDTO.setForward("Fail");
            return;
        }

        ActionTypeSequence actionTypeSequence =
                getActionTypeSequence(actionType.getActionTypeId(), actionType.getCompanyId());

        if (null != actionTypeSequence) {
            Integer numberId = actionTypeSequence.getNumberId();
            try {
                actionTypeSequence.remove();
            } catch (RemoveException e) {
                log.error(ActionTypeCmd.class.getName() + " can't remove ActionTypeSequence ", e);
                ctx.setRollbackOnly();
            }
            deleteSequenceRule(numberId, ctx);
        }

        try {
            actionType.remove();
        } catch (RemoveException e) {
            log.error(ActionTypeCmd.class.getName() + " can't remove ActionType ", e);
            ctx.setRollbackOnly();
        }
    }

    private void read(ActionTypeDTO actionTypeDTO, boolean withReferences, SessionContext ctx) {
        ActionType actionType =
                (ActionType) ExtendedCRUDDirector.i.read(actionTypeDTO, resultDTO, withReferences);

        if (null == actionType) {
            return;
        }

        SequenceRule sequenceRule = getSequenceRule(actionType.getActionTypeId(), actionType.getCompanyId());
        if (null != sequenceRule) {
            SequenceRuleDTO sequenceRuleDTO = new SequenceRuleDTO();

            DTOFactory.i.copyToDTO(sequenceRule, sequenceRuleDTO);

            // remove version from sequenceRule because the actionType version is used in the view
            sequenceRuleDTO.remove("version");
            resultDTO.putAll(sequenceRuleDTO);
        }
    }


    private Integer createSequenceRule(SequenceRuleDTO sequenceRuleDTO, SessionContext ctx) {
        SequenceRuleCmd sequenceRuleCmd = new SequenceRuleCmd();
        sequenceRuleCmd.putParam(sequenceRuleDTO);
        sequenceRuleCmd.setOp("create");
        sequenceRuleCmd.executeInStateless(ctx);

        if (sequenceRuleCmd.getResultDTO().isFailure()) {
            ctx.setRollbackOnly();
        }

        return (Integer) sequenceRuleCmd.getResultDTO().get("numberId");
    }

    private void updateSequenceRule(SequenceRuleDTO sequenceRuleDTO, SessionContext ctx) {
        SequenceRuleCmd sequenceRuleCmd = new SequenceRuleCmd();
        sequenceRuleCmd.putParam(sequenceRuleDTO);
        sequenceRuleCmd.setOp("update");
        sequenceRuleCmd.executeInStateless(ctx);

        if (sequenceRuleCmd.getResultDTO().isFailure()) {
            ctx.setRollbackOnly();
        }
    }

    private void deleteSequenceRule(Integer numberId, SessionContext ctx) {
        SequenceRuleCmd sequenceRuleCmd = new SequenceRuleCmd();
        sequenceRuleCmd.putParam("numberId", numberId);
        sequenceRuleCmd.setOp("delete");
        sequenceRuleCmd.executeInStateless(ctx);
    }

    private void createActionTypeSequence(Integer actionTypeId, Integer numberId, Integer companyId, SessionContext ctx) {
        ActionTypeSequenceHome actionTypeSequenceHome =
                (ActionTypeSequenceHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTIONTYPESEQUENCE);
        try {
            actionTypeSequenceHome.create(actionTypeId, numberId, companyId);
        } catch (CreateException e) {
            log.error(ActionTypeCmd.class.getName() + " couln't stablish relation between ActionType and SequenceRule ", e);
            ctx.setRollbackOnly();
        }
    }

    private SequenceRule getSequenceRule(Integer actionTypeId, Integer companyId) {
        ActionTypeSequence sequenceRuleRelation = getActionTypeSequence(actionTypeId, companyId);
        if (null == sequenceRuleRelation) {
            return null;
        }

        return getSequenceRule(sequenceRuleRelation.getNumberId());
    }

    private SequenceRule getSequenceRule(Integer numberId) {
        SequenceRuleHome sequenceRuleHome =
                (SequenceRuleHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_SEQUENCERULE);
        try {
            return sequenceRuleHome.findByPrimaryKey(numberId);
        } catch (FinderException e) {
            return null;
        }
    }

    private ActionTypeSequence getActionTypeSequence(Integer actionTypeId, Integer companyId) {
        ActionTypeSequenceHome actionTypeSequenceHome =
                (ActionTypeSequenceHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTIONTYPESEQUENCE);
        try {
            return actionTypeSequenceHome.findByActionTypeId(actionTypeId, companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    private ActionTypeDTO getActionTypeDTO() {
        ActionTypeDTO actionTypeDTO = new ActionTypeDTO();
        EJBCommandUtil.i.setValueAsInteger(this, actionTypeDTO, "actionTypeId");
        actionTypeDTO.put("actionTypeName", paramDTO.get("actionTypeName"));
        EJBCommandUtil.i.setValueAsInteger(this, actionTypeDTO, "companyId");
        EJBCommandUtil.i.setValueAsInteger(this, actionTypeDTO, "probability");
        EJBCommandUtil.i.setValueAsInteger(this, actionTypeDTO, "sequence");
        EJBCommandUtil.i.setValueAsInteger(this, actionTypeDTO, "templateId");
        EJBCommandUtil.i.setValueAsInteger(this, actionTypeDTO, "version");

        //use in CRUD utils
        actionTypeDTO.put("withReferences", paramDTO.get("withReferences"));

        return actionTypeDTO;
    }

    private SequenceRuleDTO getSequenceRuleDTO() {
        SequenceRuleDTO sequenceRuleDTO = new SequenceRuleDTO();

        sequenceRuleDTO.put("format", paramDTO.get("format"));
        EJBCommandUtil.i.setValueAsInteger(this, sequenceRuleDTO, "numberId");
        EJBCommandUtil.i.setValueAsInteger(this, sequenceRuleDTO, "resetType");
        EJBCommandUtil.i.setValueAsInteger(this, sequenceRuleDTO, "startNumber");
        EJBCommandUtil.i.setValueAsInteger(this, sequenceRuleDTO, "companyId");
        sequenceRuleDTO.put("type", FinanceConstants.SequenceRuleType.ACTION_TYPE.getConstant());

        return sequenceRuleDTO;
    }

    public boolean isStateful() {
        return false;
    }
}