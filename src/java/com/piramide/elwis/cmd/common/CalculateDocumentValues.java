package com.piramide.elwis.cmd.common;

import com.piramide.elwis.cmd.utils.VariableConstants;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.admin.CompanyHome;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.domain.catalogmanager.Currency;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.salesmanager.Action;
import com.piramide.elwis.domain.salesmanager.ActionPosition;
import com.piramide.elwis.domain.salesmanager.ActionType;
import com.piramide.elwis.domain.salesmanager.ActionTypeHome;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import java.math.BigDecimal;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: CalculateDocumentValues.java 10391 2013-10-18 21:38:17Z miguel $
 */
public class CalculateDocumentValues {
    private final static Log log = LogFactory.getLog("CalculateDocumentValues");
    private static final String SPACE = " ";
    private String END_LINE;

    private int FIELDS_SIZE;
    private int maxFloatPart;
    private int maxIntPart;

    private AdditionalAddress mainAdditionalAddress;

    private String[] address_personValues;
    private String[] salesProcessActionValues;
    private String[] actionPositionValues;


    private String email;
    private boolean isPerson;
    private boolean isEmail;

    private Locale locale;

    private BigDecimal sumPositionTotalPrice;
    private boolean isSalesProcess;
    private List telecomTypes;
    private Map addressTelecoms;

    //add for generate document YUMI
    private String isoLang;
    private String isCampaign;

    private String nullEntityValue;

    public String isCampaign() {
        return isCampaign;
    }

    public void setIsCampaign(String campaign) {
        isCampaign = campaign;
    }

    public String getIsoLang() {
        return isoLang;
    }

    public void setIsoLang(String iso) {
        this.isoLang = iso;
    }

    public AdditionalAddress getMainAdditionalAddress() {
        return mainAdditionalAddress;
    }

    public void setMainAdditionalAddress(AdditionalAddress mainAdditionalAddress) {
        this.mainAdditionalAddress = mainAdditionalAddress;
    }

    public CalculateDocumentValues(boolean isperson) {
        isPerson = isperson;
        END_LINE = "\r\n";
        initialize();
    }

    public void initNumberFormat(int maxInPart, int maxFloatPart) {
        this.maxFloatPart = maxFloatPart;
        this.maxIntPart = maxInPart;
    }

    public void setEmail(boolean email) {
        isEmail = email;
    }

    public CalculateDocumentValues(boolean isperson, boolean isUpdate) {
        isPerson = isperson;
        //this.isUpdate = isUpdate;
        END_LINE = isUpdate ? "/" : "\r\n";
        initialize();
    }

    public void setCaseAsSalesProcess(Boolean isSalesProcess) {
        this.isSalesProcess = isSalesProcess.booleanValue();
    }

    public void initializeSalesProcess() {
        salesProcessActionValues = new String[VariableConstants.SALESPROCESS_ACTION_FIELDS.length];
        actionPositionValues = new String[VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS.length];
    }

    private void initialize() {
        salesProcessActionValues = new String[VariableConstants.SALESPROCESS_ACTION_FIELDS.length];
        actionPositionValues = new String[VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS.length];
        address_personValues = new String[VariableConstants.ADDRESS_PERSON_FIELDS.length];
        addressTelecoms = new LinkedHashMap();
    }

    public void setEmptySaleProcessValues() {
        for (int i = 0; i < actionPositionValues.length; i++) {
            actionPositionValues[i] = " ";
        }

        for (int i = 0; i < salesProcessActionValues.length; i++) {
            salesProcessActionValues[i] = " ";
        }
    }

    public void calculateSaleProcessActionPositionValues(ActionPosition actionPosition, Action action) {
        log.debug("Calculate action position values.." + actionPosition);

        for (int i = 0; i < actionPositionValues.length; i++) {
            actionPositionValues[i] = "";
        }

        //define prices as net or gross
        BigDecimal positionPrice;
        BigDecimal positionTotalPrice;
        if (FinanceConstants.NetGrossFLag.GROSS.equal(action.getNetGross())) {
            positionPrice = actionPosition.getUnitPriceGross();
            positionTotalPrice = actionPosition.getTotalPriceGross();
        } else {
            positionPrice = actionPosition.getPrice();
            positionTotalPrice = actionPosition.getTotalPrice();
        }

        //sum action position total price
        if (positionTotalPrice != null) {
            sumPositionTotalPrice = (sumPositionTotalPrice != null) ? sumPositionTotalPrice.add(positionTotalPrice) : positionTotalPrice;
        }

        actionPositionValues[VariableConstants.FIELD_SALESPROCESS_ACTIONPOSITION_NUMBER] = validValue(actionPosition.getNumber());
        actionPositionValues[VariableConstants.FIELD_SALESPROCESS_ACTIONPOSITION_PRICE] = decimal4DigitsValidValue(positionPrice, locale.getLanguage());
        actionPositionValues[VariableConstants.FIELD_SALESPROCESS_ACTIONPOSITION_QUANTITY] = decimalValidValue(actionPosition.getAmount(), locale.getLanguage());

        actionPositionValues[VariableConstants.FIELD_SALESPROCESS_ACTIONPOSITION_TOTAL] = decimalValidValue(positionTotalPrice, locale.getLanguage());

        if (actionPosition.getDescriptionId() != null) {
            actionPositionValues[VariableConstants.FIELD_SALESPROCESS_ACTIONPOSITION_TEXT] = readFreeTextValue(actionPosition.getDescriptionId());
        }

        Product product = readProduct(actionPosition.getProductId());
        if (product != null) {
            actionPositionValues[VariableConstants.FIELD_SALESPROCESS_ACTIONPOSITION_NAME] = validValue(product.getProductName());
            actionPositionValues[VariableConstants.FIELD_SALESPROCESS_ACTIONPOSITION_PRODUCTNUMBER] = validValue(product.getProductNumber());
        }
        if (actionPosition.getUnit() != null) {
            actionPositionValues[VariableConstants.FIELD_SALESPROCESS_ACTIONPOSITION_UNIT] = readProductUnitName(actionPosition.getUnit());
        }
    }

