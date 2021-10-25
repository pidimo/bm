package com.piramide.elwis.service.campaign;

import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import javax.ejb.Local;
import javax.transaction.SystemException;

/**
 * Service that is used to send massive mails to campaign recipients
 *
 * @author Fernando Javier Montaño Torrico
 * @version 4.3.4
 */
@Local
public interface CampaignMailerService {

    ResultDTO sendMassiveMails(ParamDTO paramDTO, Class commandClass) throws SystemException;
}
