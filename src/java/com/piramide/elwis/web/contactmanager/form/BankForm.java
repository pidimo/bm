package com.piramide.elwis.web.contactmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Mar 28, 2005
 * Time: 4:19:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankForm extends DefaultForm {

    private Log log = LogFactory.getLog(com.piramide.elwis.web.campaignmanager.form.CampaignForm.class);

    private String bankId;
    private String bankAccountId;
    private String bankName;
    private String bankCode;
    private String bankLabel;
    private String bankInternationalCode;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankLabel() {
        return bankLabel;
    }

    public void setBankLabel(String bankLabel) {
        this.bankLabel = bankLabel;
    }

    public String getBankInternationalCode() {
        return bankInternationalCode;
    }

    public void setBankInternationalCode(String bankInternationalCode) {
        this.bankInternationalCode = bankInternationalCode;
    }


    private Map dto = new HashMap();

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public void setDto(String s, Object o) {
        dto.put(s, o);
    }

    public Object getDto(String s) {
        return dto.get(s);
    }
}
