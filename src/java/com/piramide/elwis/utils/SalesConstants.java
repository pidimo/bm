package com.piramide.elwis.utils;

/**
 * Sales process manager constant s
 */
public abstract class SalesConstants {
    /**
     * TABLE NAMES SECTIOn
     */
    public static final String TABLE_FREETEXT = "freetext";
    public static final String TABLE_STATUS = "processstatus";
    public static final String TABLE_PRIORITY = "processpriority";
    public static final String TABLE_ACTIONTYPE = "actiontype";
    public static final String TABLE_SALESPROCESS = "salesprocess";
    public static final String TABLE_ACTIONPOSITION = "actionposition";
    public static final String TABLE_ACTION = "action";
    public static final String TABLE_ACCOUNT = "account";
    public static final String TABLE_CONTRACTTYPE = "contracttype";
    public static final String TABLE_PRODUCTCONTRACT = "productcontract";
    public static final String TABLE_SALE = "sale";
    public static final String TABLE_PAYMENTSTEP = "paymentstep";
    public static final String TABLE_SALEPOSITION = "saleposition";
    public static final String TABLE_ACTIONTYPESEQUENCE = "actiontypeseq";


    /**
     * JNDI SECTION *
     */
    public static final String JNDI_STATUS = "Elwis.Status";
    public static final String JNDI_PRIORITY = "Elwis.SalesProcessPriority";
    public static final String JNDI_ACTIONTYPE = "Elwis.ActionType";
    public static final String JNDI_SALESPROCESS = "Elwis.SalesProcess";
    public static final String JNDI_ACTIONPOSITION = "Elwis.ActionPosition";
    public static final String JNDI_FREETEXT = "Elwis.SalesProcessFreeText";
    public static final String JNDI_ACTION = "Elwis.Action";
    public static final String JNDI_ACCOUNT = "Elwis.Account";
    public static final String JNDI_CONTRACTTYPE = "Elwis.ContractType";
    public static final String JNDI_PRODUCTCONTRACT = "Elwis.ProductContract";
    public static final String JNDI_SALE = "Elwis.Sale";
    public static final String JNDI_PAYMENTSTEP = "Elwis.PaymentStep";
    public static final String JNDI_SALEPOSITION = "Elwis.SalePosition";
    public static final String JNDI_ACTIONTYPESEQUENCE = "Elwis.ActionTypeSequence";

    public static final String JNDI_CONTRACTENDREMINDERSERVICE = "Elwis.ContractEndReminderService";

    public static final String SALES_SERVICE_RESOURCES = "com.piramide.elwis.service.sales.SalesServiceResources";

    public static final Integer DISCOUNT_PERCENT = new Integer(1);
    public static final Integer DISCOUNT_AMOUNT = new Integer(2);

    /*for  REPORTS*/
    public static final String SALESPROCESS_REPORT_LIST = "1";
    public static final String PROCESSACTION_REPORT_LIST = "2";
    public static final String ACTIONPOSITION_REPORT_LIST = "3";

    public static enum PayMethod {
        Single(0),
        Periodic(2),
        PartialPeriodic(1),
        PartialFixed(3),
        SingleWithoutContract(4);

        private int constant;

        PayMethod(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }

        public boolean equal(Integer value) {
            return this.constant == value;
        }

        public boolean equal(String value) {
            return getConstantAsString().equals(value);
        }
    }

    /*public static enum PayPeriod {
        Monthly(1, 1),
        Quarterly(3, 3),
        HalfYearly(6, 6),
        Yearly(12, 12);

        private int constant;
        private int unit;

        PayPeriod(int constant, int unit) {
            this.constant = constant;
            this.unit = unit;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }

        public int getUnit() {
            return unit;
        }

        public static PayPeriod getPayPeriod(int constat){
            for(PayPeriod payPeriod : values())
                if(payPeriod.getConstant() == constat)
                    return payPeriod;

            return null;
        }
    }*/

    public static enum MatchCalendarPeriod {
        YES(1),
        NO(0);

        private int constant;

        MatchCalendarPeriod(int constant) {
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

    /**
     * @deprecated replaced by MatchCalendarPeriod
     */
    public static enum InvoiceRemainOn {
        FirtsInvoice(0),
        LastInvoice(1);

        private int constant;

        InvoiceRemainOn(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }
    }


    public static enum AmounType {
        PERCENTAGE(1),
        AMOUNT(2);

        private int constant;

        AmounType(int constant) {
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

    public static enum ContractIncome {
        REAL_INCOME(1),
        VIRTUAL_INCOME(2);

        private int constant;

        ContractIncome(int constant) {
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

    public static final String INVOICE_NOT_REMINDERED_YET = "-1";

    public static final String REMINDER_GENERATED = "1";
    public static final String REMINDER_NOT_GENERATED = "0";

    public static final Integer MAX_INSTALLMENT = 100;

    public static final String CONTRACTS_TO_BE_INVOICED = "-1";
    public static final String CONTRACTS_NOT_TO_BE_INVOICED = "-2";

}
