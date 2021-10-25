package com.piramide.elwis.web.contactmanager.el;

import com.piramide.elwis.cmd.contactmanager.AdditionalAddressCmd;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.dto.contactmanager.AdditionalAddressDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.controller.SearchParameter;
import org.alfacentauro.fantabulous.tag.SelectItemPreProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Class to pre-process additional address select tag, if not exist main address, this is created for this contact
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class AdditionalAddressSelectPreProcessor implements SelectItemPreProcessor {
    private Log log = LogFactory.getLog(this.getClass());

    public Collection<Map> preProcessListStructureResult(Collection<Map> listResult, Parameters parameters, boolean readOnly, HttpServletRequest request) {
        Collection<Map> processedListResult = listResult;

        if (listResult != null) {
            Map mainAddressItemMap = null;
            for (Iterator<Map> iterator = listResult.iterator(); iterator.hasNext();) {
                Map rowMap = iterator.next();

                String type = rowMap.get("additionalAddressType").toString();
                if (ContactConstants.AdditionalAddressType.MAIN.equal(type)) {
                    rowMap.put("name", getMainAddressName(request));
                    mainAddressItemMap = rowMap;
                    iterator.remove();
                    break;
                }
            }

            if (mainAddressItemMap != null) {
                processedListResult = new ArrayList<Map>();
                processedListResult.add(mainAddressItemMap);
                processedListResult.addAll(listResult);

            } else if (!readOnly) {
                processedListResult = createMainAddress(listResult, parameters, request);
            }
        }

        return processedListResult;
    }

    private String getMainAddressName(HttpServletRequest request) {
        return JSPHelper.getMessage(request, "AdditionalAddress.item.mainAddress");
    }

    private Collection<Map> createMainAddress(Collection<Map> listResult, Parameters parameters, HttpServletRequest request) {
        Collection<Map> newListResult = listResult;

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        Object addressId = findFieldValueInParameter("addressId", parameters);

        if (addressId != null && !"".equals(addressId.toString()) && Integer.valueOf(addressId.toString()) > 0) {

            AdditionalAddressCmd additionalAddressCmd = new AdditionalAddressCmd();
            additionalAddressCmd.setOp("createMainAddress");
            additionalAddressCmd.putParam("name", ContactConstants.ADDITIONALADDRESS_MAINADDRESS_NAME);
            additionalAddressCmd.putParam("addressId", addressId);
            additionalAddressCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
            additionalAddressCmd.putParam("additionalAddressType", ContactConstants.AdditionalAddressType.MAIN.getConstant());
            additionalAddressCmd.putParam("isDefault", Boolean.FALSE);
            additionalAddressCmd.putParam(getMainAddressDataMap(addressId));

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(additionalAddressCmd, request);
                AdditionalAddressDTO additionalAddressDTO = (AdditionalAddressDTO) resultDTO.get("mainAddAddressDTO");

                if (additionalAddressDTO != null) {
                    Map itemMap = new HashMap();
                    itemMap.put("additionalAddressId", additionalAddressDTO.get("additionalAddressId").toString());
                    itemMap.put("name", getMainAddressName(request));

                    newListResult = new ArrayList<Map>();
                    newListResult.add(itemMap);
                    newListResult.addAll(listResult);
                }

            } catch (AppLevelException e) {
                log.error("-> Execute " + AdditionalAddressCmd.class.getName() + " FAIL", e);
            }
        }

        return newListResult;
    }

    private Map getMainAddressDataMap(Object addressId) {
        Map addressMap = new HashMap();

        if (addressId != null) {
            LightlyAddressCmd addressCmd = new LightlyAddressCmd();
            addressCmd.putParam("addressId", addressId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(addressCmd, null);
                if (!resultDTO.isFailure()) {
                    addressMap.put("cityId", resultDTO.get("cityId"));
                    addressMap.put("countryId", resultDTO.get("countryId"));
                    addressMap.put("street", resultDTO.get("street"));
                    addressMap.put("houseNumber", resultDTO.get("houseNumber"));
                    addressMap.put("additionalAddressLine", resultDTO.get("additionalAddressLine"));
                }
            } catch (Exception e) {
                log.warn("Error in execute cmd..", e);
            }
        }
        return addressMap;
    }

    private Object findFieldValueInParameter(String fieldAlias, Parameters parameters) {
        Object value = null;

        SearchParameter searchParameter = parameters.getSearchParameter(fieldAlias);
        if (searchParameter != null) {
            value = searchParameter.getValueAsObject();
        }
        return value;
    }

}
