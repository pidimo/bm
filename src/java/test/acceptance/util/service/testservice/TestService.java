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

import javax.ejb.EJBLocalObject;

public interface TestService extends EJBLocalObject {

    public ResultDTO executeCommand(EJBCommand cmd) throws AppLevelException;
}