    public void calculateSalesProcessActionValues(Action action) {
        log.debug("calculate sales process action values....");

        for (int i = 0; i < salesProcessActionValues.length; i++) {
            salesProcessActionValues[i] = "";
        }

        salesProcessActionValues[VariableConstants.FIELD_SALESPROCESS_ACTION_NUMBER] = validValue(action.getNumber());
        salesProcessActionValues[VariableConstants.FIELD_SALESPROCESS_ACTION_DATE] = dateValidValue(readActionContactSendDate(action.getContactId()), locale.getLanguage());
        salesProcessActionValues[VariableConstants.FIELD_SALESPROCESS_ACTION_TOTALAMOUNT] = decimalValidValue(action.getValue(), locale.getLanguage());

        if (action.getActionTypeId() != null) {
            salesProcessActionValues[VariableConstants.FIELD_SALESPROCESS_ACTION_TYPE] = readActionTypeName(action.getActionTypeId());
        }
        if (action.getCurrencyId() != null) {
            salesProcessActionValues[VariableConstants.FIELD_SALESPROCESS_ACTION_CURRENCY] = readCurrencyName(action.getCurrencyId());
        }

        //this value is sum of all action position total prices
        salesProcessActionValues[VariableConstants.FIELD_SALESPROCESS_ACTION_VALUE] = decimalValidValue(sumPositionTotalPrice, locale.getLanguage());
    }

    public void calculateValuesToNullEntity(Date date) {
        String strValue = (nullEntityValue != null) ? nullEntityValue : "";

        for (int i = 0; i < VariableConstants.ADDRESS_PERSON_FIELDS.length; i++) {
            address_personValues[i] = strValue;
        }

        //set date value
        setValue(VariableConstants.FIELD_DATE, dateValidValue(date, SystemPattern.DEFAULT_ISOLANG));

        //set Telecoms as null default value
        for (Iterator iterator = telecomTypes.iterator(); iterator.hasNext();) {
            Integer telecomTypeId = (Integer) iterator.next();
            addressTelecoms.put("Address_tel_" + telecomTypeId, strValue);
        }
    }

    /**
     * Si se selecciona a un contactperson de un person, NO se utiliza los valores del contactperson, solo se calcula
     * en base a los valores del person.<br>
     * Para telecoms, si es una persona, se utilizara los telecoms privados.
     *
     * @param organization  Puede ser una organizacion o una persona
     * @param contactPerson En caso de ser una organizacion, entonces se utilizara al contactperson para los calculos
     * @param company
     * @param employeeBean
     * @param user
     * @param requestLocale
     * @param date
     * @param telecomTypeId
     */
    public void calculateValues(Address organization, Address contactPerson, Address company, Employee employeeBean, Address user, String requestLocale, Date date, Integer telecomTypeId) throws FinderException {
        log.debug("+++++++++++++++++++++++ [Func-calculateValues]:Init ++++++++++++++++++++++++");
        //Initialization with blank string
        for (int i = 0; i < VariableConstants.ADDRESS_PERSON_FIELDS.length; i++) {
            address_personValues[i] = "";
        }

        log.debug("Init extract values..... " + organization + " - " + contactPerson + " - " + company + " - " + employeeBean + " - " + user + " - Telecom:" + telecomTypeId);

        Address employee = null;
        if (employeeBean != null) {
            employee = employeeBean.getAddress();
        }

        //I18N of Date
        String isoLang_ = getDefaultIsoLang(organization, contactPerson, company, user);

        if (isoLang_ == null) {
            isoLang_ = requestLocale;
        }
        //AQUI deberia sacar el lenguage por defecto ...
        locale = new Locale(isoLang_);

        setValue(VariableConstants.FIELD_DATE, dateValidValue(date, isoLang_));

        // Set Address Values
        boolean areSameCountry = setCountryAndAreSameCountry(organization, contactPerson, company, employee);

        //Set email..
        if (isEmail) {
            setEmail4Address(organization, contactPerson, telecomTypeId);
        }

        if (isPerson && contactPerson != null) {
            setOrganizationValues(contactPerson, areSameCountry);
            setPersonValues(contactPerson, null, company, user);
        } else {
            setOrganizationValues(organization, areSameCountry);
            // Set Person Values
            setPersonValues(organization, contactPerson, company, user);
        }

        StringBuffer temp = new StringBuffer();
        if (!isPerson) {
            if (isValidValue(VariableConstants.FIELD_ADDRESS_NAME)) {
                temp.append(getValue(VariableConstants.FIELD_ADDRESS_NAME));
            }

            if (isValidValue(VariableConstants.FIELD_ADDRESS_NAME) && isValidValue(VariableConstants.FIELD_PERSON_NAME)) {
                temp.append(END_LINE);
            }
        }

        if (isValidValue(VariableConstants.FIELD_PERSON_NAME)) {
            temp.append(getValue(VariableConstants.FIELD_PERSON_ADDRESSNAME));
        }

        setValue(VariableConstants.FIELD_ADDRESS_NAMES, new String(temp));

        setAddressCustomerNumber(organization);

        try {
            if (isPerson) {
                setPersonTelecoms(organization);
            } else {
                setContactPersonTelecoms(organization, contactPerson);
            }
        } catch (FinderException e) {
            log.debug("No telecoms found");
        }

        log.debug(" +++++++++++++++++++++++++++++++++++++++++++ ");
    }

