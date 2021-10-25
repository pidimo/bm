package com.piramide.elwis.web.reports.filterers;

import com.jatun.titus.listgenerator.engine.TitusOperator;
import com.jatun.titus.listgenerator.engine.fantabulous.CustomCondition;
import org.alfacentauro.fantabulous.structure.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * create custom condition to BLOB data type, using field function to management
 *
 * @author Miky
 * @version $Id: BlobReadFilterer.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class BlobReadFilterer implements CustomCondition {
    public Condition processTitusFilter(Field field, List list, Map map, TitusOperator titusOperator) {

        //create fantabulous field function to process this data type BLOB
        Function function = new Function();
        String functionClass = "com.piramide.elwis.utils.dbfunction.BlobReadFunction";
        function.setClazz(functionClass);

        //function parameters
        FunctionParameter functionParameter = new FunctionParameter();
        functionParameter.setType("field");
        functionParameter.setField(field);

        List params = new ArrayList();
        params.add(functionParameter);
        function.setParameters(params);

        SimpleCondition simpleCondition = new SimpleCondition();
        simpleCondition.setField1(function);
        simpleCondition.setOperator(titusOperator.getFantaOperator(0).getName());
        if (!list.isEmpty()) {
            simpleCondition.setValue((String) list.get(0));
        }

        return simpleCondition;
    }

    public Condition getConditionForChart(Field field, Map map) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
