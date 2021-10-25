package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.admin.ApplicationMailSignature;
import com.piramide.elwis.domain.admin.ApplicationMailSignatureHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.SystemLanguage;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Map;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ApplicationSignatureCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ApplicationSignatureCmd.class);

    @Override
    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        if ("update".equals(getOp())) {
            isRead = false;
            update();
        }

        if (isRead) {
            read();
        }
    }

    private void read() {
        Map systemLanguages = SystemLanguage.systemLanguages;

        boolean enabled = false;

        for (Object object : systemLanguages.entrySet()) {
            Map.Entry mapElement = (Map.Entry) object;
            String key = (String) mapElement.getKey();

            String textSignature = "";
            String htmlSignature = "";


            ApplicationMailSignature signature = getApplicationMailSignature(key);
            if (null != signature) {
                if (null != signature.getTextSignature()) {
                    textSignature = new String(signature.getTextSignature());
                }
                if (null != signature.getHtmlSignature()) {
                    htmlSignature = new String(signature.getHtmlSignature());
                }

                if (null != signature.getEnabled()) {
                    enabled = signature.getEnabled();
                }
            }

            resultDTO.put(getTextDTOKey(key), textSignature);
            resultDTO.put(getHtmlDTOKey(key), htmlSignature);
        }

        resultDTO.put("systemLanguages", systemLanguages);
        resultDTO.put("enabled", enabled);
    }

    private void update() {
        Boolean enabled = EJBCommandUtil.i.getValueAsBoolean(this, "enabled");

        Map systemLanguages = SystemLanguage.systemLanguages;
        for (Object object : systemLanguages.entrySet()) {
            Map.Entry mapElement = (Map.Entry) object;
            String key = (String) mapElement.getKey();
            String uiTextSignature = (String) paramDTO.get(getTextDTOKey(key));
            String uiHtmlSignature = (String) paramDTO.get(getHtmlDTOKey(key));

            ApplicationMailSignature signature = getApplicationMailSignature(key);

            if (null != signature) {
                updateApplicationMailSignature(uiTextSignature, uiHtmlSignature, enabled, signature);
                continue;
            }

            createApplicationMailSignature(key, uiTextSignature, uiHtmlSignature, enabled);
        }
    }

    private void createApplicationMailSignature(String key,
                                                String uiTextSignature,
                                                String uiHtmlSignature,
                                                Boolean enabled) {
        if ("".equals(uiTextSignature.trim()) && "".equals(uiHtmlSignature.trim())) {
            return;
        }

        byte[] textSignature = null;
        if (!"".equals(uiTextSignature.trim())) {
            textSignature = uiTextSignature.getBytes();
        }

        byte[] htmlSignature = null;
        if (!"".equals(uiHtmlSignature.trim())) {
            htmlSignature = uiHtmlSignature.getBytes();
        }

        ApplicationMailSignatureHome applicationMailSignatureHome =
                (ApplicationMailSignatureHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_APPLICATIONMAILSIGNATURE);
        try {
            ApplicationMailSignature signature = applicationMailSignatureHome.create(key, htmlSignature, textSignature);
            signature.setEnabled(enabled);
        } catch (CreateException e) {
            log.error("-> Create ApplicationMailSignature FAIL", e);
        }
    }

    private void updateApplicationMailSignature(String uiTextSignature,
                                                String uiHtmlSignature,
                                                Boolean enabled,
                                                ApplicationMailSignature signature) {
        if ("".equals(uiTextSignature.trim()) && "".equals(uiHtmlSignature.trim())) {
            try {
                signature.remove();
            } catch (RemoveException e) {
                log.error("-> Remove ApplicationMailSignature FAIL", e);
            }
            return;
        }

        signature.setEnabled(enabled);

        if ("".equals(uiTextSignature.trim())) {
            signature.setTextSignature(null);
        } else {
            signature.setTextSignature(uiTextSignature.getBytes());
        }

        if ("".equals(uiHtmlSignature.trim())) {
            signature.setHtmlSignature(null);
        } else {
            signature.setHtmlSignature(uiHtmlSignature.getBytes());
        }
    }

    private String getTextDTOKey(String key) {
        return "text_" + key;
    }

    private String getHtmlDTOKey(String key) {
        return "html_" + key;
    }

    private ApplicationMailSignature getApplicationMailSignature(String key) {
        ApplicationMailSignatureHome applicationMailSignatureHome =
                (ApplicationMailSignatureHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_APPLICATIONMAILSIGNATURE);
        try {
            return applicationMailSignatureHome.findByPrimaryKey(key);
        } catch (FinderException e) {
            return null;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