    /**
     * Los telecomTypes son calculados en CompanyInfoValues#setTelecoms
     *
     * @param organization
     * @param contactPerson
     * @throws FinderException
     * @see CompanyInfoValues#setTelecoms(com.piramide.elwis.domain.contactmanager.Address, com.piramide.elwis.domain.contactmanager.Address)
     */
    private void setContactPersonTelecoms(Address organization, Address contactPerson) throws FinderException {
        log.debug("[Func-calculateValues]:Extract Address(ContactPerson) Telecoms");
        if (telecomTypes != null && !telecomTypes.isEmpty()) {
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            Collection telecomsOrganization = telecomHome.findAllAddressDefaultTelecoms(organization.getAddressId());
            Collection telecomsContactPerson = new ArrayList();
            if (contactPerson != null) {
                telecomsContactPerson = telecomHome.findAllContactPersonDefaultTelecoms(organization.getAddressId(), contactPerson.getAddressId());
            }
            for (Iterator iterator = telecomTypes.iterator(); iterator.hasNext();) {
                Integer telecomType = (Integer) iterator.next();
                boolean find = false;
                String telecomValue = "";
                for (Iterator iterator1 = telecomsContactPerson.iterator(); iterator1.hasNext();) {
                    Telecom telecom = (Telecom) iterator1.next();
                    if (telecom.getTelecomTypeId().equals(telecomType)) {
                        find = true;
                        telecomValue = telecom.getData();
                        iterator1.remove();
                        //noinspection UnusedAssignment
                        iterator1 = telecomsContactPerson.iterator();
                        break;
                    }
                }

                if (!find) {
                    for (Iterator iterator1 = telecomsOrganization.iterator(); iterator1.hasNext();) {
                        Telecom telecom = (Telecom) iterator1.next();
                        if (telecom.getTelecomTypeId().equals(telecomType)) {
                            //find = true;
                            telecomValue = telecom.getData();
                            iterator1.remove();
                            //noinspection UnusedAssignment
                            iterator1 = telecomsOrganization.iterator();
                            break;
                        }
                    }
                }
                addressTelecoms.put("Address_tel_" + telecomType, telecomValue);
            }
        }
    }

    private void setPersonTelecoms(Address person) throws FinderException {
        log.debug("[Func-calculateValues]:Extract Address(Person) Telecoms");
        if (telecomTypes != null && !telecomTypes.isEmpty()) {
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            Collection telecomsPerson = telecomHome.findAllAddressDefaultTelecoms(person.getAddressId());
            for (Iterator iterator = telecomTypes.iterator(); iterator.hasNext();) {
                Integer telecomType = (Integer) iterator.next();
                String telecomValue = "";
                for (Iterator iterator1 = telecomsPerson.iterator(); iterator1.hasNext();) {
                    Telecom telecom = (Telecom) iterator1.next();
                    if (telecom.getTelecomTypeId().equals(telecomType)) {
                        telecomValue = telecom.getData();
                        iterator1.remove();
                        //noinspection UnusedAssignment
                        iterator1 = telecomsPerson.iterator();
                        break;
                    }
                }
                addressTelecoms.put("Address_tel_" + telecomType, telecomValue);
            }
        }
    }

    private void setEmail4Address(Address organization, Address contactPerson, Integer telecomId) {
        log.debug("setEmail4Address");
        log.debug("Address:" + organization.getName1() + " - " + "Address1:" + organization.getName2());
        log.debug("TelecomId" + telecomId + " - AddressID:" + organization.getAddressId());
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        Telecom telecom = null;
        if (contactPerson != null) {
            try {
                telecom = telecomHome.findContactPersonDefaultTelecomsByTypeId(organization.getAddressId(), contactPerson.getAddressId(), telecomId);
            } catch (FinderException e) {
                //log.debug("---FINDER:1------------");
            }
            if (telecom == null) {
                try {
                    Collection telecoms = telecomHome.findAllContactPersonTelecomsByTypeId(organization.getAddressId(), contactPerson.getAddressId(), telecomId);
                    if (!telecoms.isEmpty()) {
                        telecom = (Telecom) telecoms.iterator().next();
                    }
                } catch (FinderException e) {
                    //log.debug("---FINDER:2------------");
                }
            }
        } else {
            try {
                telecom = telecomHome.findAddressDefaultTelecomsByTypeId(organization.getAddressId(), telecomId);
            } catch (FinderException e) {/*log.debug("---FINDER:3------------");*/}
            if (telecom == null) {
                try {
                    Collection telecoms = telecomHome.findAllAddressTelecomsByTypeId(organization.getAddressId(), telecomId);
                    //log.debug("TELECOMS:" + telecoms);
                    if (telecoms != null && !telecoms.isEmpty()) {
                        telecom = (Telecom) telecoms.iterator().next();
                    }
                } catch (FinderException e) {/*log.debug("---FINDER:4------------");*/}
            }
        }

        if (telecom != null) {
            setEmail(telecom.getData());
        }
    }


