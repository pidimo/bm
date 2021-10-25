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
public class ConcatName1Name2Name3 implements InformixUserFunction, MySqlUserFunction {

    public String writeInformixUserFunction(Function function, Parameters parameters) {
        return new StringBuilder().append("(").
                append("lower(nvl(").
                append(SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters)).
                append(",'') || ").
                append("' ' || ").
                append("nvl(").
                append(SqlGenaratorUtil.writeFunctionParameter(function, 1, parameters)).
                append(",'') || ").
                append("' ' || ").
                append("nvl(").
                append(SqlGenaratorUtil.writeFunctionParameter(function, 2, parameters)).
                append(",'')) ").
                append(")").toString();
    }

    public String writeMysqlUserFunction(Function function, Parameters parameters) {
        return new StringBuilder().append("concat_ws('',").
                append(SqlGenaratorUtil.writeFunctionParameter(function, 0, parameters)).
                append(",").
                append("' ',").
                append(SqlGenaratorUtil.writeFunctionParameter(function, 1, parameters)).
                append(",").
                append("' ',").
                append(SqlGenaratorUtil.writeFunctionParameter(function, 2, parameters)).
                append(")").toString();
    }
}
