package com.piramide.elwis.utils.dbfunction;

import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenaratorUtil;
import org.alfacentauro.fantabulous.sqlgenerator.informixgenerator.InformixUserFunction;
import org.alfacentauro.fantabulous.sqlgenerator.mysqlgenerator.MySqlUserFunction;
import org.alfacentauro.fantabulous.structure.Function;


/**
 * Jatun S.R.L.
 * class to management blob2varchar db informix function
 *
 * @author Miky
 * @version $Id: BlobReadFunction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class BlobReadFunction implements InformixUserFunction, MySqlUserFunction {
    private String functionName = "blob2varchar";

    public String writeInformixUserFunction(Function function, Parameters parameters) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(functionName).append("(");
        buffer.append(SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters));
        buffer.append(")");

        return new String(buffer);
    }

    public String writeMysqlUserFunction(Function function, Parameters parameters) {
        return SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters);
    }
}
