package com.piramide.elwis.domain.common.sequence;

import javax.ejb.EJBLocalObject;

public interface Sequence extends EJBLocalObject {
    int getValueAfterIncrementingBy(int blockSize);

    String getName();

    void setName(String name);

    int getIndex();

    void setIndex(int index);

    Integer getNextValue();
}
