package test;

import net.java.dev.strutsejb.ServiceLocator;
import net.java.dev.strutsejb.SysLevelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
/**
 * Jatun S.R.L.
 *
 * @author Fernando Montaño
 * @version $Id: DbQuery.java 9119 2009-04-17 00:21:59Z fernando $
 */

/**
 * Provides generic JDBC query feature.
 *
 * @author kaz
 * @version $Id: DbQuery.java 9119 2009-04-17 00:21:59Z fernando $
 */
public class DbQuery {

    /**
     * Singleton instance.
     */
    public static final DbQuery i = new DbQuery();

    private DbQuery() {
    }

    protected final Log log = LogFactory.getLog(getClass());

    /**
     * Executes a query with no parameters and put results into ResultSetDTO.
     *
     * @param dataSourceName name of DataSource to query
     * @param sql            SQL statement of query
     */
    public List executeQuery(String dataSourceName, String sql) {

        return executeQuery(dataSourceName, sql, new LinkedList());
    }

    /**
     * Executes a query with one parameter and put results into ResultSetDTO.
     *
     * @param dataSourceName name of DataSource to query
     * @param sql            SQL statement of query
     * @param param0         a parameter for the query
     */
    public List executeQuery(
            String dataSourceName,
            String sql,
            Object param0) {

        final List paramList = new LinkedList();
        paramList.add(param0);
        return executeQuery(dataSourceName, sql, paramList);
    }

    /**
     * Executes a query with specified parameters and put results into ResultSetDTO.
     *
     * @param dataSourceName name of DataSource to query
     * @param sql            SQL statement of query
     * @param paramList      List of parameters of query
     */
    public List executeQuery(
            String dataSourceName,
            String sql,
            List paramList) {

        final List recordList;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            //get Connection
            final DataSource ds =
                    (DataSource) ServiceLocator.i.lookup(dataSourceName);
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(sql);

            //set parameters to PreparedStatement
            int i = 1;
            for (Iterator it = paramList.iterator(); it.hasNext();) {
                pstmt.setObject(i++, it.next());
            }

            //execute a query and creates ResultSetDTO
            rs = pstmt.executeQuery();
            recordList = createRecordList(rs);

        } catch (SQLException e) {
            throw new SysLevelException(e);
        } catch (NamingException e) {
            throw new SysLevelException(e);
        } finally {
            closeAllResources(conn, pstmt, rs);
        }

        //logging
        if (log.isDebugEnabled()) {
            logSQLExecution(sql, paramList, recordList);
        }
        return recordList;
    }

    private void closeAllResources(
            Connection conn,
            PreparedStatement pstmt,
            ResultSet rs) {

        try {
            rs.close();
        } catch (Exception e) {
        }
        try {
            pstmt.close();
        } catch (Exception e) {
        }
        try {
            conn.close();
        } catch (Exception e) {
        }
    }

    private List createRecordList(ResultSet rs) throws SQLException {

        final List recordList = new LinkedList();
        final ResultSetMetaData rsmd = rs.getMetaData();
        final int columnCount = rsmd.getColumnCount();
        while (rs.next()) {
            final Map item = new HashMap(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                final String columnName = rsmd.getColumnName(i);
                final String columnValue;
                switch (rsmd.getColumnType(i)) {
                    case java.sql.Types.LONGVARBINARY:
                        columnValue = new String(rs.getBytes(i));
                        break;
                    case java.sql.Types.BINARY:
                        columnValue = new String(rs.getBytes(i));
                        break;
                    case java.sql.Types.BLOB:
                        columnValue = new String(rs.getBytes(i));
                        break;
                    case java.sql.Types.VARBINARY:
                        columnValue = new String(rs.getBytes(i));
                        break;
                    default:
                        columnValue = rs.getString(i);
                        break;
                }

                //final String columnValue = rs.getString(i);

                item.put(columnName, columnValue);
            }
            recordList.add(item);
        }
        return recordList;
    }

    /**
     * Executes a query and returns a List of prim key values.
     *
     * @param dataSourceName name of DataSource to query
     * @param sql            SQL statement of query
     * @param primKeyName    name of primary key
     */
    public List createKeyList(
            String dataSourceName,
            String sql,
            String primKeyName) {

        final List primKeyList = new LinkedList();
        final List resultList =
                executeQuery(dataSourceName, sql, new LinkedList());
        for (Iterator it = resultList.iterator(); it.hasNext();) {
            final Map recordMap = (Map) it.next();
            primKeyList.add(recordMap.get(primKeyName));
        }
        return primKeyList;
    }

    /**
     * Executes a query to render a List of DTOs from a List of
     * primary keys.
     *
     * @param dataSourceName name of DataSource to query
     * @param sqlPrefix      SQL statement without WHERE clause
     * @param primKeyName    name of primary key
     * @param primKeyList    List of primary keys
     */
    public List renderDTOs(
            String dataSourceName,
            String sqlPrefix,
            String primKeyName,
            List primKeyList) {

        //build SQL
        final int listSize = primKeyList.size();
        final StringBuffer sql = new StringBuffer(sqlPrefix);
        sql.append(" WHERE ");
        for (int i = 0; i < listSize; i++) {
            sql.append(primKeyName);
            sql.append(" = ? ");
            if (i < listSize - 1) {
                sql.append(" OR ");
            }
        }

        //execute query
        final List unsortedDTOList =
                executeQuery(dataSourceName, new String(sql), primKeyList);

        //create a Map of DTOs
        final Map dtoMap = new HashMap();
        for (Iterator it = unsortedDTOList.iterator(); it.hasNext();) {
            final Map recordMap = (Map) it.next();
            final Object primKey = recordMap.get(primKeyName);
            dtoMap.put(primKey, recordMap);
        }

        //create a List of DTOs sorted by primKeyList order
        final List sortedDTOList = new LinkedList();
        for (Iterator it = primKeyList.iterator(); it.hasNext();) {
            final Object primKey = it.next();
            sortedDTOList.add(dtoMap.get(primKey));
        }
        return sortedDTOList;
    }

    private void logSQLExecution(String sql, List params, List recordList) {

        //prints the SQL statement
        final StringBuffer sb = new StringBuffer();
        sb.append("executeQuery: ");
        sb.append(sql);

        //prints parameters
        sb.append(" (");
        for (Iterator it = params.iterator(); it.hasNext();) {
            sb.append("" + it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        sb.append(")");

        //prints results
        sb.append("\nexecuteQuery: result: ");
        sb.append(recordList);

        log.debug(sb);
    }

}
