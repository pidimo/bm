package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.TitleHome;
import com.piramide.elwis.dto.catalogmanager.TitleDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.ComponentDTOListBuilder;
import net.java.dev.strutsejb.ui.DTOListDirector;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Map;

/**
 * Command that represent a list reader for Titles.
 *
 * @author Fernando Montaño
 * @version $Id: ReadTitleListCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class ReadTitleListCmd extends EJBCommand {

    public void executeInStateful(Map sessionMap, SessionContext ctx) {

        final TitleDTO titleDTO = new TitleDTO(paramDTO);
        DTOListDirector.getOrCreateDTOList(sessionMap,
                new TitleListBuilder(titleDTO),
                resultDTO);
    }

    public boolean isStateful() {
        return true;
    }

    private class TitleListBuilder extends ComponentDTOListBuilder {

        private static final int PAGESIZE = 10;

        public TitleListBuilder(ComponentDTO factoryDTO) {
            super(factoryDTO);
        }

        protected Collection callFinder(EJBLocalHome ejbHome)
                throws FinderException {
            return ((TitleHome) ejbHome).findAll();
        }

        public String getDTOListName() {
            return "titleList";
        }

        public int getPageSize() {
            return PAGESIZE;
        }

        public void addDTOListEmptyMsgTo(ResultDTO resultDTO) {
            resultDTO.addResultMessage("msg.DTOListEmpty", "Title");
        }
    }

}
