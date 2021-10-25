package com.piramide.elwis.web.admin.captcha;

import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * Jatun S.R.L.
 *
 * @author : ivan
 */
public class CaptchaServiceSingleton {

    private static ImageCaptchaService instance;

    static {
        instance = new DefaultManageableImageCaptchaService(
                new FastHashMapCaptchaStore(),
                new MyImageCaptchaEngine(),
                180,
                100000,
                75000);
    }

    public static ImageCaptchaService getInstance() {
        return instance;
    }
    /*private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService();

    public static ImageCaptchaService getInstance(){
        return instance;
    }*/
}
