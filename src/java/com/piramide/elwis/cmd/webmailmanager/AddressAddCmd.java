package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.cmd.contactmanager.AddressCreateCmd;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Alfacentauro Team
 * <p/>
 * this class help to add new address of form fast, only whit some fields
 *
 * @author miky
 * @version $Id: AddressAddCmd.java 10377 2013-09-27 15:59:03Z miguel $
 */

public class AddressAddCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing cmd AddressAddCmd ........" + getParamDTO());


        Language language = null;
        LanguageHome home = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
        try {
            language = home.findByDefault(new Integer(paramDTO.get("companyId").toString()));
        } catch (FinderException e) {
            log.debug(" language not found .......");
        }

        if ("create".equals(getOp())) {
            if (paramDTO.get("cancel") != null) {
                resultDTO.setForward("Cancel");
                return;
            } else {
                createAddressAndTelecom(ctx, language);
            }
        }
        if ("createMany".equals(getOp())) {
            createManyAddressAndTelecom(ctx, language);
        }
    }

    /**
     * create or update one (person,organization) and yuor telecoms
     *
     * @param ctx
     */

    private void createAddressAndTelecom(SessionContext ctx, Language language) {
        log.debug("Executing method createAddressAndTelecom ........");

        //paramDTO.put("languageId",language.getLanguageId());

        //variable to contact group
        String mailGroupAddrId = paramDTO.get("mailGroupAddrId").toString();
        Integer addressId = null;
        Integer contactPersonId = null;
        Map resultTelecomMap = null;
        TelecomDTO telecomDTO = null;
        String telecomId = null;

        //create map of telecom
        Integer telecomTypeId = new Integer(paramDTO.get("telecomTypeId").toString());
        String telecomData = paramDTO.get("email").toString();

        TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
        cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
        cmd.putParam("companyId", paramDTO.get("companyId"));

        if (language != null) {
            cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, language.getLanguageIso());
        } else {
            cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, "en");
        }

        cmd.putParam("telecomTypeId", telecomTypeId);
        cmd.executeInStateless(ctx);
        ResultDTO resultDto = cmd.getResultDTO();
        TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) resultDto.get(TelecomTypeSelectCmd.RESULT);
        telecomDTO = new TelecomDTO(telecomData, null, true);
        Map telecomMap = new LinkedHashMap();
        telecomMap.put(telecomTypeDTO.get("telecomTypeId").toString(), new TelecomWrapperDTO(telecomDTO, telecomTypeDTO));

        //add telecom to address
        if (paramDTO.get("isUpdate") != null) {

            if (paramDTO.get("addressId") != null && !"".equals(paramDTO.get("addressId").toString())) {

                addressId = Integer.valueOf(paramDTO.get("addressId").toString());
                Address address = (Address) EJBFactory.i.callFinder(new AddressDTO(), "findByPrimaryKey", new Object[]{addressId});
                TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

                if ((paramDTO.get("contactPersonId") != null) && !"".equals(paramDTO.get("contactPersonId").toString())) {
                    contactPersonId = Integer.valueOf(paramDTO.get("contactPersonId").toString());
                }

                //verif if have email address
                boolean telecomPredetermined = true;
                if (contactPersonId != null) {
                    try {
                        Collection telecoms = telecomHome.findContactPersonTelecomsByTelecomTypeType(addressId, contactPersonId, TelecomType.EMAIL_TYPE);
                        if (telecoms.size() > 0) {
                            telecomPredetermined = false;
                        }
                    } catch (FinderException fe) {
                    }
                } else {
                    try {
                        Collection telecoms = telecomHome.findAddressTelecomsByTelecomTypeType(addressId, TelecomType.EMAIL_TYPE); //find for addressId
                        if (telecoms.size() > 0) {
                            telecomPredetermined = false;
                        }
                    } catch (FinderException fe) {
                    }
                }

                try {

                    Telecom telecom = telecomHome.create(address.getAddressId(), contactPersonId, telecomDTO.getData(),
                            telecomDTO.getDescription(), new Boolean(telecomPredetermined),
                            telecomTypeId, address.getCompanyId());
                    address.getTelecoms().add(telecom);
                    telecomDTO.setTelecomId(telecom.getTelecomId().toString());
                } catch (CreateException e) {
                    log.error("Unexpected error creating telecoms", e);
                }

            }

        } else { //create address and telecom

            String addressType = paramDTO.get("addressType").toString();
            if (addressType.equals(ContactConstants.ADDRESSTYPE_PERSON)) {

                byte code = CodeUtil.default_;   // default code

                AddressCreateCmd addressCreateCmd = new AddressCreateCmd();
                addressCreateCmd.putParam("addressType", addressType);
                addressCreateCmd.putParam("name1", paramDTO.get("PerName1"));
                addressCreateCmd.putParam("name2", paramDTO.get("PerName2"));
                addressCreateCmd.putParam("name3", paramDTO.get("PerName3"));
                addressCreateCmd.putParam("searchName", paramDTO.get("PerSearchName"));
                addressCreateCmd.putParam("recordUserId", paramDTO.get("userMailId").toString());
                addressCreateCmd.putParam("companyId", paramDTO.get("companyId").toString());
                addressCreateCmd.putParam("code", new Byte(code).toString());
                addressCreateCmd.putParam("active", new Boolean(true));  // contact is active
                if (language != null) {
                    addressCreateCmd.putParam("languageId", language.getLanguageId());// add language by default,it is language of the company.
                }

                addressCreateCmd.putParam("telecomMap", telecomMap);

                addressCreateCmd.executeInStateless(ctx); //create the address
                ResultDTO resultDTOaddress = addressCreateCmd.getResultDTO();
                addressId = (Integer) resultDTOaddress.get("addressId");
                resultTelecomMap = (Map) resultDTOaddress.get("telecomMap");
            }
            if (addressType.equals(ContactConstants.ADDRESSTYPE_ORGANIZATION)) {

                byte code = CodeUtil.default_;   // default code

                AddressCreateCmd addressCreateCmd = new AddressCreateCmd();
                addressCreateCmd.putParam("addressType", addressType);
                addressCreateCmd.putParam("name1", paramDTO.get("OrgName1"));
                addressCreateCmd.putParam("name2", paramDTO.get("OrgName2"));
                addressCreateCmd.putParam("name3", paramDTO.get("OrgName3"));
                addressCreateCmd.putParam("searchName", paramDTO.get("OrgSearchName"));
                addressCreateCmd.putParam("recordUserId", paramDTO.get("userMailId").toString());
                addressCreateCmd.putParam("companyId", paramDTO.get("companyId").toString());
                addressCreateCmd.putParam("code", new Byte(code).toString());
                addressCreateCmd.putParam("active", new Boolean(true));  // contact is active
                if (language != null) {
                    addressCreateCmd.putParam("languageId", language.getLanguageId());// add language by default,it is language of the company.
                }


                addressCreateCmd.putParam("telecomMap", telecomMap);

                addressCreateCmd.executeInStateless(ctx); //create the address
                ResultDTO resultDTOaddress = addressCreateCmd.getResultDTO();
                addressId = (Integer) resultDTOaddress.get("addressId");
                resultTelecomMap = (Map) resultDTOaddress.get("telecomMap");
            }
        }

        //add to contact group
        if (resultTelecomMap != null && resultTelecomMap.size() > 0) {
            for (Iterator iterator = resultTelecomMap.values().iterator(); iterator.hasNext();) {
                TelecomWrapperDTO telecomWrapperDTO = (TelecomWrapperDTO) iterator.next();
                for (Iterator iterator1 = telecomWrapperDTO.getTelecoms().iterator(); iterator1.hasNext();) {
                    telecomDTO = (TelecomDTO) iterator1.next();
                    if (telecomDTO.getData().equals(telecomData)) {
                        telecomId = telecomDTO.getTelecomId();
                        break;
                    }
                }
            }
        }
        if (telecomId != null) {
            Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
            DTO dto = new DTO();
            dto.put("telecomId", telecomId);
            dto.put("addressId", addressId);
            dto.put("contactPersonAddressId", null);

            Collection c = new ArrayList();
            c.add(dto);
            AddressGroupCmd addressGroupCmd = new AddressGroupCmd();
            addressGroupCmd.putParam("op", "create");
            addressGroupCmd.putParam("selectedMails", c);
            addressGroupCmd.putParam("mailGroupAddrId", mailGroupAddrId);
            addressGroupCmd.putParam("companyId", companyId);

            addressGroupCmd.executeInStateless(ctx);
        }
    }

    /**
     * create or update many (person,organization) and yuor telecoms
     *
     * @param ctx
     */
    private void createManyAddressAndTelecom(SessionContext ctx, Language language) {
        log.debug("Executing method createManyAddressAndTelecom ........");

        List tempEmailIds = (List) paramDTO.get("tempEmailIds");

        if (tempEmailIds != null) {
            for (Iterator iterator = tempEmailIds.iterator(); iterator.hasNext();) {

                String identify = iterator.next().toString();

                String addressType = paramDTO.get("addressType_" + identify).toString();
                Object isUpdate = paramDTO.get("isUpdate_" + identify);
                if ((!addressType.equals(WebMailConstants.BLANK_KEY) && isUpdate == null) || isUpdate != null) {

                    //create map of telecom
                    Integer telecomTypeId = new Integer(paramDTO.get("telecomTypeId_" + identify).toString());
                    String telecomData = paramDTO.get("email_" + identify).toString();

                    TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
                    if (language != null) {
                        cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, language.getLanguageIso());
                    } else {
                        cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, "en");
                    }

                    cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
                    cmd.putParam("companyId", paramDTO.get("companyId"));
                    /*cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, "en");*/
                    cmd.putParam("telecomTypeId", telecomTypeId);

                    cmd.executeInStateless(ctx);
                    ResultDTO resultDto = cmd.getResultDTO();
                    TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) resultDto.get(TelecomTypeSelectCmd.RESULT);
                    TelecomDTO telecomDTO = new TelecomDTO(telecomData, null, true);

                    Map telecomMap = new LinkedHashMap();
                    telecomMap.put(telecomTypeDTO.get("telecomTypeId").toString(), new TelecomWrapperDTO(telecomDTO, telecomTypeDTO));

                    //add telecom to address
                    if (isUpdate != null) {

                        if (paramDTO.get("addressId_" + identify) != null && !"".equals(paramDTO.get("addressId_" + identify).toString())) {

                            Integer addressId = Integer.valueOf(paramDTO.get("addressId_" + identify).toString());
                            Address address = (Address) EJBFactory.i.callFinder(new AddressDTO(), "findByPrimaryKey", new Object[]{addressId});
                            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

                            Integer contactPersonId = null;
                            if ((paramDTO.get("contactPersonId_" + identify) != null) && !"".equals(paramDTO.get("contactPersonId_" + identify).toString())) {
                                contactPersonId = Integer.valueOf(paramDTO.get("contactPersonId_" + identify).toString());
                            }

                            //verif if have email address
                            boolean telecomPredetermined = true;
                            if (contactPersonId != null) {
                                try {
                                    Collection telecoms = telecomHome.findContactPersonTelecomsByTelecomTypeType(addressId, contactPersonId, TelecomType.EMAIL_TYPE);
                                    if (telecoms.size() > 0) {
                                        telecomPredetermined = false;
                                    }
                                } catch (FinderException fe) {
                                }
                            } else {
                                try {
                                    Collection telecoms = telecomHome.findAddressTelecomsByTelecomTypeType(addressId, TelecomType.EMAIL_TYPE); //find for addressId
                                    if (telecoms.size() > 0) {
                                        telecomPredetermined = false;
                                    }
                                } catch (FinderException fe) {
                                }
                            }

                            try {

                                Telecom telecom = telecomHome.create(address.getAddressId(), contactPersonId, telecomDTO.getData(),
                                        telecomDTO.getDescription(), new Boolean(telecomPredetermined),
                                        telecomTypeId, address.getCompanyId());
                                address.getTelecoms().add(telecom);
                                telecomDTO.setTelecomId(telecom.getTelecomId().toString());
                            } catch (CreateException e) {
                                log.error("Unexpected error creating telecoms", e);
                            }

                        }

                    } else { //create address and telecom


                        if (addressType.equals(ContactConstants.ADDRESSTYPE_PERSON)) {

                            byte code = CodeUtil.default_;   // default code

                            AddressCreateCmd addressCreateCmd = new AddressCreateCmd();
                            addressCreateCmd.putParam("addressType", addressType);
                            addressCreateCmd.putParam("name1", paramDTO.get("PerName1_" + identify));
                            addressCreateCmd.putParam("name2", paramDTO.get("PerName2_" + identify));
                            addressCreateCmd.putParam("name3", paramDTO.get("PerName3_" + identify));
                            addressCreateCmd.putParam("searchName", paramDTO.get("PerSearchName_" + identify));
                            addressCreateCmd.putParam("recordUserId", paramDTO.get("userMailId").toString());
                            addressCreateCmd.putParam("companyId", paramDTO.get("companyId").toString());
                            addressCreateCmd.putParam("code", new Byte(code).toString());
                            addressCreateCmd.putParam("active", new Boolean(true));  // contact is active
                            if (language != null) {
                                addressCreateCmd.putParam("languageId", language.getLanguageId());// add language by default,it is language of the company.
                            }


                            addressCreateCmd.putParam("telecomMap", telecomMap);
                            addressCreateCmd.executeInStateless(ctx); //create the address
                        }
                        if (addressType.equals(ContactConstants.ADDRESSTYPE_ORGANIZATION)) {

                            byte code = CodeUtil.default_;   // default code

                            AddressCreateCmd addressCreateCmd = new AddressCreateCmd();
                            addressCreateCmd.putParam("addressType", addressType);
                            addressCreateCmd.putParam("name1", paramDTO.get("OrgName1_" + identify));
                            addressCreateCmd.putParam("name2", paramDTO.get("OrgName2_" + identify));
                            addressCreateCmd.putParam("name3", paramDTO.get("OrgName3_" + identify));
                            addressCreateCmd.putParam("searchName", paramDTO.get("OrgSearchName_" + identify));
                            addressCreateCmd.putParam("recordUserId", paramDTO.get("userMailId").toString());
                            addressCreateCmd.putParam("companyId", paramDTO.get("companyId").toString());
                            addressCreateCmd.putParam("code", new Byte(code).toString());
                            addressCreateCmd.putParam("active", new Boolean(true));  // contact is active
                            if (language != null) {
                                addressCreateCmd.putParam("languageId", language.getLanguageId());// add language by default,it is language of the company.
                            }


                            addressCreateCmd.putParam("telecomMap", telecomMap);
                            addressCreateCmd.executeInStateless(ctx); //create the address
                        }
                    }
                }
            }
        }
    }
}
