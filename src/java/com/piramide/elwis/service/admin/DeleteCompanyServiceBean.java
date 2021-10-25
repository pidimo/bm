/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.service.admin;

import com.piramide.elwis.cmd.admin.dropcompany.SQLGenerator;
import com.piramide.elwis.utils.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class DeleteCompanyServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(DeleteCompanyServiceBean.class);

    private SessionContext ctx;

    public DeleteCompanyServiceBean() {
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        this.ctx = sessionContext;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    //business methods

    public Boolean initializeSQLGenerator() {
        Context context = null;
        boolean result = true;
        try {
            context = new javax.naming.InitialContext();
            DataSource dataSource = (DataSource) context.lookup(Constants.JNDI_ELWISDS);
            Connection connection = dataSource.getConnection();

            DatabaseMetaData dbmdt = connection.getMetaData();

            ResultSet rsTables = dbmdt.getTables(null, null, "%", null);
            SQLGenerator.i.initialize(rsTables, dbmdt);

            connection.close();
        } catch (NamingException e) {
            log.debug("-> Initialize SQL Generator FAIL", e);
            result = false;
        } catch (SQLException e) {
            log.debug("-> Initialize SQL Generator FAIL", e);
            result = false;
        }

        return result;
    }


    public Boolean deleteCompany(Integer companyId) {
        Context context;
        String sql = "";

        try {
            context = new javax.naming.InitialContext();
            DataSource dataSource = (DataSource) context.lookup(Constants.JNDI_ELWISDS);
            Connection connection = dataSource.getConnection();

            DatabaseMetaData dbmdt = connection.getMetaData();

            ResultSet rsTables = dbmdt.getTables(null, null, "%", null);
            String mainSQL = SQLGenerator.i.buildSQL(companyId, rsTables, dbmdt);

            List<String> sqlList = Arrays.asList(mainSQL.split(";"));
            for (String element : sqlList) {
                if ("".equals(element.trim())) {
                    continue;
                }

                sql = element;
                Statement statement = connection.createStatement();
                statement.execute(element);
                statement.close();
                log.debug("-> Execute " + sql + " OK");
            }
            connection.close();
            log.debug("-> Deleted Company companyId=" + companyId + " OK");
            return true;
        } catch (NamingException e) {
            log.error("-> Execute " + sql + " FAIL, companyId=" + companyId, e);
            ctx.setRollbackOnly();
        } catch (SQLException e) {
            log.error("-> Execute " + sql + " FAIL, companyId=" + companyId, e);
            ctx.setRollbackOnly();
        }
        return false;
    }
}
