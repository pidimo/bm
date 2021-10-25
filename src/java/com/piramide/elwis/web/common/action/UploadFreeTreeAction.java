package com.piramide.elwis.web.common.action;

import com.piramide.elwis.cmd.common.UploadFreeTextCmd;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.contactmanager.form.UploadForm;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * @author Tayes
 * @version $Id: UploadFreeTreeAction.java 10133 2011-08-12 20:00:43Z fernando $
 */
public class UploadFreeTreeAction extends DefaultAction {
    private final static String KEY = "fm2uf6dm0dy9mq7w";
    private final static int KEY_VALUE = 22;

    public static int createKey(String key) {
        float intKey = 0;
        for (int intX = 0; intX < key.length(); intX++) {
            float mc = (int) key.charAt(intX);
            float div = mc / 20;
            intKey = intKey + div;
        }

        return Math.round(intKey);
    }

    public static String dec(String cad) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] keyValue = KEY.getBytes();
        byte[] pass = new BASE64Decoder().decodeBuffer(cad);
        SecretKeySpec keySpec = new SecretKeySpec(keyValue, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        String decrypt = new String(cipher.doFinal(pass));

        return decrypt;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.info("Upload Attach");
        UploadForm uploadForm = (UploadForm) form;
        UploadFreeTextCmd cmd = new UploadFreeTextCmd();
        log.debug("Form:" + uploadForm.getDtoMap());
        cmd.putParam(uploadForm.getDtoMap());
        //&& uploadForm.getDtoMap().containsKey("operationUpdate")
        String password = (String) uploadForm.getDtoMap().get("password");
        if (password != null) {
            cmd.putParam("password", dec(password));
        }
        Locale en = new Locale("en");
        Locale es = new Locale("es");
        Locale de = new Locale("de");
        Locale fr = new Locale("fr");
        String[] resourcesEN = new String[]{JSPHelper.getMessage(en, "plugin.telecomtype.company"),
                JSPHelper.getMessage(en, "plugin.telecomtype.employee"),
                JSPHelper.getMessage(en, "plugin.telecomtype.address")
        };
        String[] resourcesES = new String[]{JSPHelper.getMessage(es, "plugin.telecomtype.company"),
                JSPHelper.getMessage(es, "plugin.telecomtype.employee"),
                JSPHelper.getMessage(es, "plugin.telecomtype.address")
        };
        String[] resourcesDE = new String[]{JSPHelper.getMessage(de, "plugin.telecomtype.company"),
                JSPHelper.getMessage(de, "plugin.telecomtype.employee"),
                JSPHelper.getMessage(de, "plugin.telecomtype.address")
        };

        String[] resourcesFR = new String[]{JSPHelper.getMessage(fr, "plugin.telecomtype.company"),
                JSPHelper.getMessage(fr, "plugin.telecomtype.employee"),
                JSPHelper.getMessage(fr, "plugin.telecomtype.address")
        };
        cmd.putParam("resourcesEN", resourcesEN);
        cmd.putParam("resourcesES", resourcesES);
        cmd.putParam("resourcesDE", resourcesDE);
        cmd.putParam("resourcesFR", resourcesFR);
        if (!"login".equals(request.getParameter("dto(op)"))) {
            cmd.putParam("file", uploadForm.getFile().getInputStream());
        }
        ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);

        String answer = resultDTO.getAsString("response") + "\n<*END*>";


        String fileName = "response.xml";
        ByteArrayOutputStream stream = new ByteArrayOutputStream(answer.getBytes().length);
        stream.write(answer.getBytes());
        MimeTypeUtil.formatResponseToDownloadFile(fileName, stream.size(), response);
        ServletOutputStream os = response.getOutputStream();
        stream.writeTo(os);
        os.flush();
        os.close();
        stream.close();
        return null;

    }
}
