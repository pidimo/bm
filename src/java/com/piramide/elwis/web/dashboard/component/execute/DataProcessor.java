package com.piramide.elwis.web.dashboard.component.execute;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter;
import com.piramide.elwis.web.dashboard.component.web.struts.action.DrawComponentAction;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 */
public abstract class DataProcessor {
    /**
     * This method implements data access for read the content of
     * <code>com.piramide.elwis.web.dashboard.component.configuration.structure.Component</code> object .
     *
     * @param cloneComponent   component can read contain
     * @param searchParameters some search parameters for read the contain
     * @param action           <code>DrawComponentAction</code> object
     * @param request          <code>HttpServletRequest</code> object
     */
    public abstract void readComponentContain(Component cloneComponent,
                                              Map searchParameters,
                                              DrawComponentAction action,
                                              HttpServletRequest request);

    /**
     * This method implements data access for read the contain of
     * <code>com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter</code> object,
     * when this object have default values can read from data base
     * query
     *
     * @param componentId  component identifier
     * @param filter       filter that contain query values
     * @param elParameters
     * @param parameters
     */
    public abstract void readFilterContain(int componentId,
                                           ConfigurableFilter filter,
                                           Map elParameters,
                                           Map parameters);

    /**
     * This method returns the contain of component
     *
     * @return <code>List></code> of <code>Map</code> Object that represents the contain of
     *         <code>com.piramide.elwis.web.dashboard.component.configuration.structure.Component</code> object.
     */
    public abstract List<Map> getData();

    /**
     * This method returns the number of rows has be exists in data base
     *
     * @return int that represents number of rows
     */
    public abstract int getNumberOfAllElements();

}
