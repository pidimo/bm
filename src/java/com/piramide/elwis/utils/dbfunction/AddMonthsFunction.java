package com.piramide.elwis.utils.dbfunction;

import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenaratorUtil;
import org.alfacentauro.fantabulous.sqlgenerator.informixgenerator.InformixUserFunction;
import org.alfacentauro.fantabulous.sqlgenerator.mysqlgenerator.MySqlUserFunction;
import org.alfacentauro.fantabulous.structure.Function;

/**
 * Jatun S.R.L.
 * Compose sql to add months in an date represented as integer:
 * (paystartdate + (TRUNC(addMonth/12,0)*10000)+(MOD(addMonth,12)*100))
 * <p/>
 * This is to add filter in Partial periodic payment method in contract to invoice report.
 * This can be used as : (paystartdate + (TRUNC(13/12,0)*10000)+(MOD(13,12)*100)) <= 20300201
 *
 * @author Miky
 * @version $Id: AddMonthsFunction.java 04-feb-2009 14:42:58 $
 */
public class AddMonthsFunction implements InformixUserFunction, MySqlUserFunction {

    public String writeInformixUserFunction(Function function, Parameters parameters) {
        return composeSQL(function, parameters, "MOD", "TRUNC");
    }

    public String writeMysqlUserFunction(Function function, Parameters parameters) {
        return composeSQL(function, parameters, "MOD", "TRUNCATE");
    }

    /**
     * sql to add month:
     * (paystartdate + (TRUNC(addMonth/12,0)*10000)+(MOD(addMonth,12)*100))
     *
     * @param function
     * @param parameters
     * @param MODFunct
     * @param TRUNCATEFunct
     * @return String
     */
    private String composeSQL(Function function, Parameters parameters, String MODFunct, String TRUNCATEFunct) {
        String monthsToAdd = getAddMonth(function, parameters);
        String payStartDate = SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters);

        String str = "(" + payStartDate + "+(" + TRUNCATEFunct + "(" + monthsToAdd + "/12,0)*10000) + (" + MODFunct + "(" + monthsToAdd + ",12)*100))";
        return str;
    }

    /**
     * sql to calculate the months to add:
     * ((installment-1) * payperiod)
     *
     * @param function
     * @param parameters
     * @return
     */
    private String getAddMonth(Function function, Parameters parameters) {
        String installment = SqlGenaratorUtil.writeFunctionParameter(function, 1, parameters);
        String payPeriod = SqlGenaratorUtil.writeFunctionParameter(function, 2, parameters);
        StringBuffer buffer = new StringBuffer();
        buffer.append("((").append(installment).append("-1)*").append(payPeriod).append(")");
        return buffer.toString();
    }
}