    /**
     * Establish Organization values
     * <p/>
     * Calculate Address Complete
     * <Address.Street>
     * <Country.areacode>-<City.zip><City.cityname>  {<Country.areacode>- :Only Address.country != LoggedCompany.country}
     * <Country.countryname>  {Only Address.country != LoggedCompany.country}
     *
     * @param organization
     * @param areSameCountry
     */
    private void setOrganizationValues(Address organization, boolean areSameCountry) {
        log.debug("Set Organization");
        StringBuffer temp = new StringBuffer();
        StringBuffer tempAddressComplete = new StringBuffer();

        City city = organization.getCityEntity();
        String street = organization.getStreet();
        String houseNumber = organization.getHouseNumber();
        String additionalLine = organization.getAdditionalAddressLine();

        AdditionalAddress additionalAddress = findAdditionalAddressForGenerate(organization);

        if (additionalAddress != null) {
            city = findCity(additionalAddress.getCityId());
            street = additionalAddress.getStreet();
            houseNumber = additionalAddress.getHouseNumber();
            additionalLine = additionalAddress.getAdditionalAddressLine();
        }

        if (city != null) {
            setValue(VariableConstants.FIELD_ADDRESS_CITY, city.getCityName());
            setValue(VariableConstants.FIELD_ADDRESS_ZIP, city.getCityZip());
        }

        //additional line
        if (isValidValue(additionalLine)) {
            if (temp.length() > 0) {
                temp.append(END_LINE);
            }
            temp.append(additionalLine);
        }

        // Initialize Street
        setValue(VariableConstants.FIELD_ADDRESS_STREET, street);


        if (isValidValue(VariableConstants.FIELD_ADDRESS_STREET)) {
            // Add HouseNumber to Street
            if (isValidValue(houseNumber)) {
                setValue(VariableConstants.FIELD_ADDRESS_STREET, getValue(VariableConstants.FIELD_ADDRESS_STREET) + SPACE + houseNumber);
            }
            // Add Street
            if (temp.length() > 0) {
                temp.append(END_LINE);
            }
            temp.append(getValue(VariableConstants.FIELD_ADDRESS_STREET));
        }

        // Add zip
        if (isValidValue(VariableConstants.FIELD_ADDRESS_ZIP)) {
            tempAddressComplete.append(getValue(VariableConstants.FIELD_ADDRESS_ZIP)).append(SPACE);
        }

        // Add cityName
        if (isValidValue(VariableConstants.FIELD_ADDRESS_CITY)) {
            tempAddressComplete.append(getValue(VariableConstants.FIELD_ADDRESS_CITY));
        }


        String areacode_zip_city = new String(tempAddressComplete);
        // Add endLine only exist (zip, city, areacode)
        if (isValidValue(VariableConstants.FIELD_ADDRESS_STREET) && isValidValue(areacode_zip_city)) {
            temp.append(END_LINE);
        }

        if (isValidValue(areacode_zip_city)) {
            temp.append(areacode_zip_city);
        }

        // Add country
        if (!areSameCountry) {
            if (isValidValue(areacode_zip_city) && isValidValue(VariableConstants.FIELD_ADDRESS_COUNTRY)) {
                temp.append(END_LINE);
            }
            if (isValidValue(VariableConstants.FIELD_ADDRESS_COUNTRY)) {
                temp.append(getValue(VariableConstants.FIELD_ADDRESS_COUNTRY));
            }
        }

        setValue(VariableConstants.FIELD_ADDRESS_COMPLETE, new String(temp));

        if (!isPerson) {
            // Add Address Name
            setValue(VariableConstants.FIELD_ADDRESS_NAME1, organization.getName1());
            setValue(VariableConstants.FIELD_ADDRESS_NAME2, organization.getName2());
            setValue(VariableConstants.FIELD_ADDRESS_NAME3, organization.getName3());

            temp = new StringBuffer();
            temp.append(getValue(VariableConstants.FIELD_ADDRESS_NAME1));
            if (isValidValue(VariableConstants.FIELD_ADDRESS_NAME2)) {
                temp.append(END_LINE).append(getValue(VariableConstants.FIELD_ADDRESS_NAME2));
            }
            if (isValidValue(VariableConstants.FIELD_ADDRESS_NAME3)) {
                temp.append(END_LINE).append(getValue(VariableConstants.FIELD_ADDRESS_NAME3));
            }

            setValue(VariableConstants.FIELD_ADDRESS_NAME, new String(temp));
        }
    }


