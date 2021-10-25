package com.piramide.elwis.web.dashboard.component.persistence;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : ivan
 */
public abstract class AbstractDao {
    /**
     * In this method the logic related to the reading of the configuration of a component is implemented,
     * An <code>Component</code> object is passed as parameter, this object is processed in the reading logic;
     * and eliminated columns that are not used, the values of the filters and the orderable columns are updated.
     *
     * @param cloneComponent <code>Component</code> clon object that contains default values (columns, filters)
     *                       and some component information
     * @param map            <code>Map</code> object can contain some information required for read information of database
     *                       eg. dbComponentId
     * @param request        <code>HttpServletRequest</code> object for execute commands
     */
    public abstract void readComponentFromDataBase(Component cloneComponent,
                                                   Map map,
                                                   HttpServletRequest request);

    /**
     * In this method the logic related to the save of component information is implemented,
     * An <code>Component</code> object is passed as parameter, this object contain all columns and filters can
     * be save information in data base
     *
     * @param cloneComponent         <code>Component</code> clon object that contains saved values (columns, filters)
     * @param dataBaseReadParameters <code>Map</code> object can contain some information required for read
     *                               information of database
     * @param request                <code>HttpServletRequest</code> object for execute commands
     */

    public abstract void saveComponentConfiguration(Component cloneComponent,
                                                    Map dataBaseReadParameters,
                                                    HttpServletRequest request);
}
