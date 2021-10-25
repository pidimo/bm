package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.domain.supportmanager.SupportFreeText;
import com.piramide.elwis.domain.supportmanager.SupportFreeTextHome;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author : ivan
 * @version : $Id DownloadSupportAttachCmd ${time}
 */
public class DownloadSupportAttachCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        Integer attachId = Integer.valueOf(paramDTO.get("attachId").toString());
        String supportAttachName = paramDTO.get("supportAttachName").toString();
        //Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        SupportAttachCmd supportAttachCmd = new SupportAttachCmd();
        supportAttachCmd.putParam("attachId", attachId);
        supportAttachCmd.putParam("supportAttachName", supportAttachName);
        //supportAttachCmd.putParam("companyId", companyId);
        supportAttachCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = supportAttachCmd.getResultDTO();

        if (!supportAttachCmd.getResultDTO().isFailure()) {
            Integer freeTextId = Integer.valueOf(myResultDTO.get("freetextId").toString());
            SupportFreeTextHome freeTextHome = (SupportFreeTextHome)
                    EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_FREETEXT);
            try {
                SupportFreeText freeText = freeTextHome.findByPrimaryKey(freeTextId);
                ArrayByteWrapper wrapper = new ArrayByteWrapper(freeText.getFreeTextValue());
                //InputStream in = new ByteArrayInputStream(wrapper.getFileData());
                resultDTO.put("fileDownload", wrapper);
                resultDTO.put("freeTextType", freeText.getFreeTextType());
                resultDTO.put("fileName", myResultDTO.get("supportAttachName"));
                resultDTO.put("fileSize", Integer.valueOf(String.valueOf(wrapper.getFileData().length)));
                resultDTO.put("Failure", Boolean.valueOf(false));
            } catch (FinderException e) {
                log.warn("Cannot read FreeText ... ");
                resultDTO.setResultAsFailure();
                resultDTO.put("Failure", Boolean.valueOf(true));
            }
        } else {
            resultDTO.setResultAsFailure();
            resultDTO.setForward("Fail");
            resultDTO.put("Failure", Boolean.valueOf(true));
        }

    }

    public boolean isStateful() {
        return false;
    }
}
