package com.piramide.elwis.web.common.deduplication;

import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.Processor;
import no.priv.garshol.duke.matchers.MatchListener;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class Deduplication {

    private Configuration config;
    private MatchListener matchListener;

    public Deduplication(Configuration config, MatchListener matchListener) {
        this.config = config;
        this.matchListener = matchListener;
    }

    public void process() {
        Processor processor = new Processor(config);
        processor.addMatchListener(matchListener);
        processor.deduplicate();
        processor.close();
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public MatchListener getMatchListener() {
        return matchListener;
    }

    public void setMatchListener(MatchListener matchListener) {
        this.matchListener = matchListener;
    }
}
