package com.piramide.elwis.utils.dbfunction;

import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenaratorUtil;
import org.alfacentauro.fantabulous.sqlgenerator.informixgenerator.InformixUserFunction;
import org.alfacentauro.fantabulous.sqlgenerator.mysqlgenerator.MySqlUserFunction;
import org.alfacentauro.fantabulous.structure.Function;

import java.util.Calendar;

/**
 * @author: ivan
 * Date: 24-11-2006: 03:49:09 PM
 */
public class AgeReadFunction implements InformixUserFunction, MySqlUserFunction {
    private String fn = "minus";
    private Calendar c = Calendar.getInstance();

    public String writeInformixUserFunction(Function function, Parameters parameters) {
        int year = c.get(Calendar.YEAR);
        String r = fn + "(" + year + "," + "trunc(divide(" + SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters) + ",10000)))";
        return r;
    }

    public String writeMysqlUserFunction(Function function, Parameters parameters) {
        int year = c.get(Calendar.YEAR);

        String r = "(" + year + " - " + SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters) + " div 10000)";
        return r;
    }

}
