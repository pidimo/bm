package test.acceptance;

import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import net.java.dev.strutsejb.AppLevelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import test.acceptance.base.BaseTest;

import javax.naming.NamingException;
import java.util.HashMap;

/**
 * Jatun S.R.L.
 * This class implements a sample test
 *
 * @author Alvaro Sejas
 */
public class SampleTest extends BaseTest {

    Log log = LogFactory.getLog(this.getClass());

    protected void setUp() throws Exception {
        log.debug("Executing baseTest.......");
        super.setUp();
    }

    public String testSample(String userName) throws NamingException, AppLevelException {
        log.debug("Sample testing.....");
        LightlyAddressCmd lightlyAddressCmd = new LightlyAddressCmd();
        HashMap parameters = new HashMap();
        parameters.put("addressId", 12);
        super.executeCommand(lightlyAddressCmd, parameters, "read");
        return ("Hi!, " + userName);
    }
}
