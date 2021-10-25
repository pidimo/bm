package com.piramide.elwis.cmd.dashboard;

import com.piramide.elwis.domain.dashboard.*;
import com.piramide.elwis.utils.DashboardConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 9:48:53 AM
 */
public class DashboardConfigurationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        if ("read".equals(this.getOp())) {
            read();
        }
        if ("save".equals(this.getOp())) {
            save();
        }
        if ("deleteComponent".equals(this.getOp())) {
            deleteComponent();
        }
        if ("deleteContainer".equals(this.getOp())) {
            deleteDashboardContainer();
        }
    }

    public boolean isStateful() {
        return false;
    }


    /**
     * This method reads information that exists in the container, creating one structure List of Map Objects
     * that contain all information  over componnets assigned to container.
     * <p/>
     * This method requires the identifier of containter "dashboardContainerId" in the ParamDTO object
     * and the result structure is returned in ResultDTO object as key "structure"
     */
    private void read() {
        //read dashboardContainerId from paramDTO object
        Integer id = new Integer(paramDTO.get("dashboardContainerId").toString());
        DashboardContainerHome home = (DashboardContainerHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_DASHBOARDCONTAINER);
        DashboardContainer container = null;

        //read the container from data base
        try {
            container = home.findByPrimaryKey(id);
        } catch (FinderException fe) {
            fe.printStackTrace();
        }

        //reads all components assigned to containder
        Collection adminComponents = new ArrayList();
        AdminComponentHome admHome = (AdminComponentHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_ADMINCOMPONENT);
        if (null != container) {
            try {
                adminComponents = admHome.findByContainerId(container.getDashboardContainerId());
            } catch (FinderException fe) {
                fe.printStackTrace();
            }
        }

        //create strutcture  that contains all information over components of the container
        List<Map> structure = new ArrayList<Map>();
        for (Iterator iterator = adminComponents.iterator(); iterator.hasNext();) {
            Map m = new HashMap();
            AdminComponent adminComponent = (AdminComponent) iterator.next();
            m.put("componentId", adminComponent.getComponentId());
            m.put("xmlComponentId", adminComponent.getXmlComponentId());
            m.put("rowX", adminComponent.getRowX());
            m.put("columnY", adminComponent.getColumnY());
            structure.add(m);
        }

        //setting up the structure in resultDTO object with the key "structure"
        resultDTO.put("structure", structure);
    }


    /**
     * This method is the one in charge to keep the information corresponding to the distribution from the components
     * in the container, to make his work it requires that paramDTO is in the object the identifier
     * of the container, under the key ?dashboardContainerId?; in addition to the identifiers of the components selected
     * in the columns of left and right under the following keys ?left? and ?right? respectively.
     * its work makes it of the following way:
     * <p/>
     * 1ro. it looks for the object container
     * 2do. it looks for the associated AdminComponent objects the container
     * 3ro. component eliminates the objects that were eliminated of anyone of the two columns of the View
     * 4to. it updates the positions of the components that even are in anyone of the two columns of the View
     * 5to. it adds the new components that added to anyone of the columns in the View
     */
    private void save() {
        //finds the container
        Integer id = new Integer(paramDTO.get("dashboardContainerId").toString());
        DashboardContainerHome home = (DashboardContainerHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_DASHBOARDCONTAINER);
        DashboardContainer container = null;
        try {
            container = home.findByPrimaryKey(id);
        } catch (FinderException fe) {
            fe.printStackTrace();
        }

        //identifies the right and left components
        List<String> left = (List<String>) paramDTO.get("left");
        List<String> right = (List<String>) paramDTO.get("right");

        // search the components associated to container
        AdminComponentHome admHome = (AdminComponentHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_ADMINCOMPONENT);
        Collection adminComponents = new ArrayList();
        try {
            adminComponents = admHome.findByContainerId(id);

            //delete all components htah are not selected in the view
            removeOldComponents(adminComponents, left, right);
            adminComponents = admHome.findByContainerId(id);
        } catch (FinderException fe) {
            fe.printStackTrace();
        }

        //update position components
        List<Map> newComponents = new ArrayList<Map>();

        int col = 1;
        int row = 1;
        for (String s : left) {
            int cId = new Integer(s);
            Map m = updatePosition(cId, adminComponents, row, col);
            if (!m.isEmpty()) {
                newComponents.add(m);
            }
            row++;
        }

        col = 2;
        row = 1;
        for (String s : right) {
            int cId = new Integer(s);
            Map m = updatePosition(cId, adminComponents, row, col);
            if (!m.isEmpty()) {
                newComponents.add(m);
            }
            row++;
        }

        //add the new components
        addNewComponents(newComponents, container);

    }

    private void deleteComponent() {
        int dbComponentId = new Integer(paramDTO.get("dbComponentId").toString());
        int xmlComponentId = new Integer(paramDTO.get("xmlComponenetId").toString());
        int dashboardContainerId = new Integer(paramDTO.get("dashboardContainerId").toString());

        AdminComponentHome admH = (AdminComponentHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_ADMINCOMPONENT);

        AdminComponentPK pk = new AdminComponentPK();
        pk.componentId = dbComponentId;
        pk.containerId = dashboardContainerId;
        AdminComponent deleteElement = null;
        try {
            deleteElement = admH.findByPrimaryKey(pk);
        } catch (FinderException fe) {
            fe.printStackTrace();
        }


        if (null != deleteElement) {
            Collection orderableElements = new ArrayList();
            try {
                orderableElements = admH.findByColumnY(dbComponentId, deleteElement.getColumnY());
            } catch (FinderException fe) {
                fe.printStackTrace();
            }

            for (Iterator it = orderableElements.iterator(); it.hasNext();) {
                AdminComponent c = (AdminComponent) it.next();
                if (c.getRowX() > deleteElement.getRowX()) {
                    int nIdx = c.getRowX() - 1;
                    c.setRowX(nIdx);
                }
            }

            try {
                removeColumns(deleteElement.getComponent().getColumns());
                removeFilters(deleteElement.getComponent().getFilters());
                Component c = deleteElement.getComponent();
                deleteElement.remove();
                c.remove();
            } catch (RemoveException re) {
                re.printStackTrace();
            }
        }
    }

    private void addNewComponents(List<Map> l, DashboardContainer container) {

        ComponentHome h = (ComponentHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_COMPONENT);

        AdminComponentHome admH = (AdminComponentHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_ADMINCOMPONENT);
        Collection adminConponents = new ArrayList();
        for (Map m : l) {
            int id = (Integer) m.get("id");
            int row = new Integer(m.get("row").toString());
            int column = new Integer(m.get("column").toString());
            Component c = null;
            try {
                c = h.create();
                c.setXmlComponentId(id);
                c.setCompanyId(container.getCompanyId());
            } catch (CreateException ce) {
                ce.printStackTrace();
            }

            if (null != c) {
                try {
                    AdminComponent admC = admH.create(container.getDashboardContainerId(),
                            c.getComponentId());
                    admC.setXmlComponentId(id);
                    admC.setColumnY(column);
                    admC.setRowX(row);
                    admC.setComponent(c);
                    admC.setCompanyid(container.getCompanyId());
                    adminConponents.add(admC);
                } catch (CreateException ce) {
                    ce.printStackTrace();
                }
            }

        }
        if (!adminConponents.isEmpty()) {
            container.getAdminComponents().addAll(adminConponents);
        }
    }

    private void removeOldComponents(Collection adminComponents, List<String> leftIds, List<String> rigthIds) {
        List<String> allIds = new ArrayList<String>();
        allIds.addAll(leftIds);
        allIds.addAll(rigthIds);

        Iterator it = adminComponents.iterator();

        List removeElements = new ArrayList();

        while (it.hasNext()) {
            AdminComponent adminComponent = (AdminComponent) it.next();
            if (!allIds.contains(adminComponent.getXmlComponentId().toString())) {
                removeElements.add(adminComponent);
            }
        }

        for (int i = 0; i < removeElements.size(); i++) {
            AdminComponent c = (AdminComponent) removeElements.get(i);
            try {
                removeColumns(c.getComponent().getColumns());
                removeFilters(c.getComponent().getFilters());
                Component myC = c.getComponent();
                c.remove();
                myC.remove();
            } catch (RemoveException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeColumns(Collection columns) {
        List myColumns = new ArrayList(columns);
        for (int i = 0; i < myColumns.size(); i++) {
            ComponentColumn c = (ComponentColumn) myColumns.get(i);
            try {
                c.remove();
            } catch (RemoveException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeFilters(Collection f) {
        List myF = new ArrayList(f);
        for (int i = 0; i < myF.size(); i++) {
            Filter ff = (Filter) myF.get(i);
            try {
                ff.remove();
            } catch (RemoveException e) {
                e.printStackTrace();
            }
        }
    }

    private Map updatePosition(int id, Collection adminComponents, int row, int column) {
        Map ls = new HashMap();
        boolean exists = false;
        for (Iterator iterator = adminComponents.iterator(); iterator.hasNext();) {
            AdminComponent adminComponent = (AdminComponent) iterator.next();

            if (id == adminComponent.getXmlComponentId()) {
                adminComponent.setColumnY(column);
                adminComponent.setRowX(row);
                exists = true;
            }
        }

        if (!exists) {
            ls.put("id", id);
            ls.put("row", row);
            ls.put("column", column);
        }

        return ls;
    }

    private void deleteDashboardContainer() {
        int userId = new Integer(paramDTO.get("userId").toString());
        int companyId = new Integer(paramDTO.get("companyId").toString());

        DashboardContainerHome dbH =
                (DashboardContainerHome) EJBFactory.i.getEJBLocalHome(DashboardConstants.JNDI_DASHBOARDCONTAINER);
        DashboardContainer container = null;
        try {
            container = dbH.findByUserIdCompanyId(userId, companyId);
        } catch (FinderException e) {
            log.debug("Cannot find dashboardContainer Object for user " + userId + " in company " + companyId);
        }

        if (null != container) {

            if (null != container.getAdminComponents() && container.getAdminComponents().size() > 0) {
                List elements = new ArrayList(container.getAdminComponents());
                for (int i = 0; i < elements.size(); i++) {
                    AdminComponent element = (AdminComponent) elements.get(i);
                    Component c = element.getComponent();
                    removeColumns(element.getComponent().getColumns());
                    removeFilters(element.getComponent().getFilters());

                    try {
                        element.remove();
                        c.remove();
                    } catch (RemoveException re) {
                        re.printStackTrace();
                    }
                }
            }
            try {
                container.remove();
            } catch (RemoveException e) {
                log.error("Cannot remove dashboardContainer object ", e);
            }
        }
    }
}
