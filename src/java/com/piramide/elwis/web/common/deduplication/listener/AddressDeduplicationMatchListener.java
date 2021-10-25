package com.piramide.elwis.web.common.deduplication.listener;

import com.piramide.elwis.web.common.deduplication.DeduplicationUtil;
import no.priv.garshol.duke.Comparator;
import no.priv.garshol.duke.Property;
import no.priv.garshol.duke.Record;
import no.priv.garshol.duke.matchers.AbstractMatchListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class AddressDeduplicationMatchListener extends AbstractMatchListener {
    private Log log = LogFactory.getLog(this.getClass());

    private int matches;
    private int records;
    private List<Property> properties;

    private List<String> processedItemKeyList;
    private List<List<Integer>> addressIdDupliList;

    public AddressDeduplicationMatchListener(List<Property> properties) {
        log.debug("New Instance AddressDeduplicationMatchListener...");

        this.matches = 0;
        this.records = 0;
        this.properties = properties;

        addressIdDupliList = new ArrayList<List<Integer>>();
        processedItemKeyList = new ArrayList<String>();
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<List<Integer>> getAddressIdDupliList() {
        return addressIdDupliList;
    }

    @Override
    public void startProcessing() {
        log.debug("Start deduplication match process.. " + addressIdDupliList);
    }

    public void batchReady(int size) {
        records += size;
    }

    public void matches(Record r1, Record r2, double confidence) {
        matches++;

        Integer addressId1 = getAddressIdAsInteger(r1);
        Integer addressId2 = getAddressIdAsInteger(r2);

        if (addressId1 != null && addressId2 != null) {

            String key = composeKey(addressId1, addressId2);
            if (!processedItemKeyList.contains(key)) {
                processedItemKeyList.add(key);
                addNewDuplicateItemInList(addressId1, addressId2);
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

        log.debug("address duplicate List:" + addressIdDupliList);
        log.debug("address duplicate List size:" + addressIdDupliList.size());
    }

    public void noMatchFor(Record record) {
    }

    private Integer getAddressIdAsInteger(Record record) {
        Integer result = null;
        String id = record.getValue(DeduplicationUtil.PROP_ID);
        if (id != null) {
            try {
                result = Integer.valueOf(id.trim());
            } catch (NumberFormatException ignore) {
            }
        }
        return result;
    }

    private String composeKey(Integer addressId1, Integer addressId2) {
        String key;
        if (addressId1 < addressId2) {
            key = addressId1.toString() + "_" + addressId2.toString();
        } else {
            key = addressId2.toString() + "_" + addressId1.toString();
        }
        return key;
    }

    private void addNewDuplicateItemInList(Integer addressId1, Integer addressId2) {
        boolean isAdded = false;
        for (List<Integer> idList : addressIdDupliList) {

            if (idList.contains(addressId1) && idList.contains(addressId2)) {
                isAdded = true;
                break;
            } else if (idList.contains(addressId1) && !idList.contains(addressId2)) {
                idList.add(addressId2);
                isAdded = true;
                break;
            } else if (idList.contains(addressId2) && !idList.contains(addressId1)) {
                idList.add(addressId1);
                isAdded = true;
                break;
            }
        }

        if (!isAdded) {
            List<Integer> addressIdList = new ArrayList<Integer>();
            addressIdList.add(addressId1);
            addressIdList.add(addressId2);
            addressIdDupliList.add(addressIdList);
        }
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
            Comparator comparator = p.getComparator();
            String value1 = r1.getValue(propName);
            String value2 = r2.getValue(propName);

            if (comparator != null && value1 != null && value2 != null) {
                confidence = comparator.compare(value1, value2);
            }
        }
        return confidence != null ? String.valueOf(confidence) : "";
    }
}
