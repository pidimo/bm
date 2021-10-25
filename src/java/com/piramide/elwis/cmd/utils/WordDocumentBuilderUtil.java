package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.exception.CreateDocumentException;
import com.piramide.elwis.service.campaign.DocumentGenerateService;
import com.piramide.elwis.service.campaign.DocumentGenerateServiceHome;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.CreateException;
import java.util.List;

/**
 * Jatun S.R.L.
 * class to help in the word document generation
 *
 * @author Miky
 * @version $Id: WordDocumentBuilderUtil.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class WordDocumentBuilderUtil {
    private DocumentGenerateService documentGenerateService;

    /**
     * initialize service to generate document
     *
     * @throws CreateException
     */
    public WordDocumentBuilderUtil() throws CreateException {
        DocumentGenerateServiceHome documentGenerateServiceHome = (DocumentGenerateServiceHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_DOCUMENT_GENERATESERVICE);
        documentGenerateService = documentGenerateServiceHome.create();
    }

    /**
     * campaign document generation
     *
     * @param fieldValuesList list of values for template variables
     * @param fieldNames      variables of document template
     * @param template        campaign document template
     * @return byte[] document generated
     * @throws CreateDocumentException
     */
    public byte[] renderCampaignDocument(List fieldValuesList, String[] fieldNames, byte[] template) throws CreateDocumentException {
        return documentGenerateService.renderDocument(fieldValuesList, fieldNames, template);
    }
}
