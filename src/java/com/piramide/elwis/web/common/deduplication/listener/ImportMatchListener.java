package com.piramide.elwis.web.common.deduplication.listener;

import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.common.deduplication.DeduplicationUtil;
import no.priv.garshol.duke.Property;
import no.priv.garshol.duke.Record;
import no.priv.garshol.duke.matchers.AbstractMatchListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ImportMatchListener extends AbstractMatchListener {
    private Log log = LogFactory.getLog(this.getClass());

    private int matches;
    private int records;
    private List<Property> properties;

    private Map<Integer, List<Integer>> importRecordIdMap;

    public ImportMatchListener(List<Property> properties) {
        log.debug("New Instance ImportMatchListener...");

        this.matches = 0;
        this.records = 0;
        this.properties = properties;

        importRecordIdMap = new HashMap<Integer, List<Integer>>();
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public Map<Integer, List<Integer>> getImportRecordIdMap() {
        return importRecordIdMap;
    }

    @Override
    public void startProcessing() {
        log.debug("Start deduplication match process.. " + importRecordIdMap);
    }

    public void batchReady(int size) {
        records += size;
    }

    public void matches(Record r1, Record r2, double confidence) {

        matches++;

        if (isImportRecord(r1) && isAddressRecord(r2)) {
            Integer importRecordId = getElementIdAsInteger(r1);
            Integer addressId = getElementIdAsInteger(r2);

            if (importRecordId != null && addressId != null) {
                if (importRecordIdMap.containsKey(importRecordId)) {

                    if (!importRecordIdMap.get(importRecordId).contains(addressId)) {
                        importRecordIdMap.get(importRecordId).add(addressId);
                    }
                } else {
                    List<Integer> addressIdList = new ArrayList<Integer>();
                    addressIdList.add(addressId);

                    importRecordIdMap.put(importRecordId, addressIdList);
                }
            }
        }

        //debugMatchRecord(r1, r2, confidence, "\nMATCH", properties);
    }

    public void matchesPerhaps(Record r1, Record r2, double confidence) {
        debugMatchRecord(r1, r2, confidence, "\nMAYBE MATCH", properties);
    }

    public void endProcessing() {
        log.debug("");
        log.debug("Total records: " + records);
        log.debug("Total matches: " + matches);

        log.debug("Import record duplicate Map:" + importRecordIdMap);
        log.debug("Import record duplicate size:" + importRecordIdMap.size());
    }

    public void noMatchFor(Record record) {
    }

    private Integer getElementIdAsInteger(Record record) {
        Integer result = null;
        String elementId = record.getValue(DeduplicationUtil.PROP_ELEMENTID);

        if (elementId != null) {
            try {
                result = Integer.valueOf(elementId.trim());
            } catch (NumberFormatException ignore) {
            }
        }
        return result;
    }

    private boolean isImportRecord(Record record) {
        String type = record.getValue(DeduplicationUtil.PROP_TYPE);
        return ContactConstants.ImportRecordType.contains(type);
    }

    private boolean isAddressRecord(Record record) {
        String type = record.getValue(DeduplicationUtil.PROP_TYPE);
        return type != null && (ContactConstants.ADDRESSTYPE_PERSON.equals(type.trim()) || ContactConstants.ADDRESSTYPE_ORGANIZATION.equals(type.trim()));
    }

    private void debugMatchRecord(Record r1, Record r2, double confidence, String heading, List<Property> props) {
        log.debug(heading + " " + confidence);

        for (Property p : props) {
            String prop = p.getName();
            if ((r1.getValues(prop) == null || r1.getValues(prop).isEmpty()) &&
                    (r2.getValues(prop) == null || r2.getValues(prop).isEmpty())) {
                continue;
            }

            //log.debug(prop);
            log.debug(prop + " " + propertyConfidenceTest(r1, r2, p));
            log.debug("  " + value(r1, prop));
            log.debug("  " + value(r2, prop));
        }
    }

    private String value(Record r, String p) {
        Collection<String> vs = r.getValues(p);
        if (vs == null) {
            return "<null>";
        }
        if (vs.isEmpty()) {
            return "<null>";
        }

        StringBuffer buf = new StringBuffer();
        for (String v : vs) {
            buf.append("'");
            buf.append(v);
            buf.append("', ");
        }

        return buf.toString();
    }

    private String propertyConfidenceTest(Record r1, Record r2, Property p) {
        Double confidence = null;
        if (!(p.isIdProperty() || p.isIgnoreProperty())) {
            String propName = p.getName();
            no.priv.garshol.duke.Comparator comparator = p.getComparator();
            String value1 = r1.getValue(propName);
            String value2 = r2.getValue(propName);

            if (comparator != null && value1 != null && value2 != null) {
                confidence = comparator.compare(value1, value2);
            }
        }
        return confidence != null ? String.valueOf(confidence) : "";
    }
}
