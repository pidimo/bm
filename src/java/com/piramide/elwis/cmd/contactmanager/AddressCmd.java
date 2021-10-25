package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.AccessRightDataLevelCmdUtil;
import com.piramide.elwis.domain.catalogmanager.City;
import com.piramide.elwis.domain.catalogmanager.Country;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.dto.catalogmanager.CityDTO;
import com.piramide.elwis.dto.catalogmanager.CountryDTO;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.CodeUtil;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.*;

/**
 * Execute command for address and makes business logic for it, this class is used to read Address information
 *
 * @author Ernesto
 * @version $Id: AddressCmd.java 10377 2013-09-27 15:59:03Z miguel $
 */
public class AddressCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing AddressCmd reading address");
        log.debug("addressId to read = " + paramDTO.get("addressId"));

        Address address = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(paramDTO), resultDTO, true);

        if (address != null) {
            //read categoryfield values for contact or organization
            String finderName = "findByAddressId";
            Object[] params = new Object[]{address.getAddressId(), address.getCompanyId()};
            List paramsAsList = Arrays.asList(params);

            CategoryUtilCmd myCmd = new CategoryUtilCmd();
            myCmd.putParam("finderName", finderName);
            myCmd.putParam("params", paramsAsList);
            myCmd.setOp("readCAtegoryFieldValues");
            myCmd.executeInStateless(ctx);
            ResultDTO myResultDTO = myCmd.getResultDTO();
            resultDTO.putAll(myResultDTO);

            //reading telecoms
            Collection telecoms = address.getTelecoms();
            Map telecomMap = new LinkedHashMap();
            TelecomDTO telecomDTO = null;
            for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                Telecom telecom = (Telecom) iterator.next();
                //add telecoms of address
                if (telecom.getContactPersonId() == null) {
                    TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
                    cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
                    cmd.putParam("companyId", telecom.getCompanyId());
                    cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, paramDTO.get("locale"));
                    cmd.putParam("telecomTypeId", telecom.getTelecomTypeId());
                    cmd.executeInStateless(ctx);
                    TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) cmd.getResultDTO().get(TelecomTypeSelectCmd.RESULT);
                    telecomDTO = new TelecomDTO(telecom.getTelecomId().toString(), telecom.getData(),
                            telecom.getDescription(), telecom.getPredetermined().booleanValue());
                    TelecomWrapperDTO.addToMapTelecomDTO(telecomMap, telecomDTO, telecomTypeDTO);
                }
            }
            resultDTO.put("telecomMap", telecomMap);


            if (address.getFreeText() != null && address.getFreeText().getValue() != null) {
                resultDTO.put("note", new String(address.getFreeText().getValue()));
            }
            if (address.getWayDescription() != null && address.getWayDescription().getValue() != null) {
                resultDTO.put("wayDescription", new String(address.getWayDescription().getValue()));
            }

            //recovering country code
            if (address.getCountryId() != null) {
                log.debug("Recovering the country code");
                log.debug("countryId = " + address.getCountryId());
                Country country = (Country) EJBFactory.i.findEJB(new CountryDTO(address.getCountryId()));
                if (country != null) {
                    resultDTO.put("countryCode", country.getCountryAreaCode());
                }
            }
            if (address.getCityId() != null) {
                City city = (City) EJBFactory.i.findEJB(new CityDTO(address.getCityId()));
                if (city != null) {
                    resultDTO.put("city", city.getCityName());
                    resultDTO.put("zip", city.getCityZip());
                    resultDTO.put("beforeZip", city.getCityZip());
                    resultDTO.put("cityId", city.getCityId());
                }
            } else {
                resultDTO.put("city", null);
                resultDTO.put("zip", null);
                resultDTO.put("cityId", null);
                resultDTO.put("beforeZip", null);
            }

            //If date do not have year
            if (address.getBirthday() != null) {
                if (address.getBirthday().toString().length() <= 5) {
                    resultDTO.put("dateWithoutYear", "true");
                }
            }

            resultDTO.put("isCustomer", new Boolean(CodeUtil.isCustomer(address.getCode().byteValue())));
            resultDTO.put("isSupplier", new Boolean(CodeUtil.isSupplier(address.getCode().byteValue())));
            resultDTO.put("isActive", address.getActive());

            //read address user groups with access right in data level
            readUserAddressAccessRight(address, ctx);

            // if address update is successful then update recent list, with addressId updated.
            if (paramDTO.get("userId") != null && paramDTO.get("companyId") != null) {
                RecentCmd recentCmd = new RecentCmd();
                recentCmd.putParam(paramDTO);
                recentCmd.setOp(CRUDDirector.OP_UPDATE);
                recentCmd.executeInStateless(ctx);
            }
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void readUserAddressAccessRight(Address address, SessionContext ctx) {
        UserAddressAccessCmd userAddressAccessCmd = new UserAddressAccessCmd();
        userAddressAccessCmd.setOp("readUserGroups");
        userAddressAccessCmd.putParam("addressId", address.getAddressId());

        userAddressAccessCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = userAddressAccessCmd.getResultDTO();

        List<Integer> resultUserGroupIdList = (List<Integer>) myResultDTO.get("accessUserGroupIdList");

        if (address.getPersonal() != null && address.getPersonal()) {
            resultUserGroupIdList.add(ContactConstants.CREATORUSER_ACCESSRIGHT);
        }

        resultDTO.put("accessUserGroupIds", AccessRightDataLevelCmdUtil.composeIdListAsStringValue(resultUserGroupIdList));
    }
}