    private void setPersonValues(Address organization, Address contactPerson, Address company, Address user) {
        log.debug("Set person..");
        StringBuffer temp;
        if (isPerson) {
            contactPerson = organization;
        }

        if (contactPerson != null) {

            setValue(VariableConstants.FIELD_PERSON_FIRSTNAME, contactPerson.getName2());
            setValue(VariableConstants.FIELD_PERSON_LASTNAME, contactPerson.getName1());
            if (contactPerson.getTitleId() != null) {
                setValue(VariableConstants.FIELD_PERSON_TITLE, contactPerson.getTitle().getTitleName());
            }

            // AddressSalutation
            setValue(VariableConstants.FIELD_PERSON_ADDRESSSALUTATION, getSalutation(contactPerson,
                    organization,
                    company.getLanguageId(),
                    user.getLanguageId(),
                    false));

            // Add LetterSalutation
            setValue(VariableConstants.FIELD_PERSON_LETTERSALUTATION, getSalutation(contactPerson,
                    organization,
                    company.getLanguageId(),
                    user.getLanguageId(),
                    true));

            // Init Person Name
            temp = new StringBuffer();
            if (isValidValue(VariableConstants.FIELD_PERSON_FIRSTNAME)) {
                temp.append(getValue(VariableConstants.FIELD_PERSON_FIRSTNAME));
            }

            if (temp.length() > 0) {
                temp.append(SPACE);
            }
            temp.append(getValue(VariableConstants.FIELD_PERSON_LASTNAME));

            // Add Person Name
            setValue(VariableConstants.FIELD_PERSON_NAME, new String(temp));

            // Init AddressName
            temp = new StringBuffer();
            temp.append(getValue(VariableConstants.FIELD_PERSON_ADDRESSSALUTATION));

            if (temp.length() > 0) {
                if (isPerson) {
                    temp.append(END_LINE);
                } else if (isValidValue(VariableConstants.FIELD_PERSON_TITLE)) {
                    temp.append(SPACE);
                }
            }

            temp.append(getValue(VariableConstants.FIELD_PERSON_TITLE));
            if (temp.length() > 0 && isValidValue(VariableConstants.FIELD_PERSON_NAME)) {
                if (!isPerson) {
                    temp.append(SPACE);
                } else if (isValidValue(VariableConstants.FIELD_PERSON_TITLE)) {
                    temp.append(SPACE);
                }
            }

            temp.append(getValue(VariableConstants.FIELD_PERSON_NAME));

            //contact person additional line
            if (!organization.getAddressId().equals(contactPerson.getAddressId())) {
                ContactPerson realContactPerson = findContactPerson(organization.getAddressId(), contactPerson.getAddressId());
                if (realContactPerson != null) {
                    String additionalLine = realContactPerson.getAddAddressLine();
                    if (isValidValue(additionalLine)) {
                        if (temp.length() > 0) {
                            temp.append(END_LINE);
                        }
                        temp.append(additionalLine);
                    }
                }
            }

            // Add AddressName
            setValue(VariableConstants.FIELD_PERSON_ADDRESSNAME, new String(temp));

            // Init LetterName
            temp = new StringBuffer();
            temp.append(getValue(VariableConstants.FIELD_PERSON_LETTERSALUTATION));
            if (temp.length() > 0 && isValidValue(VariableConstants.FIELD_PERSON_TITLE)) {
                temp.append(SPACE);
            }
            temp.append(getValue(VariableConstants.FIELD_PERSON_TITLE));
            if (temp.length() > 0) {
                temp.append(SPACE);
            }
            temp.append(getValue(VariableConstants.FIELD_PERSON_LASTNAME));

            // Add LetterName
            setValue(VariableConstants.FIELD_PERSON_LETTERNAME, new String(temp));
        } else if (!isPerson) {
            //se default values as person to organization from company
            setValuesFromCompany(organization, company, user);
        }
    }

    private void setValuesFromCompany(Address organization, Address company, Address user) {
        Company companyInfo = findCompany(organization.getCompanyId());
        if (companyInfo != null) {
            // Add default LetterSalutation
            setValue(VariableConstants.FIELD_PERSON_LETTERSALUTATION, getSalutationFromCompany(companyInfo, organization, company.getLanguageId(), user.getLanguageId(), true));
            // AddressSalutation
            setValue(VariableConstants.FIELD_PERSON_ADDRESSSALUTATION, getSalutationFromCompany(companyInfo, organization, company.getLanguageId(), user.getLanguageId(), false));

            // Add LetterName
            setValue(VariableConstants.FIELD_PERSON_LETTERNAME, getValue(VariableConstants.FIELD_PERSON_LETTERSALUTATION));
            // Add AddressName
            setValue(VariableConstants.FIELD_PERSON_ADDRESSNAME, getValue(VariableConstants.FIELD_PERSON_ADDRESSSALUTATION));
        }
    }

    private String getSalutationValue(Integer letterTextId, Integer languageId) {
        log.debug("GETSALUTATIONL- letterTextId:" + letterTextId + " - languageId:" + languageId);
        try {
            LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
            LangText langText = langTextHome.findByPrimaryKey(new LangTextPK(letterTextId, languageId));
            return langText.getText();
        } catch (FinderException e) {
            log.debug("Error to load salutation with letterTextId:" + letterTextId + " - languageId:" + languageId);
        }
        return null;
    }

