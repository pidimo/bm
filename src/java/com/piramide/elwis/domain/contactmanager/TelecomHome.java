package com.piramide.elwis.domain.contactmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Telecom entity home interface
 *
 * @author Ernesto
 * @version $Id: TelecomHome.java 9864 2009-11-16 20:55:15Z miguel $
 */
public interface TelecomHome extends EJBLocalHome {

    Telecom create(Integer addressId, Integer contactPersonId, String data, String description, Boolean predetermined,
                   Integer telecomTypeId, Integer companyId) throws CreateException;

    /**
     * below are valid finders
     */
    Telecom findByPrimaryKey(Integer key) throws FinderException;

    Collection findAddressTelecoms(Integer addressId) throws FinderException;

    Collection findContactPersonTelecoms(Integer addressId, Integer contactPersonId) throws FinderException;

    Collection findTelecomsWithTelecomNumber(String telecomnumber, Integer companyId) throws FinderException;

    //finder used for validation, this looks for a telecoms excluding itself with  given telecomid.  
    Collection findTelecomsWithTelecomNumberWithoutTelecomId(String telecomnumber, Integer telecomId, Integer companyId) throws FinderException;


    Collection findAllContactPersonDefaultTelecoms(Integer addressId, Integer contactPersonId) throws FinderException;

    Collection findAllAddressDefaultTelecoms(Integer addressId) throws FinderException;

    Telecom findContactPersonDefaultTelecomsByTypeId(Integer addressId, Integer contactPersonId, Integer typeId) throws FinderException;

    Telecom findAddressDefaultTelecomsByTypeId(Integer addressId, Integer typeId) throws FinderException;

    Collection findAllContactPersonTelecomsByTypeId(Integer addressId, Integer contactPersonId, Integer typeId) throws FinderException;

    Collection findAllAddressTelecomsByTypeId(Integer addressId, Integer typeId) throws FinderException;


    Collection findAddressTelecomsByTelecomTypeType(Integer addressId, String telecomTypeType) throws FinderException;

    Collection findContactPersonTelecomsByTelecomTypeType(Integer addressId, Integer contactPersonId, String telecomTypeType) throws FinderException;

    Collection findAllContactPersonTelecomsOfAddress(String telecomnumber, Integer addressId) throws FinderException;
}
