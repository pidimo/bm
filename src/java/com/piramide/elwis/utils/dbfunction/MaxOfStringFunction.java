package com.piramide.elwis.utils.dbfunction;

import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenaratorUtil;
import org.alfacentauro.fantabulous.sqlgenerator.informixgenerator.InformixUserFunction;
import org.alfacentauro.fantabulous.sqlgenerator.mysqlgenerator.MySqlUserFunction;
import org.alfacentauro.fantabulous.structure.Function;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class MaxOfStringFunction implements InformixUserFunction, MySqlUserFunction {

    public String writeInformixUserFunction(Function function, Parameters parameters) {
        return new StringBuilder().append("max(cast(").
                append(SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters)).
                append(" as integer))").toString();
    }

    public String writeMysqlUserFunction(Function function, Parameters parameters) {
        return new StringBuilder().append("max(cast(").
                append(SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters)).
                append(" as UNSIGNED))").toString();
    }
}
