/**
 * Jatun S.R.L.
 *
 * @author Alvaro Sejas 
 * @version
 */
package test.acceptance.util.service.testservice;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface TestServiceHome extends EJBLocalHome {
    TestService create() throws CreateException;
}
