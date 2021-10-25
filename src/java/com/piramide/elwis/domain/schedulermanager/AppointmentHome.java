package com.piramide.elwis.domain.schedulermanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 6, 2005
 * Time: 3:34:40 PM
 * To change this template use File | Settings | File Templates.
 */

public interface AppointmentHome extends EJBLocalHome {

    public Collection findAll() throws FinderException;

    Appointment findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAllAppointmentWithoutRecurrence(Long startDate, Long endDate, Integer userId) throws FinderException;

    public Collection findAllPublicAppointmentWithoutRecurrence(Long startDate, Long endDate, Integer userId) throws FinderException;

    public Collection findAllAppointmentWithRecurrence(Integer startDate, Integer userId) throws FinderException;

    public Collection findAllPublicAppointmentWithRecurrence(Integer startDate, Integer userId) throws FinderException;

    public Collection findAllPublicOrPrivateAppointmentWithRecurrence(Integer startDate, Integer userId, Boolean isPrivate) throws FinderException;

    public Collection findAllPublicOrPrivateAppointmentWithoutRecurrence(Long startDate, Long endDate, Integer userId, Boolean isPrivate) throws FinderException;


    public Collection findByAppointmentTypeWithoutRecurrence(Long startDate, Long endDate, Integer userId, Integer appointmentTypeId) throws FinderException;

    public Collection findByAppointmentTypeWithRecurrence(Integer startDate, Integer userId, Integer appointmentTypeId) throws FinderException;

    public Collection findByAppointmentTypePublicOrPrivateWithRecurrence(Integer startDate, Integer userId, Boolean isPrivate, Integer appointmentTypeId) throws FinderException;

    public Collection findByAppointmentTypePublicOrPrivateWithoutRecurrence(Long startDate, Long endDate, Integer userId, Boolean isPrivate, Integer appointmentTypeId) throws FinderException;


    public Collection findByAddress(Integer addressId) throws FinderException;

    public Collection findByContactPerson(Integer addressId, Integer contactPersonId) throws FinderException;


    public Appointment create(ComponentDTO dto) throws CreateException;
}
