package com.piramide.elwis.web.common.deduplication;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import no.priv.garshol.duke.*;
import no.priv.garshol.duke.cleaners.LowerCaseNormalizeCleaner;
import no.priv.garshol.duke.comparators.*;
import no.priv.garshol.duke.databases.LuceneDatabase;
import no.priv.garshol.duke.datasources.Column;
import no.priv.garshol.duke.datasources.JNDIDataSource;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenerator;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGeneratorManager;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DeduplicationUtil {
    private static Log log = LogFactory.getLog(DeduplicationUtil.class);

    public static final String PROP_ID = "ID";
    public static final String PROP_ELEMENTID = "ELEMENTID";
    public static final String PROP_NAME = "NAME";
    public static final String PROP_COUNTRY = "COUNTRY";
    public static final String PROP_CITY = "CITY";
    public static final String PROP_ZIP = "ZIP";
    public static final String PROP_STREET = "STREET";
    public static final String PROP_HOUSENUMBER = "HOUSENUMBER";
    public static final String PROP_TYPE = "TYPE";

    public static Configuration importDeduplicationConfig(Integer profileId, HttpServletRequest request) {
        return importDeduplicationConfig(profileId, false, request);
    }

    public static Configuration importDeduplicationConfig(Integer profileId, boolean isStrictComparison, HttpServletRequest request) {
        ConfigurationImpl config = new ConfigurationImpl();

        double threshold = getAddressThreshold(isStrictComparison);
        config.setThreshold(threshold);
        //config.setMaybeThreshold(0.8);

        //define indexing data base, this is to make fast the process
        config.setDatabase(getIndexingDatabase());

        //cleaners
        LowerCaseNormalizeCleaner lowerCaseNormalizeCleaner = new LowerCaseNormalizeCleaner();

        Map<String, PropertyConfig> propConfigMap = getAddressPropertyConfigMap(isStrictComparison);
        PropertyConfig nameCfg = propConfigMap.get(PROP_NAME);
        PropertyConfig countryCfg = propConfigMap.get(PROP_COUNTRY);
        PropertyConfig cityCfg = propConfigMap.get(PROP_CITY);
        PropertyConfig zipCfg = propConfigMap.get(PROP_ZIP);
        PropertyConfig streetCfg = propConfigMap.get(PROP_STREET);
        PropertyConfig houseNumberCfg = propConfigMap.get(PROP_HOUSENUMBER);

        List<Property> props = new ArrayList<Property>();
        props.add(new PropertyImpl(PROP_ID));
        props.add(new PropertyImpl(PROP_NAME, nameCfg.getComparator(), nameCfg.getLow(), nameCfg.getHigh()));
        props.add(new PropertyImpl(PROP_COUNTRY, countryCfg.getComparator(), countryCfg.getLow(), countryCfg.getHigh()));
        props.add(new PropertyImpl(PROP_CITY, cityCfg.getComparator(), cityCfg.getLow(), cityCfg.getHigh()));
        props.add(new PropertyImpl(PROP_ZIP, zipCfg.getComparator(), zipCfg.getLow(), zipCfg.getHigh()));
        props.add(new PropertyImpl(PROP_STREET, streetCfg.getComparator(), streetCfg.getLow(), streetCfg.getHigh()));
        props.add(new PropertyImpl(PROP_HOUSENUMBER, houseNumberCfg.getComparator(), houseNumberCfg.getLow(), houseNumberCfg.getHigh()));

        Property elementIdProperty = new PropertyImpl(PROP_ELEMENTID, null, 0.0, 0.0);
        elementIdProperty.setIgnoreProperty(true);

        Property typeProperty = new PropertyImpl(PROP_TYPE, null, 0.0, 0.0);
        typeProperty.setIgnoreProperty(true);

        props.add(elementIdProperty);
        props.add(typeProperty);

        config.setProperties(props);

        //data source
        JNDIDataSource jndiDataSource = new JNDIDataSource();
        jndiDataSource.setJndiPath(Constants.JNDI_ELWISDS);
        jndiDataSource.setQuery(getImportListQuery(profileId, request));

        //columns
        Column id = new Column("rowId", PROP_ID, null, null);
        Column name = new Column("fullName", PROP_NAME, null, lowerCaseNormalizeCleaner);
        Column country = new Column("countryName", PROP_COUNTRY, null, lowerCaseNormalizeCleaner);
        Column city = new Column("cityName", PROP_CITY, null, lowerCaseNormalizeCleaner);
        Column zip = new Column("zip", PROP_ZIP, null, lowerCaseNormalizeCleaner);
        Column street = new Column("street", PROP_STREET, null, lowerCaseNormalizeCleaner);
        Column houseNumber = new Column("houseNumber", PROP_HOUSENUMBER, null, lowerCaseNormalizeCleaner);

        Column elementId = new Column("elementId", PROP_ELEMENTID, null, null);
        Column type = new Column("elementType", PROP_TYPE, null, null);

        jndiDataSource.addColumn(id);
        jndiDataSource.addColumn(name);
        jndiDataSource.addColumn(country);
        jndiDataSource.addColumn(city);
        jndiDataSource.addColumn(zip);
        jndiDataSource.addColumn(street);
        jndiDataSource.addColumn(houseNumber);
        jndiDataSource.addColumn(elementId);
        jndiDataSource.addColumn(type);

        config.addDataSource(0, jndiDataSource);

        return config;
    }

    public static Configuration addressDeduplicationConfig(HttpServletRequest request) {
        return addressDeduplicationConfig(false, request);
    }

    public static Configuration addressDeduplicationConfig(boolean isStrictComparison, HttpServletRequest request) {
        ConfigurationImpl config = new ConfigurationImpl();

        double threshold = getAddressThreshold(isStrictComparison);
        config.setThreshold(threshold);
        //config.setMaybeThreshold(0.8);

        //define indexing data base, this is to make fast the process
        config.setDatabase(getIndexingDatabase());

        //cleaners
        LowerCaseNormalizeCleaner lowerCaseNormalizeCleaner = new LowerCaseNormalizeCleaner();

        Map<String, PropertyConfig> propConfigMap = getAddressPropertyConfigMap(isStrictComparison);
        PropertyConfig nameCfg = propConfigMap.get(PROP_NAME);
        PropertyConfig countryCfg = propConfigMap.get(PROP_COUNTRY);
        PropertyConfig cityCfg = propConfigMap.get(PROP_CITY);
        PropertyConfig zipCfg = propConfigMap.get(PROP_ZIP);
        PropertyConfig streetCfg = propConfigMap.get(PROP_STREET);
        PropertyConfig houseNumberCfg = propConfigMap.get(PROP_HOUSENUMBER);

        List<Property> props = new ArrayList<Property>();
        props.add(new PropertyImpl(PROP_ID));
        props.add(new PropertyImpl(PROP_NAME, nameCfg.getComparator(), nameCfg.getLow(), nameCfg.getHigh()));
        props.add(new PropertyImpl(PROP_COUNTRY, countryCfg.getComparator(), countryCfg.getLow(), countryCfg.getHigh()));
        props.add(new PropertyImpl(PROP_CITY, cityCfg.getComparator(), cityCfg.getLow(), cityCfg.getHigh()));
        props.add(new PropertyImpl(PROP_ZIP, zipCfg.getComparator(), zipCfg.getLow(), zipCfg.getHigh()));
        props.add(new PropertyImpl(PROP_STREET, streetCfg.getComparator(), streetCfg.getLow(), streetCfg.getHigh()));
        props.add(new PropertyImpl(PROP_HOUSENUMBER, houseNumberCfg.getComparator(), houseNumberCfg.getLow(), houseNumberCfg.getHigh()));

        config.setProperties(props);

        //data source
        JNDIDataSource jndiDataSource = new JNDIDataSource();
        jndiDataSource.setJndiPath(Constants.JNDI_ELWISDS);
        jndiDataSource.setQuery(getAddressSourceListQuery(request));

        //columns
        Column id = new Column("addressId", PROP_ID, null, null);
        Column name = new Column("fullName", PROP_NAME, null, lowerCaseNormalizeCleaner);
        Column country = new Column("countryName", PROP_COUNTRY, null, lowerCaseNormalizeCleaner);
        Column city = new Column("cityName", PROP_CITY, null, lowerCaseNormalizeCleaner);
        Column zip = new Column("zip", PROP_ZIP, null, lowerCaseNormalizeCleaner);
        Column street = new Column("street", PROP_STREET, null, lowerCaseNormalizeCleaner);
        Column houseNumber = new Column("houseNumber", PROP_HOUSENUMBER, null, lowerCaseNormalizeCleaner);

        jndiDataSource.addColumn(id);
        jndiDataSource.addColumn(name);
        jndiDataSource.addColumn(country);
        jndiDataSource.addColumn(city);
        jndiDataSource.addColumn(zip);
        jndiDataSource.addColumn(street);
        jndiDataSource.addColumn(houseNumber);

        config.addDataSource(0, jndiDataSource);
        return config;
    }

    private static LuceneDatabase getIndexingDatabase() {
        LuceneDatabase luceneDatabase = new LuceneDatabase();
        //define search hit to make this more fast and improve the performance
        luceneDatabase.setMaxSearchHits(30);

        return luceneDatabase;
    }

    private static double getAddressThreshold(boolean isStrictComparison) {
        double threshold = 0.89;
        if (isStrictComparison) {
            threshold = 0.905;
        }
        return threshold;
    }

    private static Map<String, PropertyConfig> getAddressPropertyConfigMap(boolean isStrictComparison) {

        //comparators
        JaroWinkler compJaroWinkler = new JaroWinkler();
        Levenshtein compLevenshtein = new Levenshtein();
        WeightedLevenshtein compWeightedLevenshtein = new WeightedLevenshtein();
        PersonNameComparator nameComparator = new PersonNameComparator();
        ExactComparator exactComparator = new ExactComparator();

        QGramComparator qGramComparator = new QGramComparator();
        qGramComparator.setFormula(QGramComparator.Formula.JACCARD);

        Map<String, PropertyConfig> configMap = new HashMap<String, PropertyConfig>();
        configMap.put(PROP_NAME, new PropertyConfig(qGramComparator, 0.04, 0.90));
        configMap.put(PROP_COUNTRY, new PropertyConfig(exactComparator, 0.1, 0.74));
        configMap.put(PROP_CITY, new PropertyConfig(compLevenshtein, 0.1, 0.58));
        configMap.put(PROP_ZIP, new PropertyConfig(exactComparator, 0.01, 0.55));
        configMap.put(PROP_STREET, new PropertyConfig(compWeightedLevenshtein, 0.01, 0.70));
        configMap.put(PROP_HOUSENUMBER, new PropertyConfig(compWeightedLevenshtein, 0.01, 0.75));

        if (isStrictComparison) {
            configMap = new HashMap<String, PropertyConfig>();
            configMap.put(PROP_NAME, new PropertyConfig(compJaroWinkler, 0.01, 0.60));
            configMap.put(PROP_COUNTRY, new PropertyConfig(exactComparator, 0.01, 0.60));
            configMap.put(PROP_CITY, new PropertyConfig(compLevenshtein, 0.01, 0.60));
            configMap.put(PROP_ZIP, new PropertyConfig(exactComparator, 0.01, 0.60));
            configMap.put(PROP_STREET, new PropertyConfig(compWeightedLevenshtein, 0.01, 0.60));
            configMap.put(PROP_HOUSENUMBER, new PropertyConfig(compWeightedLevenshtein, 0.01, 0.60));
        }

        return configMap;
    }

    private static String getImportListQuery(Integer profileId, HttpServletRequest request) {
        String listName = "importDeduplicationSourceList";

        Parameters parameters = new Parameters();
        parameters.addSearchParameter("profileId", profileId.toString());

        return getListQuery(listName, parameters, request);
    }

    private static String getAddressSourceListQuery(HttpServletRequest request) {
        String listName = "addressDeduplicationSourceList";
        Parameters parameters = new Parameters();
        return getListQuery(listName, parameters, request);
    }

    private static String getListQuery(String listName, Parameters parameters, HttpServletRequest request) {
        String query = "";

        FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(request.getSession().getServletContext(), request);

        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
        } catch (ListStructureNotFoundException e) {
            log.debug("->Read List " + listName + " In Fantabulous structure Fail");
        }

        if (list != null) {

            if (parameters == null) {
                parameters = new Parameters();
            }

            //default parameters
            User user = RequestUtils.getUser(request);
            parameters.addSearchParameter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());

            SqlGenerator generator = SqlGeneratorManager.newInstance();
            query = generator.generate(list, parameters);

            log.debug("QUERYY::" + query);
        }
        return query;
    }



    public static Configuration contactDeduplicationFileConfig() {
//        String fileConfigPath = "/elwis/com/piramide/elwis/web/common/deduplication/contactDeduplicationTest.xml";
        String fileConfigPath = "contactDeduplicationTest.xml";

        Configuration config = null;
        try {
            config = ConfigLoader.load(fileConfigPath);
            log.debug("Config:" + config);
            log.debug("Config:" + config.getDataSources());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return config;
    }


    public static Configuration contactDeduplicationConfigTest(HttpServletRequest request) {
        ConfigurationImpl config = new ConfigurationImpl();
        config.setThreshold(0.89);
        //config.setMaybeThreshold(0.8);

        //comparators
        JaroWinkler compJaroWinkler = new JaroWinkler();
        Levenshtein compLevenshtein = new Levenshtein();

        //cleaners
        LowerCaseNormalizeCleaner lowerCaseNormalizeCleaner = new LowerCaseNormalizeCleaner();

        List<Property> props = new ArrayList<Property>();
        props.add(new PropertyImpl("ID"));
        props.add(new PropertyImpl("NAME", compJaroWinkler, 0.09, 0.88));
        props.add(new PropertyImpl("COUNTRY", compLevenshtein, 0.48, 0.6));
        props.add(new PropertyImpl("CITYZIP", compLevenshtein, 0.48, 0.6));
        props.add(new PropertyImpl("STREETHOUSENUMBER", compLevenshtein, 0.48, 0.6));
        config.setProperties(props);

        //data source query
        String query = "        select a.name1 as fullName, co.countryname as countryName, ci.cityname as cityAndZip, a.street as streetAndNumber\n" +
                "        from address a\n" +
                "        left join country co on a.countryid=co.countryid\n" +
                "        left join city ci on a.cityid=ci.cityid\n" +
                "        where a.companyid=1 and a.addressid > 8000";

        //data source
        JNDIDataSource jndiDataSource = new JNDIDataSource();
        jndiDataSource.setJndiPath(Constants.JNDI_ELWISDS);
        jndiDataSource.setQuery(query);

        //columns
        Column id = new Column("addressId", "ID", null, null);
        Column name = new Column("fullName", "NAME", null, null);
        Column country = new Column("countryName", "COUNTRY", null, lowerCaseNormalizeCleaner);
        Column city = new Column("cityAndZip", "CITYZIP", null, null);
        Column street = new Column("streetAndNumber", "STREETHOUSENUMBER", null, null);

        //jndiDataSource.addColumn(id);
        jndiDataSource.addColumn(name);
        jndiDataSource.addColumn(country);
        jndiDataSource.addColumn(city);
        jndiDataSource.addColumn(street);

        config.addDataSource(0, jndiDataSource);

        return config;
    }

}
