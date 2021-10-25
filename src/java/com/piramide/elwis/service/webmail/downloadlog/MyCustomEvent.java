package com.piramide.elwis.service.webmail.downloadlog;

import java.util.EventObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class MyCustomEvent extends EventObject {
    private static final long serialVersionUID = 7383182229898306240L;

    final private Object source;
    final private String sourceId;
    final private Long millisKey;
    final private Integer dataId;

    public MyCustomEvent(Object source, String sourceId, Long millisKey, Integer dataId) {
        super(source);
        this.source = source;
        this.sourceId = sourceId;
        this.millisKey = millisKey;
        this.dataId = dataId;
    }

    public Object getSource() {
        return this.source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public Long getMillisKey() {
        return millisKey;
    }

    public Integer getDataId() {
        return dataId;
    }
}