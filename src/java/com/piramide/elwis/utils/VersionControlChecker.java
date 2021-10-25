package com.piramide.elwis.utils;

import net.java.dev.strutsejb.dto.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalObject;

/**
 * This class makes version control for concurrent modification.
 *
 * @author Fernando Montaño
 * @version $Id: VersionControlChecker.java 9123 2009-04-17 00:32:52Z fernando $
 */
public class VersionControlChecker {

    private Log log = LogFactory.getLog(this.getClass());
    /**
     * Singleton instance.
     */
    public static final VersionControlChecker i = new VersionControlChecker();

    private VersionControlChecker() {
    }

    /**
     * Check if the dto version attribute is identical to entity version value.
     *
     * @param dto       ComponentDTO that have the value of DTO to check
     * @param resultDTO ResultDTO that will receive results
     */
    public synchronized void check(ComponentDTO dto, ResultDTO resultDTO, ParamDTO paramDTO) {
        log.debug("Begin Version Control for DTO = " + dto.getClass());
        try {
            //Finding the entity before update
            EJBLocalObject entity = (EJBLocalObject) EJBFactory.i.findEJB(dto);
            DTOFactory.i.copyToDTO(entity, resultDTO);

            if (resultDTO.get("version").toString().equals(dto.get("version").toString())) {
                int currentVersion = Integer.parseInt(resultDTO.get("version").toString());
                // update the next value in paramDTO for database update
                paramDTO.put("version", new Integer(++currentVersion));

            } else { // if the version values are not equal
                log.debug("Version values not are equals");
                resultDTO.isClearingForm = true;
                resultDTO.addResultMessage("Common.error.concurrency"); // concurrency message
                paramDTO.setOp(""); // invalidate the update
                resultDTO.setResultAsFailure(); // return to input
            }

        } catch (EJBFactoryException e) {
            log.debug("EJBFactoryException catched, Bean has been deleted by other user");
            resultDTO.put("EntityBeanNotFound", "true");
            return;
        }
    }

}
