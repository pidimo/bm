package com.piramide.elwis.domain.common.sequence;

import javax.ejb.*;

public abstract class SequenceBean implements EntityBean {
    EntityContext entityContext;

    public String ejbCreate(String name, int blockSize) throws CreateException {
        setName(name);
        setIndex(blockSize);
        return name;
    }

    public void ejbPostCreate(String name, int blockSize) throws CreateException {
    }

    public int getValueAfterIncrementingBy(int blockSize) {

        if (this.getIndex() % blockSize == 0) {
            this.setIndex(this.getIndex() + blockSize);
        } else {
            this.setIndex(this.getIndex() + (blockSize - (this.getIndex() % blockSize)));
        }
        return this.getIndex();
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract String getName();

    public abstract void setName(String name);

    public abstract int getIndex();

    public abstract void setIndex(int index);

    public Integer getNextValue() {
        final int i = getIndex();
        setIndex(i + 1);
        return i;
    }
}
