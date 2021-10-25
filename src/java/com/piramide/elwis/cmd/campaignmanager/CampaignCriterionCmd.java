package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.catalogmanager.Category;
import com.piramide.elwis.domain.catalogmanager.CategoryHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.dto.campaignmanager.CampaignCriterionDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.alfacentauro.fantabulous.controller.SearchParameter;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignCriterionCmd.java 10491 2014-09-17 21:56:37Z miguel $
 */

public class CampaignCriterionCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());
    public String IU_TYPE = (String) paramDTO.get("IU_Type");

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignCriterionCmd             .....");


        if (CRUDDirector.OP_CREATE.equals(paramDTO.get("op"))) {
            createData();
        } else if (CRUDDirector.OP_UPDATE.equals(paramDTO.getOp())) {
            try {
                updateData();
            } catch (FinderException e) {
                log.debug("FinderException on CampaignCriterionCmd");
            }
        } else if (CampaignConstants.EMPTY.equals(paramDTO.getOp())) {
            try {
                readData();
            } catch (FinderException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else if (CRUDDirector.OP_DELETE.equals(paramDTO.getOp())) {
            String id_ = paramDTO.get("campaignCriterionId").toString().trim();
            deleteData(new Integer(id_));
        }
        return;
    }

    private void readData() throws FinderException {
        log.debug(" ... readDate function execute ...");
        CampaignCriterion campCriterion = (CampaignCriterion) ExtendedCRUDDirector.i.read(new CampaignCriterionDTO(paramDTO), resultDTO, false);
        CampaignCriterionValueHome criterionHome = (CampaignCriterionValueHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCRITERIONVALUE);
        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        String IU = null;
        String ids = "";
        Integer fieldType = null;
        String fieldName = null;
        Object value1 = null, value2 = null;
        String operator = null;
        if (campCriterion != null) {
            if (CampaignConstants.COMPARATOR_BETWEEN.equals(campCriterion.getOperator())) {
                ids = new String(campCriterion.getCampaignFreeText().getValue());
                StringTokenizer st = new StringTokenizer(ids, ",");
                value1 = st.nextToken();
                value2 = st.nextToken();
            }

            if (campCriterion.getCampCriterionValueId() != null) {
                CampaignCriterionValue criterionValue = criterionHome.findByPrimaryKey(campCriterion.getCampCriterionValueId());
                fieldType = criterionValue.getFieldType();
                resultDTO.put("tableId", criterionValue.getTableId());
                resultDTO.put("description", criterionValue.getDescriptionKey());
                fieldName = criterionValue.getFieldName();
            } else {
                Category category = categoryHome.findByPrimaryKey(campCriterion.getCategoryId());
                fieldType = category.getCategoryType();
            }
            if (new Integer(18).equals(campCriterion.getCampCriterionValueId())) {
                AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
                Address partner = addressHome.findByPrimaryKey(new Integer(new String(campCriterion.getCampaignFreeText().getValue())));
                if (partner != null) {
                    StringBuffer addressName = new StringBuffer();
                    addressName.append(partner.getName1());
                    if (partner.getName2() != null && !"".equals(partner.getName2())) {
                        if ("1".equals(partner.getAddressType())) {
                            addressName.append(", ");
                        } else {
                            addressName.append(" ");
                        }
                        addressName.append(partner.getName2());
                    }
                    resultDTO.put("partnerId", partner.getAddressId());
                    resultDTO.put("partnerName", addressName.toString());
                }
                operator = "operator_text";
                IU = "11";


            } else if (new Integer(26).equals(campCriterion.getCampCriterionValueId())) {
                ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
                Product product = productHome.findByPrimaryKey(new Integer(new String(campCriterion.getCampaignFreeText().getValue())));
                if (product != null) {
                    resultDTO.put("productId", product.getProductId());
                    resultDTO.put("productName", product.getProductName());
                }
                operator = "operator_text";
                IU = "14";
            } else if ("ANDBIT".equals(campCriterion.getOperator())) {
                IU = "12";
                operator = "operator_contactType";
                resultDTO.put("code", new String(campCriterion.getCampaignFreeText().getValue()));
            } else if ((new Integer(23).equals(campCriterion.getCampCriterionValueId()))) {
                IU = "13";
                operator = "operator_productInUse";
                resultDTO.put("inUse", new String(campCriterion.getCampaignFreeText().getValue()));
            } else if (new Integer(1).equals(fieldType) && CampaignConstants.COMPARATOR_BETWEEN.equals(campCriterion.getOperator())) {
                IU = "5";
                resultDTO.put("numberValue_1", value1);
                resultDTO.put("numberValue_2", value2);
                operator = "operator_number";
            } else if (new Integer(2).equals(fieldType) && CampaignConstants.COMPARATOR_BETWEEN.equals(campCriterion.getOperator())) {
                IU = "6";
                resultDTO.put("decimalNumberValue_1", value1);
                resultDTO.put("decimalNumberValue_2", value2);
                operator = "operator_decimal";
            } else if (new Integer(3).equals(fieldType) && CampaignConstants.COMPARATOR_BETWEEN.equals(campCriterion.getOperator())) {
                IU = "7";
                resultDTO.put("value1", new Integer(value1.toString()));
                resultDTO.put("value2", new Integer(value2.toString()));
                operator = "operator_date";
            } else if (new Integer(1).equals(fieldType)) {
                IU = "1";
                resultDTO.put("number_value", new String(campCriterion.getCampaignFreeText().getValue()));
                operator = "operator_number";
            } else if (new Integer(2).equals(fieldType)) {
                IU = "2";
                resultDTO.put("decimalNumber_value", new String(campCriterion.getCampaignFreeText().getValue()));
                operator = "operator_decimal";
            } else if (new Integer(3).equals(fieldType)) {
                IU = "3";
                resultDTO.put("value", new String(campCriterion.getCampaignFreeText().getValue()));
                operator = "operator_date";
            } else if ((new Integer(6).equals(fieldType) || new Integer(5).equals(fieldType)
                    || (new Integer(4).equals(fieldType) && (!"0".equals(fieldName)) && campCriterion.getCategoryId() == null))
                    && !new Integer(18).equals(campCriterion.getCampCriterionValueId()) && !new Integer(13).equals(campCriterion.getCampCriterionValueId())) {
                IU = "9";
                operator = "operator_multiple";
            } else if (new Integer(4).equals(fieldType)) {
                IU = "4";
                resultDTO.put("text_value", new String(campCriterion.getCampaignFreeText().getValue()));
                operator = "operator_text";
            } else if (new Integer(CampaignConstants.FIELD_RELATION_EXISTS).equals(fieldType)) {
                IU = "15";
                operator = "operator_relationExists";
            }

            resultDTO.put(operator, campCriterion.getOperator());
            resultDTO.put("fieldname", fieldName);
            resultDTO.put("fieldType", fieldType);
            resultDTO.put("IU_Type", IU);
            resultDTO.put("freeTextValue", new String(campCriterion.getCampaignFreeText().getValue()));

        } else {
            resultDTO.setResultAsFailure();
        }
    }

    private void createData() {

        log.debug("... execute createData function .... ");
        CampaignFreeText freeText = null;
        CampaignCriterion campaignCriterion = null;
        CampaignCriterionDTO campaignCriterionDTO = new CampaignCriterionDTO(paramDTO);
        Category category = null;
        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CATEGORY);
        if ("-100".equals(paramDTO.get("categoryId"))) {
            campaignCriterionDTO.put("categoryId", null);
        } else if ("-100".equals(paramDTO.get("campCriterionValueId"))) {
            campaignCriterionDTO.put("campCriterionValueId", null);
            try {
                category = categoryHome.findByPrimaryKey(new Integer(campaignCriterionDTO.get("categoryId").toString()));
            } catch (FinderException e) {
                resultDTO.setResultAsFailure();
                resultDTO.put("categoryNULL", CampaignConstants.TRUEVALUE);
                log.debug(" ... category not found ...   ");
                return;
            }
        }
        if (("6".equals(paramDTO.get("fieldType")) || "5".equals(paramDTO.get("fieldType"))
                || (("4".equals(paramDTO.get("fieldType")) && !CampaignConstants.COMPARATOR_DISTINCT.equals(paramDTO.get("operator"))) && "13".equals(paramDTO.get("campCriterionValueId")))
                || (("4".equals(paramDTO.get("fieldType")) && CampaignConstants.COMPARATOR_DISTINCT.equals(paramDTO.get("operator"))) && "12".equals(paramDTO.get("campCriterionValueId")))
                || (("4".equals(paramDTO.get("fieldType")) && ("12".equals(paramDTO.get("campCriterionValueId")) || "16".equals(paramDTO.get("campCriterionValueId")))))
        )
                && ((CampaignConstants.COMPARATOR_DISTINCT.equals(paramDTO.get("operator"))
                || CampaignConstants.COMPARATOR_NOTIN.equals(paramDTO.get("operator"))) || CampaignConstants.COMPARATOR_IN.equals(paramDTO.get("operator")))
                && !((Boolean) paramDTO.get("isPopUp")).booleanValue()) {
            List values = (List) paramDTO.get("multipleSelectValue");
            StringBuffer stringBuffer = new StringBuffer();

            for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                stringBuffer.append(iterator.next());
                if (iterator.hasNext()) {
                    stringBuffer.append(",");
                }
            }
            paramDTO.put("fieldValue", stringBuffer.toString());
        }

        if ("code".equals(paramDTO.get("fieldName"))) {
            paramDTO.put("fieldValue", paramDTO.get("code"));
        } else if ("inuse".equals(paramDTO.get("fieldName"))) {
            paramDTO.put("fieldValue", paramDTO.get("inUse"));
        }

        campaignCriterion = (CampaignCriterion) CRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, campaignCriterionDTO, resultDTO);
        if ("code".equals(paramDTO.get("fieldName"))) {
            campaignCriterion.setOperator("ANDBIT");
        } else if ("CONTAIN".equals(paramDTO.get("operator")) && ("18".equals(paramDTO.get("campCriterionValueId"))
                || "26".equals(paramDTO.get("campCriterionValueId")))) {
            campaignCriterion.setOperator("EQUAL");
        }
        CampaignFreeTextHome freeTextHome = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
        try {
            freeText = freeTextHome.create(paramDTO.get("fieldValue").toString().getBytes(), campaignCriterion.getCompanyId(),
                    new Integer(FreeTextTypes.FREETEXT_CAMPAIGN_CRITERION));
        } catch (CreateException e) {
            log.debug(".. error create campaignCriterion ..");
        }

        campaignCriterion.setValueId(freeText.getFreeTextId());
        ListStructure structure = (ListStructure) paramDTO.get("greatQuery");
        List criterions = new ArrayList();
        criterions.add(campaignCriterion);
        ArrayList parameterList = new ArrayList();
        parameterList.add(new SearchParameter("companyId", campaignCriterion.getCompanyId().toString()));
        parameterList.add(new SearchParameter("userId", paramDTO.get("userId").toString()));

        CampaignCriterionExecute execute = new CampaignCriterionExecute();
        int numberHitsSize = 0;
        try {
            numberHitsSize = execute.executeCriterias(structure, new ArrayList(criterions), parameterList, false,
                    (ListStructure) paramDTO.get("productCategoriesGreatQuery"),
                    (ListStructure) paramDTO.get("customerCategoriesGreatQuery"),
                    (ListStructure) paramDTO.get("addressCategoriesGreatQuery"),
                    (ListStructure) paramDTO.get("contactPersonCategoriesGreatQuery"),
                    (ListStructure) paramDTO.get("salePositionCategoriesGreatQuery")).size();
        } catch (FinderException e) {
            log.debug(" ... not found ....");
        }
        campaignCriterion.setNumberHits(new Integer(numberHitsSize));
    }


    private void updateData() throws FinderException {
        log.debug(" .... updateData function .... ");
        CampaignCriterionDTO campaignCriterionDTO = new CampaignCriterionDTO();
        CampaignCriterion criterion = null;
        CampaignFreeText freeText = null;
        String id_ = paramDTO.get("campaignCriterionId").toString().trim();

        campaignCriterionDTO.put("companyId", paramDTO.get("companyId"));
        campaignCriterionDTO.put("campaignCriterionId", new Integer(id_));
        campaignCriterionDTO.put("campaignId", paramDTO.get("campaignId"));
        campaignCriterionDTO.put("version", paramDTO.get("version"));
        campaignCriterionDTO.put("operator", paramDTO.get("operator"));

        if ("code".equals(paramDTO.get("fieldName"))) {
            paramDTO.put("fieldValue", paramDTO.get("code"));
        }
        criterion = (CampaignCriterion) ExtendedCRUDDirector.i.update(campaignCriterionDTO, resultDTO, false, true, false, "Fail");
        if (criterion != null && !resultDTO.isFailure()) {

            CampaignFreeTextHome freeTextHome = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
            try {
                freeText = freeTextHome.findByPrimaryKey(criterion.getValueId());
            } catch (FinderException e) {
                log.debug(" ... freeText not found .....");
            }

            if (("6".equals(paramDTO.get("fieldType")) || "5".equals(paramDTO.get("fieldType"))
                    || (("4".equals(paramDTO.get("fieldType")) && !"DISTINCT".equals(paramDTO.get("operator"))) && "13".equals(paramDTO.get("campCriterionValueId")))
                    || (("4".equals(paramDTO.get("fieldType")) && "DISTINCT".equals(paramDTO.get("operator"))) && "12".equals(paramDTO.get("campCriterionValueId")))
                    || (("4".equals(paramDTO.get("fieldType")) && ("12".equals(paramDTO.get("campCriterionValueId")) || "16".equals(paramDTO.get("campCriterionValueId"))))))
                    && ((CampaignConstants.COMPARATOR_DISTINCT.equals(paramDTO.get("operator")) || CampaignConstants.COMPARATOR_NOTIN.equals(paramDTO.get("operator"))) || CampaignConstants.COMPARATOR_IN.equals(paramDTO.get("operator")))
                    && !((Boolean) paramDTO.get("isPopUp")).booleanValue()) {

                List values = (List) paramDTO.get("multipleSelectValue");
                StringBuffer stringBuffer = new StringBuffer();
                if (values != null) {
                    for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                        Object aux = iterator.next();
                        String id = aux.toString();
                        stringBuffer.append(id);
                        if (iterator.hasNext()) {
                            stringBuffer.append(",");
                        }
                    }
                    freeText.setValue(stringBuffer.toString().getBytes());
                }
            } else {
                criterion.getCampaignFreeText().setValue(paramDTO.get("fieldValue").toString().getBytes());
            }
            ListStructure structure = (ListStructure) paramDTO.get("greatQuery");
            List criterions = new ArrayList();

            criterions.add(criterion);
            ArrayList parameterList = new ArrayList();
            parameterList.add(new SearchParameter("companyId", criterion.getCompanyId().toString()));
            parameterList.add(new SearchParameter("userId", (paramDTO.get("userId")).toString()));
            if ("CONTAIN".equals(paramDTO.get("operator")) && ("18".equals(paramDTO.get("campCriterionValueId"))
                    || "26".equals(paramDTO.get("campCriterionValueId")))) {
                criterion.setOperator("EQUAL");
            }

            CampaignCriterionExecute execute = new CampaignCriterionExecute();
            int numberHitsSize = execute.executeCriterias(structure, new ArrayList(criterions), parameterList, false,
                    (ListStructure) paramDTO.get("productCategoriesGreatQuery"),
                    (ListStructure) paramDTO.get("customerCategoriesGreatQuery"),
                    (ListStructure) paramDTO.get("addressCategoriesGreatQuery"),
                    (ListStructure) paramDTO.get("contactPersonCategoriesGreatQuery"),
                    (ListStructure) paramDTO.get("salePositionCategoriesGreatQuery")).size();
            criterion.setNumberHits(new Integer(numberHitsSize));
            resultDTO.put("campaignId", criterion.getCampaignId());

        } else if (resultDTO.isFailure()) {

            resultDTO.put("IU_Type", paramDTO.get("IU_Type"));
            resultDTO.put("versionError", CampaignConstants.TRUEVALUE);
            resultDTO.put("fieldType", paramDTO.get("fieldType"));
            resultDTO.put("table", paramDTO.get("table"));
            resultDTO.put("fieldname", paramDTO.get("fieldName_"));
            readData();
        }
    }

    private void deleteData(Integer id) {
        log.debug("...  execute deleteData function ... ");
        CampaignCriterionDTO dto = new CampaignCriterionDTO(paramDTO);

        dto.put("campaignCriterionId", new Integer(id));
        IntegrityReferentialChecker.i.check(dto, resultDTO);

        if (resultDTO.isFailure()) {
            return;
        }

        CRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, new CampaignCriterionDTO(paramDTO), resultDTO);
        resultDTO.put("campaignId", paramDTO.get("campaignId"));

        if (resultDTO.isFailure()) {
            resultDTO.setForward("Fail");
            return;
        }
        resultDTO.setForward("Success");
        return;
    }

    public boolean isStateful() {
        return false;
    }
}