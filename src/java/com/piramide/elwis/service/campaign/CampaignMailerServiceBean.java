package com.piramide.elwis.service.campaign;

import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.SysLevelException;
import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Service that is used to send massive mails to campaign recipients
 *
 * @author Fernando Javier Montaño Torrico
 * @version 4.3.4
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class CampaignMailerServiceBean implements CampaignMailerService {

    private Log log = LogFactory.getLog(CampaignMailerServiceBean.class);

    @Resource
    private SessionContext sessionContext;

    @Resource
    private UserTransaction userTransaction;


    /**
     * Execute the command that is responsible to send emails
     *
     * @param paramDTO     the params passed from the view layer
     * @param commandClass the command class to execute
     * @return the result map
     * @throws SystemException if the transaction cannot be rollback.
     */
    @Override
    public ResultDTO sendMassiveMails(ParamDTO paramDTO, Class commandClass) throws SystemException {
        log.debug("Param DTO= " + paramDTO);
        try {

            //execute cmd without transaction
            EJBCommand campaignSenderCmd = instantiateEJBCommand(commandClass);
            campaignSenderCmd.putParam(paramDTO);
            campaignSenderCmd.executeInStateless(sessionContext);

            return campaignSenderCmd.getResultDTO();
        } catch (Exception e) {
            log.error("Error sending campaign mails", e);
        }
        return null;
    }


    private EJBCommand instantiateEJBCommand(Class command) {
        try {
            return (EJBCommand) command.newInstance();
        } catch (InstantiationException e) {
            throw new SysLevelException(e);
        } catch (IllegalAccessException e) {
            throw new SysLevelException(e);
        }
    }

}