    private String getSalutation(Address person, Address organization, Integer lang_company, Integer lang_user, boolean isLetterSalutation) {
        String salutationAddress = null;
        Integer salutationTextId;
        Integer languageId;
        //log.debug("getSalutationValue:Organization:" + organization);
        if (person.getSalutation() != null) {
            salutationTextId = isLetterSalutation ? person.getSalutation().getLetterTextId() : person.getSalutation().getAddressTextId();
            languageId = person.getLanguageId();

            //if( isCampaign != null && !"".equals(isCampaign) && !languageId.equals(new Integer(isoLang))) languageId = new Integer(isoLang); // set value ISO.
            if (isCampaign != null && !"".equals(isCampaign) && !((new Integer(isoLang).equals(languageId)))) {
                languageId = new Integer(isoLang); // set value ISO. //todo: mikyFix, solo se cambio el orden, preguntar a alex
            }

            log.debug("Person:" + person.getAddressId() + " langId:" + languageId);
            salutationAddress = getSalutationValue(salutationTextId, languageId);
            if (salutationAddress != null) {
                return salutationAddress;
            }
            if (organization != null && !isPerson) {
                salutationAddress = getSalutationValue(salutationTextId, organization.getLanguageId());
                if (salutationAddress != null) {
                    return salutationAddress;
                }

                salutationAddress = getSalutationValue(salutationTextId, lang_company);
                if (salutationAddress != null) {
                    return salutationAddress;
                }

                salutationAddress = getSalutationValue(salutationTextId, lang_user);
            }
        }
        //log.debug("getSalutation - IsLetterText:" + isLetterSalutation + " - salutationAddress:" + salutationAddress);
        return salutationAddress;
    }

    private String getSalutationFromCompany(Company companyInfo, Address organization, Integer lang_company, Integer lang_user, boolean isLetterSalutation) {
        String salutationAddress = null;
        Integer salutationTextId;
        Integer languageId;

        if (companyInfo.getSalutationId() != null) {
            Salutation salutation = findSalutation(companyInfo.getSalutationId());
            if (salutation != null) {
                salutationTextId = isLetterSalutation ? salutation.getLetterTextId() : salutation.getAddressTextId();
                languageId = organization.getLanguageId();

                if (isCampaign != null && !"".equals(isCampaign) && !((new Integer(isoLang).equals(languageId)))) {
                    languageId = new Integer(isoLang); // set value ISO.
                }

                log.debug("Organization:" + organization.getAddressId() + " langId:" + languageId);
                salutationAddress = getSalutationValue(salutationTextId, languageId);
                if (salutationAddress != null) {
                    return salutationAddress;
                }

                salutationAddress = getSalutationValue(salutationTextId, lang_company);
                if (salutationAddress != null) {
                    return salutationAddress;
                }

                salutationAddress = getSalutationValue(salutationTextId, lang_user);
            }
        }
        return salutationAddress;
    }


    private String getDefaultIsoLang(Address address, Address contactPerson, Address company, Address currentUser) throws FinderException {
        log.debug("getIsoLang:Organiztion:" + contactPerson);
        String isoLang_ = null;
        if (!isPerson && contactPerson != null && contactPerson.getLanguageId() != null) {
            isoLang_ = contactPerson.getLanguage().getLanguageIso();
        } else if (address.getLanguageId() != null) {
            isoLang_ = address.getLanguage().getLanguageIso();
        } else if (company.getLanguageId() != null) {
            isoLang_ = company.getLanguage().getLanguageIso();
        } else if (currentUser.getLanguageId() != null) {
            isoLang_ = currentUser.getLanguage().getLanguageIso();
        }
        log.debug("ISO language ..." + isoLang_);
        return isoLang_;
    }

    private boolean setCountryAndAreSameCountry(Address address, Address contactPerson, Address company, Address employee) {
        String fromCountry = null;
        String toCountry = null;
        String areaCode = null;

        log.debug("GetCountry..");
        /*
        // Now, always to be use the country of Organization
        if (isPerson) contactPerson = null;

        if (contactPerson != null && contactPerson.getCountryId() != null) {
            toCountry = contactPerson.getCountry().getCountryName();
            areaCode = contactPerson.getCountry().getCountryAreaCode();
        } else if (address.getCountryId() != null) {
            toCountry = address.getCountry().getCountryName();
            areaCode = address.getCountry().getCountryAreaCode();
        } */

        Country country = findAddressCountry(address);

        if (country != null) {
            toCountry = country.getCountryName();
            areaCode = country.getCountryAreaCode();
            setValue(VariableConstants.FIELD_ADDRESS_COUNTRY, toCountry);
            setValue(VariableConstants.FIELD_ADDRESS_COUNTRYCODE, areaCode);
        }

        if (company.getCountryId() != null) {
            fromCountry = company.getCountry().getCountryName();
        } else if (employee != null && employee.getCountryId() != null) {
            fromCountry = employee.getCountry().getCountryName();
        }

        return toCountry != null && toCountry.equals(fromCountry);
    }

    private String validValue(String value) {
        return value != null && value.trim().length() > 0 ? value : "";
    }

    private String validValue(Integer value) {
        return value != null ? value.toString() : "";
    }

    private boolean isValidValue(String value) {
        return (value != null && value.length() > 0);
    }

