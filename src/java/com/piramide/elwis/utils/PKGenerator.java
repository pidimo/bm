package com.piramide.elwis.utils;

import com.piramide.elwis.domain.common.sequence.Sequence;
import com.piramide.elwis.domain.common.sequence.SequenceHome;
import net.java.dev.strutsejb.ServiceLocator;
import net.java.dev.strutsejb.SysLevelException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

/**
 * Entity sequence generator util
 * and obtain a next sequence.
 *
 * @author Fernando
 * @version 2.0
 */
public class PKGenerator {

    public static final PKGenerator i = new PKGenerator();

    private PKGenerator() {
    }


    /**
     * Return an Integer that represents the next element in sequence of numbers for an entity
     *
     * @param sequenceId the table name which is the primary key
     * @return the next number
     */
    public Integer nextKey(String sequenceId) {

        //get Sequence bean
        Sequence seq;
        final SequenceHome sh = getSequenceHome();
        try {
            seq = sh.findByPrimaryKey(sequenceId);

        } catch (FinderException e1) {
            //if there's no Sequence yet, create it
            seq = createSequence(sequenceId, sh);
        }
        return seq.getNextValue();
    }


    private SequenceHome getSequenceHome() {

        try {
            return (SequenceHome) ServiceLocator.i.lookup(Constants.JNDI_SEQUENCE);

        } catch (NamingException e) {
            throw new SysLevelException(e);
        }
    }

    /**
     * Creates a new sequence for the given table id.
     *
     * @param sequenceId the table id
     * @param sh         the sequence home interface.
     * @return the Sequence instance.
     */
    private Sequence createSequence(String sequenceId, SequenceHome sh) {
        try {
            return sh.create(sequenceId, 1);
        } catch (CreateException e) {
            throw new SysLevelException(e);
        }
    }

}
