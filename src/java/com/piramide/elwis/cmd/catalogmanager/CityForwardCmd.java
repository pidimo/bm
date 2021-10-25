package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.City;
import com.piramide.elwis.dto.catalogmanager.CityDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;

import javax.ejb.SessionContext;


/**
 * @author Yumi
 * @version $Id: CityForwardCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */

public class CityForwardCmd extends EJBCommand {

    public void executeInStateless(SessionContext ctx) {
        try {
            CityDTO dto = new CityDTO(paramDTO);
            CRUDDirector.i.doCRUD(getOp(), dto, resultDTO);
            final City city = (City) EJBFactory.i.findEJB(dto);
            final Integer id = city.getCountry().getCountryId();
            resultDTO.put("countryId", id);
        } catch (NumberFormatException e) {
        }
    }

    public boolean isStateful() {
        return false;
    }
}