    private boolean isValidValue(int pos) {
        String value = getValue(pos);//Address
        return (value != null && value.length() > 0);
    }

    private void setValue(int field, String value) {
        address_personValues[field] = validValue(value);
    }

    public String getValue(int field) {
        return address_personValues[field];
    }

    public Object[] getValues(Object[] companyValues, Object[] company_employeeTelecoms, boolean withSalesProcess) {
        int length = VariableConstants.ADDRESS_PERSON_FIELDS.length +
                VariableConstants.COMPANY_EMPLOYEE_FIELDS.length +
                VariableConstants.SALESPROCESS_ACTION_FIELDS.length +
                company_employeeTelecoms.length +
                telecomTypes.size();
        if (!withSalesProcess) {
            length = length + VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS.length;
        }
        String[] values = new String[length];
        int lastPos = 0;
        // Copy address_person values
        System.arraycopy(address_personValues, 0, values, 0, VariableConstants.ADDRESS_PERSON_FIELDS.length);
        lastPos += VariableConstants.ADDRESS_PERSON_FIELDS.length;

        // Copy company_employee values
        System.arraycopy(companyValues, 0, values, lastPos, VariableConstants.COMPANY_EMPLOYEE_FIELDS.length);
        lastPos += VariableConstants.COMPANY_EMPLOYEE_FIELDS.length;

        // Copy Salesprocess action values
        System.arraycopy(salesProcessActionValues, 0, values, lastPos, VariableConstants.SALESPROCESS_ACTION_FIELDS.length);
        lastPos += VariableConstants.SALESPROCESS_ACTION_FIELDS.length;

        if (!withSalesProcess) {
            // Copy SalesProcess values
            System.arraycopy(actionPositionValues, 0, values, lastPos, VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS.length);
            lastPos += VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS.length;
        }

        // Copy telecom values
        log.debug("TELECOM-lastPos:" + lastPos + " - CommTEL:" + company_employeeTelecoms.length);
        System.arraycopy(company_employeeTelecoms, 0, values, lastPos, company_employeeTelecoms.length);
        lastPos += company_employeeTelecoms.length;

        log.debug("lastPos:" + lastPos + " - LENGHT:" + length + " - SIZE _TELECOMS:" + addressTelecoms.size());
        System.arraycopy(addressTelecoms.values().toArray(), 0, values, lastPos, telecomTypes.size());
        return values;
    }

    private Company findCompany(Integer companyId) {
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        Company company = null;
        try {
            company = companyHome.findByPrimaryKey(companyId);
        } catch (FinderException e) {
            log.debug("Not foun company.." + e);
        }
        return company;
    }

    private Salutation findSalutation(Integer salutationId) {
        SalutationHome salutationHome = (SalutationHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_SALUTATION);
        Salutation salutation = null;
        try {
            salutation = salutationHome.findByPrimaryKey(salutationId);
        } catch (FinderException e) {
            log.debug("Not found salutation.." + e);
        }
        return salutation;
    }

    public int length() {
        return FIELDS_SIZE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        log.debug("Set email : " + email);
        this.email = email;
    }

    public String[] getAddress_personValues() {
        return address_personValues;
    }

    public String[] getSalesProcessActionValues() {
        return salesProcessActionValues;
    }

