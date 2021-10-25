/**
 * Jatun S.R.L.
 *
 * @author Alvaro Sejas 
 * @version
 */
package test.acceptance.util.service.testservice;

import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

public class TestServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(this.getClass());
    private SessionContext ctx;

    public TestServiceBean() {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        this.ctx = sessionContext;
    }

    public void ejbCreate() throws CreateException {
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    /**
     * This method executed an EJBCommand, this because the command wants a SessionContext instance, and the only
     * that has the instance is the sessionbean
     *
     * @param cmd EJBCommand
     * @throws AppLevelException
     */
    public ResultDTO executeCommand(EJBCommand cmd) throws AppLevelException {
        log.debug("Executing command: " + cmd + " parameters: " + cmd.getParamDTO());
        cmd.executeInStateless(ctx);
        return (cmd.getResultDTO());
    }
}
