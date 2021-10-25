package com.piramide.elwis.web.webmail.form;

/**
 * AlfaCentauro Team
 * <p/>
 * This class helps to the view of Add AddressGroup
 *
 * @author Alvaro
 * @version $Id: AddressGroupForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class AddressGroupForm extends SearchMailAddressForm {
    private Object selectedEmails;

    public Object getSelectedEmails() {
        return this.selectedEmails;
    }

    public void setSelectedEmails(Object selectedMails) {
        if (selectedMails != null) {
            this.selectedEmails = selectedMails;
        } else {
            this.selectedEmails = "";
        }
    }
}
