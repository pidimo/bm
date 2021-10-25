package test.acceptance.base;

import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.concordion.integration.junit3.ConcordionTestCase;
import test.acceptance.util.service.testservice.TestService;
import test.acceptance.util.service.testservice.TestServiceHome;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Map;
import java.util.Properties;

/**
 * Jatun S.R.L.
 * This is a base class for the tests
 *
 * @author Alvaro Sejas
 * @version 4.2.2
 */
public abstract class BaseTest extends ConcordionTestCase {
    Log log = LogFactory.getLog(this.getClass());

    protected Properties contextProperties = new Properties();

    /**
     * This method initialize some objects needed to run the tests
     *
     * @throws Exception Exeception
     */
    protected void setUp() throws Exception {
        log.debug("SetUp method executing.............................");
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");

        contextProperties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
        //Logging configuration
        contextProperties.put("log4j.appender.C", "org.apache.log4j.ConsoleAppender");
        contextProperties.put("log4j.appender.C.layout", "org.apache.log4j.SimpleLayout");

        //Test datasource
        contextProperties.put("burroDS", "new://Resource?type=DataSource");
        contextProperties.put("burroDS.JdbcDriver", "org.gjt.mm.mysql.Driver");
        contextProperties.put("burroDS.JdbcUrl", "jdbc:mysql://localhost/burro");
        contextProperties.put("burroDS.userName", "burro");
        contextProperties.put("burroDS.password", "burro");

        /*contextProperties.put("elwisDS", "new://Resource?type=DataSource");
        contextProperties.put("elwisDS.JdbcDriver", "org.gjt.mm.mysql.Driver");
        contextProperties.put("elwisDS.JdbcUrl", "jdbc:mysql://10.0.0.11:3308/elwis2?useUnicode=true&amp;characterEncoding=UTF-8");
        contextProperties.put("elwisDS.UserName", "jatundev");
        contextProperties.put("elwisDS.Password", "jdev.pwd");*/
        //JNDI configuration
        contextProperties.put("openejb.jndiname.format", "Elwis.{ejbName}");
        log.debug("Starting openJB Server.....");
    }

    /**
     * Method to execute commands, this is needed to execute a command, because in order to execute commands
     * the SessionContext is required.
     *
     * @param command    command
     * @param parameters parameters
     * @param op         Operation
     * @throws NamingException   Exception
     * @throws AppLevelException Exception
     */
    protected void executeCommand(EJBCommand command, Map parameters, String op) throws NamingException, AppLevelException {
        log.debug("Executing command................. " + command);

        if (op != null) {
            command.setOp(op);
        }
        if (parameters != null && parameters.size() > 0) {
            command.putParam(parameters);
        }
        InitialContext initialContext = new InitialContext(contextProperties);
        TestServiceHome testServiceHome = (TestServiceHome) initialContext.lookup("Elwis.TestService");
        try {
            TestService testService = testServiceHome.create();
            testService.executeCommand(command);
        } catch (CreateException e) {
            log.error("Error creating testService....");
        }
    }
}
