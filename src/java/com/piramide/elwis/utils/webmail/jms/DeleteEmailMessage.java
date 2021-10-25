package com.piramide.elwis.utils.webmail.jms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class DeleteEmailMessage implements Serializable {
    private Integer userMailId;
    private Map<String, List<String>> accounts = new HashMap<String, List<String>>();

    public DeleteEmailMessage(Integer userMailId, Map<String, List<String>> accounts) {
        this.userMailId = userMailId;
        this.accounts = accounts;
    }

    public Integer getUserMailId() {
        return userMailId;
    }

    public Map<String, List<String>> getAccounts() {
        return accounts;
    }
}
