package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.VariableConstants;
import com.piramide.elwis.domain.catalogmanager.WebDocument;
import com.piramide.elwis.domain.catalogmanager.WebParameter;
import com.piramide.elwis.domain.catalogmanager.WebParameterHome;
import com.piramide.elwis.dto.catalogmanager.WebDocumentDTO;
import com.piramide.elwis.dto.catalogmanager.WebParameterDTO;
import com.piramide.elwis.dto.catalogmanager.WebParameterWrapperDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class WebDocumentCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void executeInStateless(SessionContext context) {
        log.debug("Executing WebDocumentCmd................" + paramDTO);

        boolean isRead = true;
        WebDocumentDTO webDocumentDTO = getWebDocumentDTO();

        if ("create".equals(getOp())) {
            isRead = false;
            create(webDocumentDTO, context);
        }

        if ("update".equals(getOp())) {
            isRead = false;
            update(webDocumentDTO, context);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(webDocumentDTO);
        }

        if (isRead) {
            boolean withReferences = null != paramDTO.get("withReferences")
                    && "true".equals(paramDTO.get("withReferences").toString());
            read(webDocumentDTO, withReferences);
        }
    }

    private WebDocumentDTO getWebDocumentDTO() {
        WebDocumentDTO webDocumentDTO = new WebDocumentDTO(paramDTO);
        EJBCommandUtil.i.setValueAsInteger(this, webDocumentDTO, "webDocumentId");
        return webDocumentDTO;
    }

    private void create(WebDocumentDTO webDocumentDTO, SessionContext ctx) {
        WebDocument webDocument = (WebDocument) ExtendedCRUDDirector.i.create(webDocumentDTO, resultDTO, false);

        if (!resultDTO.isFailure() && webDocument != null) {
            createOrUpdateWebParameter(webDocument, ctx);
        }
    }

    private void update(WebDocumentDTO webDocumentDTO, SessionContext ctx) {
        WebDocument webDocument = (WebDocument) ExtendedCRUDDirector.i.update(webDocumentDTO, resultDTO, false, true, true, "Fail");

        if (webDocument != null) {
            if (!resultDTO.isFailure()) {
                createOrUpdateWebParameter(webDocument, ctx);
            }

            //read parameters
            readWebParameters(webDocument);
        }
    }

    private void delete(WebDocumentDTO webDocumentDTO) {
        ExtendedCRUDDirector.i.delete(webDocumentDTO, resultDTO, true, "Fail");
    }

    private void read(WebDocumentDTO webDocumentDTO, boolean checkReferences) {
        WebDocument webDocument = (WebDocument) ExtendedCRUDDirector.i.read(webDocumentDTO, resultDTO, checkReferences);
        readWebParameters(webDocument);
    }

    private void createOrUpdateWebParameter(WebDocument webDocument, SessionContext ctx) {
        WebParameterWrapperDTO wrapperDTO = (WebParameterWrapperDTO) paramDTO.get("webParameterWrapper");

        WebParameterHome webParameterHome = (WebParameterHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_WEBPARAMETER);
        List<Integer> updatedIdList = new ArrayList<Integer>();
        List<Integer> currentWebParameterIds = null;
        try {
            currentWebParameterIds = (List<Integer>) webParameterHome.selectWebParameterIdsByWebDocument(webDocument.getWebDocumentId());
        } catch (FinderException e) {
            currentWebParameterIds = new ArrayList<Integer>();
        }

        for (int i = 0; i < VariableConstants.VariableType.values().length; i++) {
            VariableConstants.VariableType variableType = VariableConstants.VariableType.values()[i];

            List<WebParameterDTO> updateWebParameterDTOList = wrapperDTO.getWebParameterListByType(variableType);
            List<WebParameterDTO> createWebParameterDTOList = wrapperDTO.getNewsWebParameterListByType(variableType);

            updatedIdList.addAll(updateWebParameters(updateWebParameterDTOList, webDocument));
            createWebParameters(createWebParameterDTOList, webDocument, ctx);
        }

        cleanRemovedWebParameteres(currentWebParameterIds, updatedIdList);
    }

    private void createWebParameters(List<WebParameterDTO> createWebParameterDTOList, WebDocument webDocument, SessionContext ctx) {
        for (WebParameterDTO webParameterDTO : createWebParameterDTOList) {
            webParameterDTO = complementWebParameterDTO(webParameterDTO, webDocument);

            if (webParameterDTO != null) {
                createWebParameter(webParameterDTO, ctx);
            }
        }
    }

    private void createWebParameter(WebParameterDTO webParameterDTO, SessionContext ctx) {
        WebParameter webParameter = (WebParameter) ExtendedCRUDDirector.i.create(webParameterDTO, resultDTO, false);
    }

    private List<Integer> updateWebParameters(List<WebParameterDTO> updateWebParameterDTOList, WebDocument webDocument) {
        List<Integer> updatedIdList = new ArrayList<Integer>();

        for (WebParameterDTO webParameterDTO : updateWebParameterDTOList) {
            webParameterDTO = complementWebParameterDTO(webParameterDTO, webDocument);
            if (webParameterDTO != null) {
                WebParameter webParameter = updateWebParameter(webParameterDTO);
                if (webParameter != null) {
                    updatedIdList.add(webParameter.getWebParameterId());
                }
            }
        }

        return updatedIdList;
    }

    private WebParameter updateWebParameter(WebParameterDTO webParameterDTO) {
        WebParameter webParameter = (WebParameter) ExtendedCRUDDirector.i.update(webParameterDTO, resultDTO, false, false, true, "Fail");
        return webParameter;
    }

    private void cleanRemovedWebParameteres(List<Integer> currentWebParameterIds, List<Integer> updatedIdList) {

        for (Integer currentWebParameterId : currentWebParameterIds) {
            if (!updatedIdList.contains(currentWebParameterId)) {
                //if no contain, remove this
                WebParameter webParameter = findWebParameter(currentWebParameterId);
                if (webParameter != null) {
                    try {
                        webParameter.remove();
                    } catch (RemoveException e) {
                        log.warn("Web parameter cannot remove... " + webParameter.getWebParameterId(), e);
                    }
                }
            }
        }
    }

    private WebParameterDTO complementWebParameterDTO(WebParameterDTO webParameterDTO, WebDocument webDocument) {
        WebParameterDTO newWebParameterDTO = null;

        String parameterName = (String) webParameterDTO.get("parameterName");
        String variableName = (String) webParameterDTO.get("variableName");

        if (parameterName != null && !parameterName.trim().equals("") && variableName != null && !variableName.trim().equals("")) {

            newWebParameterDTO = new WebParameterDTO(webParameterDTO);
            newWebParameterDTO.put("webDocumentId", webDocument.getWebDocumentId());
            newWebParameterDTO.put("companyId", webDocument.getCompanyId());
            newWebParameterDTO.put("version", paramDTO.get("version"));
        }

        return newWebParameterDTO;
    }

    private void readWebParameters(WebDocument webDocument) {
        WebParameterWrapperDTO wrapperDTO = new WebParameterWrapperDTO();

        if (webDocument != null) {
            Iterator iteratorWebParameters = webDocument.getWebParameters().iterator();
            while (iteratorWebParameters.hasNext()) {
                WebParameter webParameter = (WebParameter) iteratorWebParameters.next();
                WebParameterDTO webParameterDTO = new WebParameterDTO();

                DTOFactory.i.copyToDTO(webParameter, webParameterDTO);
                wrapperDTO.addWebParameterDTO(webParameterDTO);
            }
        }

        resultDTO.put("webParameterWrapper", wrapperDTO);
    }

    private WebParameter findWebParameter(Integer webParameterId) {
        WebParameterHome webParameterHome = (WebParameterHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_WEBPARAMETER);
        if (webParameterId != null) {
            try {
                return webParameterHome.findByPrimaryKey(webParameterId);
            } catch (FinderException e) {
                log.debug("Error in find web parameter: " + webParameterId, e);
            }
        }
        return null;
    }


    public boolean isStateful() {
        return false;
    }
}
