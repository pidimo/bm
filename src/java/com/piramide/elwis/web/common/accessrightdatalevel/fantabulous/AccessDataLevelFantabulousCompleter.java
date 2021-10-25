package com.piramide.elwis.web.common.accessrightdatalevel.fantabulous;

import org.alfacentauro.fantabulous.structure.ListStructure;

/**
 * Interface to implement fantabulous completer for access right on data level
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public interface AccessDataLevelFantabulousCompleter {

    public ListStructure completeByList(Integer userId, ListStructure listStructure);

    public ListStructure completeByTable(Integer userId, ListStructure listStructure);
}
