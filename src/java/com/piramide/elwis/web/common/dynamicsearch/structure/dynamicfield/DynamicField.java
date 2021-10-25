package com.piramide.elwis.web.common.dynamicsearch.structure.dynamicfield;

import com.piramide.elwis.web.common.dynamicsearch.util.ClassLoaderUtil;
import com.piramide.elwis.web.common.dynamicsearch.util.ObjectInstanceException;

import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicField {
    private String loadfieldclass;
    private DynamicFieldLoader dynamicFieldLoader;

    private String joinFieldAlias;
    private String joinFieldAlias2;

    public DynamicField(String loadfieldclass) throws ObjectInstanceException {
        this.loadfieldclass = loadfieldclass;
        dynamicFieldLoader = (DynamicFieldLoader) ClassLoaderUtil.newInstance(loadfieldclass);
    }

    public String getLoadfieldclass() {
        return loadfieldclass;
    }

    public void setLoadfieldclass(String loadfieldclass) {
        this.loadfieldclass = loadfieldclass;
    }

    public List<CategoryField> getCategoryFields(Map params) {
        params.put("joinFieldAlias", joinFieldAlias);
        params.put("joinFieldAlias2", joinFieldAlias2);

        return dynamicFieldLoader.getCategoryFields(params);
    }

    public String getJoinFieldAlias() {
        return joinFieldAlias;
    }

    public void setJoinFieldAlias(String joinFieldAlias) {
        this.joinFieldAlias = joinFieldAlias;
    }

    public String getJoinFieldAlias2() {
        return joinFieldAlias2;
    }

    public void setJoinFieldAlias2(String joinFieldAlias2) {
        this.joinFieldAlias2 = joinFieldAlias2;
    }
}
