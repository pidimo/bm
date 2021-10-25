package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miky
 * @version $Id: ImageStoreDTO.java 2009-06-23 11:52:38 AM $
 */
public class ImageStoreDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_IMAGESTOREID = "imageStoreId";

    public ImageStoreDTO() {
    }

    public ImageStoreDTO(DTO dto) {
        super.putAll(dto);
    }

    public ImageStoreDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new ImageStoreDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_IMAGESTORE;
    }

    public String getPrimKeyName() {
        return KEY_IMAGESTOREID;
    }

    public String getTableName() {
        return WebMailConstants.TABLE_IMAGESTORE;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("fileName"));
    }

    public HashMap referencedValues() {
        HashMap m = new HashMap();
        m.put(CatalogConstants.TABLE_TEMPLATETEXTIMG, "imagestoreid");
        m.put(CampaignConstants.TABLE_CAMPAIGNTEXTIMG, "imagestoreid");
        m.put(CampaignConstants.TABLE_CAMPAIGNGENTEXTIMG, "imagestoreid");

        return m;
    }
}
