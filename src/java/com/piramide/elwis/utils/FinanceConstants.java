package com.piramide.elwis.utils;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class FinanceConstants {
    /*Table names*/
    public static final String TABLE_FREETEXT = "freetext";
    public static final String TABLE_INVOICE = "invoice";
    public static final String TABLE_INCOMINGINVOICE = "incominginvoice";
    public static final String TABLE_INCOMINGPAYMENT = "incomingpayment";
    public static final String TABLE_INVOICEPOSITION = "invoiceposition";
    public static final String TABLE_INVOICEVAT = "invoicevat";
    public static final String TABLE_INVOICEPAYMENT = "invoicepayment";
    public static final String TABLE_INVOICEREMINDER = "invoicereminder";
    public static final String TABLE_INVOICEFREENUMBER = "invoicefreenum";
    public static final String TABLE_INVOICETEMPLATE = "invoicetemplate";
    public static final String TABLE_INVOICETEXT = "invoicetext";
    public static final String TABLE_SEQUENCERULE = "sequencerule";
    public static final String TABLE_REMINDERLEVEL = "reminderlevel";
    public static final String TABLE_REMINDERTEXT = "remindertext";

    /*Jndi names*/
    public static final String JNDI_FREETEXT = "Elwis.FinanceFreeText";
    public static final String JNDI_INVOICETEMPLATE = "Elwis.InvoiceTemplate";
    public static final String JNDI_INVOICETEXT = "Elwis.InvoiceText";
    public static final String JNDI_INVOICE = "Elwis.Invoice";
    public static final String JNDI_INCOMINGINVOICE = "Elwis.IncomingInvoice";
    public static final String JNDI_INCOMINGPAYMENT = "Elwis.IncomingPayment";
    public static final String JNDI_INVOICEPOSITION = "Elwis.InvoicePosition";
    public static final String JNDI_INVOICEVAT = "Elwis.InvoiceVat";
    public static final String JNDI_INVOICEPAYMENT = "Elwis.InvoicePayment";
    public static final String JNDI_INVOICEREMINDER = "Elwis.InvoiceReminder";
    public static final String JNDI_INVOICEFREENUMBER = "Elwis.InvoiceFreeNumber";
    public static final String JNDI_SEQUENCERULE = "Elwis.SequenceRule";
    public static final String JNDI_REMINDERLEVEL = "Elwis.ReminderLevel";
    public static final String JNDI_REMINDERTEXT = "Elwis.ReminderText";

    public static final String JNDI_INVOICEPOSITION_BATCH_SERVICE = "Elwis.InvoicePositionBatchService";

    /**
     * Constants Sequence Rule Types
     */
    public static enum SequenceRuleType {
        VOUCHER(1),
        CUSTOMER(2),
        ARTICLE(3),
        SUPPORT_CASE(4),
        PRODUCT_CONTRACT_NUMBER(5),
        ACTION_TYPE(6);

        private int constant;

        SequenceRuleType(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return this.constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }
    }

    /**
     * Constants for Sequence Rule reset types
     * Daily = 1
     * Monthly = 2
     * Yearly = 3
     * NoReset =4
     */
    public static enum SequenceRuleResetType {
        Daily(1),
        Monthly(2),
        Yearly(3),
        NoReset(4);
        private int constant;

        SequenceRuleResetType(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return this.constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }

    }

    /**
     * Constants for Invoice Types
     * Invoice = 1
     * CreditNote = 2
     */
    public static enum InvoiceType {
        Invoice(1),
        CreditNote(2);

        private int constant;

        InvoiceType(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }
    }

    public static enum NetGrossFLag {
        NET(1),
        GROSS(2);

        private int constant;

        NetGrossFLag(int constant) {
            this.constant = constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }
    }
}
