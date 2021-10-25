package com.piramide.elwis.domain.common.sequence;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface SequenceHome extends EJBLocalHome {
    Sequence create(String name, int blockSize) throws CreateException;

    Sequence findByPrimaryKey(String name) throws FinderException;
}
