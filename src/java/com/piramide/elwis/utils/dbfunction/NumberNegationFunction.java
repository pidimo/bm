package com.piramide.elwis.utils.dbfunction;

import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenaratorUtil;
import org.alfacentauro.fantabulous.sqlgenerator.informixgenerator.InformixUserFunction;
import org.alfacentauro.fantabulous.sqlgenerator.mysqlgenerator.MySqlUserFunction;
import org.alfacentauro.fantabulous.structure.Function;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class NumberNegationFunction implements InformixUserFunction, MySqlUserFunction {

    public String writeInformixUserFunction(Function function, Parameters parameters) {
        return new StringBuilder().append("(").
                append(SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters)).
                append(" * -1)").toString();
    }

    public String writeMysqlUserFunction(Function function, Parameters parameters) {
        return new StringBuilder().append("(").
                append(SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters)).
                append(" * -1)").toString();
    }
}