    public String[] getActionPositionValues() {
        return actionPositionValues;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setTelecomTypes(List telecomTypes) {
        this.telecomTypes = telecomTypes;
    }

    /**
     * set jump line '<br/>' to HTML values in send mails
     */
    public void setEndLineAsHTMLDocument() {
        END_LINE = "<br/>";
    }

    public void setNullEntityValue(String nullEntityValue) {
        this.nullEntityValue = nullEntityValue;
    }

    private String dateValidValue(Date date, String isoLanguage) {
        String dateValue = "";
        if (date != null) {
            dateValue = DateUtils.parseDate(date, SystemPattern.getDatePattern(isoLanguage));
        }
        return dateValue;
    }

    private String dateValidValue(Integer dateAsInteger, String isoLanguage) {
        String dateValue = "";
        if (dateAsInteger != null) {
            dateValue = dateValidValue(DateUtils.integerToDate(dateAsInteger), isoLanguage);
        }
        return dateValue;
    }

    private String decimalValidValue(BigDecimal decimal, String isoLanguage) {
        String value = "";
        if (decimal != null) {
            String decimalPattern = SystemPattern.getDecimalPattern(isoLanguage);
            value = validValue(FormatUtils.formatDecimal(decimal, new Locale(isoLanguage), decimalPattern));
        }
        return value;
    }

    private String decimal4DigitsValidValue(BigDecimal decimal, String isoLanguage) {
        String value = "";
        if (decimal != null) {
            String decimalPattern = SystemPattern.getDecimalPattern4Digits(isoLanguage);
            value = validValue(FormatUtils.formatDecimal(decimal, new Locale(isoLanguage), decimalPattern));
        }
        return value;
    }

    private void setAddressCustomerNumber(Address address) {
        CustomerHome customerHome = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        try {
            Customer customer = customerHome.findByPrimaryKey(address.getAddressId());
            setValue(VariableConstants.FIELD_ADDRESS_CUSTOMERNUMBER, customer.getNumber());
        } catch (FinderException e) {
            log.debug("Not found customer to address:" + address.getAddressId());
        }
    }

    private String readActionTypeName(Integer actionTypeId) {
        String actionTypeName = "";
        ActionTypeHome actionTypeHome = (ActionTypeHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTIONTYPE);
        try {
            ActionType actionType = actionTypeHome.findByPrimaryKey(actionTypeId);
            actionTypeName = validValue(actionType.getActionTypeName());
        } catch (FinderException e) {
            log.debug("Not found action Type:" + actionTypeId);
        }
        return actionTypeName;
    }

    private String readCurrencyName(Integer currencyId) {
        String currencyName = "";
        CurrencyHome currencyHome = (CurrencyHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CURRENCY);
        try {
            Currency currency = currencyHome.findByPrimaryKey(currencyId);
            currencyName = validValue(currency.getCurrencyName());
        } catch (FinderException e) {
            log.debug("Not found currency:" + currencyId);
        }
        return currencyName;
    }

    private Product readProduct(Integer productId) {
        Product product = null;
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        try {
            product = productHome.findByPrimaryKey(productId);
        } catch (FinderException e) {
            log.debug("Not found Product:" + productId);
        }
        return product;
    }

    private String readFreeTextValue(Integer freeTextId) {
        String freeTextValue = "";
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        if (freeTextId != null) {
            try {
                FreeText freeText = freeTextHome.findByPrimaryKey(freeTextId);
                if (freeText.getValue() != null) {
                    freeTextValue = new String(freeText.getValue());
                }
            } catch (FinderException e) {
                log.debug("Not found freeText:" + freeTextId);
            }
        }
        return freeTextValue;
    }

    private String readProductUnitName(Integer unitId) {
        String unitName = "";
        ProductUnitHome productUnitHome = (ProductUnitHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PRODUCTUNIT);
        try {
            ProductUnit productUnit = productUnitHome.findByPrimaryKey(unitId);
            unitName = validValue(productUnit.getUnitName());
        } catch (FinderException e) {
            log.debug("Not found product unit:" + unitId);
        }
        return unitName;
    }

    private Integer readActionContactSendDate(Integer contactId) {
        Integer sendDate = null;
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        try {
            Contact contact = contactHome.findByPrimaryKey(contactId);
            sendDate = contact.getDateStart();
        } catch (FinderException e) {
            log.debug("Not found communication:" + contactId);
        }
        return sendDate;
    }

    private AdditionalAddress findAdditionalAddressForGenerate(Address address) {
        AdditionalAddress additionalAddress = null;
        if (mainAdditionalAddress != null) {
            additionalAddress = mainAdditionalAddress;
        } else {
            //verify if has additional address as default
            additionalAddress = findAddressDefaultAdditionalAddress(address.getAddressId(), address.getCompanyId());
        }

        if (additionalAddress != null && ContactConstants.AdditionalAddressType.MAIN.equal(additionalAddress.getAdditionalAddressType())) {
            return null;
        }
        return additionalAddress;
    }

    private AdditionalAddress findAddressDefaultAdditionalAddress(Integer addressId, Integer companyId) {
        AdditionalAddress additionalAddress = null;
        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);

        Collection additionalAddressDefaults = null;
        try {
            additionalAddressDefaults = additionalAddressHome.findByDefault(addressId, companyId);
            if (!additionalAddressDefaults.isEmpty()) {
                additionalAddress = (AdditionalAddress) additionalAddressDefaults.iterator().next();
            }
        } catch (FinderException e) {
            log.debug("error in found the default additional address..", e);
        }

        return additionalAddress;
    }

    private Country findAddressCountry(Address address) {
        Country country = address.getCountry();

        AdditionalAddress additionalAddress = findAdditionalAddressForGenerate(address);
        if (additionalAddress != null) {
            country = findCountry(additionalAddress.getCountryId());
        }
        return country;
    }

    private City findAddressCity(Address address) {
        City city = address.getCityEntity();

        AdditionalAddress additionalAddress = findAdditionalAddressForGenerate(address);
        if (additionalAddress != null) {
            city = findCity(additionalAddress.getCityId());
        }
        return city;
    }

    private Country findCountry(Integer countryId) {
        Country country = null;
        if (countryId != null) {
            CountryHome countryHome = (CountryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COUNTRY);
            try {
                country = countryHome.findByPrimaryKey(countryId);
            } catch (FinderException e) {
                log.debug("Error in find country " + countryId, e);
            }
        }
        return country;
    }

    private City findCity(Integer cityId) {
        City city = null;

        if (cityId != null) {
            CityHome cityHome = (CityHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CITY);
            try {
                city = cityHome.findByPrimaryKey(cityId);
            } catch (FinderException e) {
                log.debug("Error in find city " + cityId, e);
            }
        }
        return city;
    }

    private ContactPerson findContactPerson(Integer addressId, Integer contactPersonId) {
        ContactPerson contactPerson = null;

        if (addressId != null && contactPersonId != null) {
            ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
            ContactPersonPK contactPersonPK = new ContactPersonPK(addressId, contactPersonId);
            try {
                contactPerson = contactPersonHome.findByPrimaryKey(contactPersonPK);
            } catch (FinderException e) {
                log.debug("Error in find contact person " + addressId + "-" + contactPersonId, e);
            }
        }
        return contactPerson;
    }

}
