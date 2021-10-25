package com.piramide.elwis.web.common.deduplication;

import com.piramide.elwis.web.common.deduplication.listener.AddressDeduplicationMatchListener;
import com.piramide.elwis.web.common.deduplication.listener.ImportMatchListener;
import com.piramide.elwis.web.contactmanager.delegate.DeduplicationAddressDelegate;
import no.priv.garshol.duke.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DeduplicationFactory {
    private Log log = LogFactory.getLog(this.getClass());

    public static DeduplicationFactory i = new DeduplicationFactory();

    private DeduplicationFactory() {
    }

    public Map<Integer, List<Integer>> importDeduplicateProcess(Integer profileId, HttpServletRequest request) {
        log.debug("First check duplicate process Import: Normal...");
        Configuration config = DeduplicationUtil.importDeduplicationConfig(profileId, request);
        ImportMatchListener importMatchListener = new ImportMatchListener(config.getProperties());
        Deduplication deduplication = new Deduplication(config, importMatchListener);
        deduplication.process();

        log.debug("Second check duplicate process Import: Strict...");
        config = DeduplicationUtil.importDeduplicationConfig(profileId, true, request);
        importMatchListener.setProperties(config.getProperties());
        deduplication.setConfig(config);
        deduplication.process();

        return importMatchListener.getImportRecordIdMap();
    }


    public boolean addressDeduplicateProcess(Integer dedupliContactId, Integer companyId, HttpServletRequest request) {
        boolean existDuplicates = false;

        log.debug("First check duplicate process: Normal...");
        Configuration config = DeduplicationUtil.addressDeduplicationConfig(request);
        AddressDeduplicationMatchListener matchListener = new AddressDeduplicationMatchListener(config.getProperties());
        Deduplication deduplication = new Deduplication(config, matchListener);
        deduplication.process();

        log.debug("Second check duplicate process: Strict...");
        config = DeduplicationUtil.addressDeduplicationConfig(true, request);
        matchListener.setProperties(config.getProperties());
        deduplication.setConfig(config);
        deduplication.process();

        List<List<Integer>> addressIdDupliList = matchListener.getAddressIdDupliList();

        if (!addressIdDupliList.isEmpty()) {
            existDuplicates = true;
            DeduplicationAddressDelegate.i.createProcessedDuplicateAddress(dedupliContactId, companyId, addressIdDupliList);
        }
        return existDuplicates;
    }

}
