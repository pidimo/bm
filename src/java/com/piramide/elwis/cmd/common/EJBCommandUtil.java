package com.piramide.elwis.cmd.common;

import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * This class contains utilitarian methods for handling objects in the ejb commands
 * <p/>
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class EJBCommandUtil {

    private Log log = LogFactory.getLog(EJBCommandUtil.class);

    public static EJBCommandUtil i = new EJBCommandUtil();

    private EJBCommandUtil() {

    }

    public Integer getValueAsInteger(EJBCommand ejbCommand, String key) {
        Integer value = null;
        if (null != ejbCommand.getParamDTO().get(key) &&
                !"".equals(ejbCommand.getParamDTO().get(key).toString().trim())) {
            try {
                value = Integer.valueOf(ejbCommand.getParamDTO().get(key).toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse " + key + "=" + ejbCommand.getParamDTO().get(key) + " FAIL");
            }
        }

        return value;
    }

    public void setValueAsInteger(EJBCommand ejbCommand, ComponentDTO dto, String key) {
        if (!ejbCommand.getParamDTO().containsKey(key)) {
            return;
        }

        Integer value = null;
        if (null != ejbCommand.getParamDTO().get(key) &&
                !"".equals(ejbCommand.getParamDTO().get(key).toString().trim())) {
            try {
                value = Integer.valueOf(ejbCommand.getParamDTO().get(key).toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse " + key + "=" + ejbCommand.getParamDTO().get(key) + " FAIL");
                return;
            }
        }
        dto.put(key, value);
    }

    public void setValueAsBigDecimal(EJBCommand ejbCommand, ComponentDTO dto, String key) {
        if (!ejbCommand.getParamDTO().containsKey(key)) {
            return;
        }

        BigDecimal value = null;
        if (null != ejbCommand.getParamDTO().get(key) &&
                !"".equals(ejbCommand.getParamDTO().get(key).toString().trim())) {
            try {
                value = new BigDecimal(ejbCommand.getParamDTO().get(key).toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse " + key + "=" + ejbCommand.getParamDTO().get(key) + " FAIL");
                return;
            }
        }
        dto.put(key, value);
    }

    public Boolean getValueAsBoolean(EJBCommand ejbCommand, String key) {
        Boolean value = null;
        if (null != ejbCommand.getParamDTO().get(key) &&
                !"".equals(ejbCommand.getParamDTO().get(key).toString().trim())) {
            String object = ejbCommand.getParamDTO().get(key).toString().trim().toLowerCase();
            if ("true".equals(object)) {
                value = true;
            }
            if ("false".equals(object)) {
                value = false;
            }
        }

        return value;
    }

    public Long getValueAsLong(EJBCommand ejbCommand, String key) {
        Long value = null;
        if (null != ejbCommand.getParamDTO().get(key) &&
                !"".equals(ejbCommand.getParamDTO().get(key).toString().trim())) {
            try {
                value = Long.valueOf(ejbCommand.getParamDTO().get(key).toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse " + key + "=" + ejbCommand.getParamDTO().get(key) + " FAIL");
            }
        }

        return value;
    }

    public void processCmdResultDTOErrorMessages(ResultDTO resultDTO, ResultDTO cmdResultDTO) {
        if (!"Success".equals(cmdResultDTO.getForward()) && cmdResultDTO.hasResultMessage()) {
            for (Iterator iterator = cmdResultDTO.getResultMessages(); iterator.hasNext();) {
                ResultMessage message = (ResultMessage) iterator.next();
                resultDTO.addResultMessage(message);
            }
        }
    }

}
