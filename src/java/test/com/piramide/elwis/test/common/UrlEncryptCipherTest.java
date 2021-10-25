package test.com.piramide.elwis.test.common;

import com.piramide.elwis.web.common.urlencrypt.UrlEncryptCipher;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * URL Encrypt cipher test
 */

public class UrlEncryptCipherTest {

    /**
     * Tests 500 concurrent request (threads) asking to encrypt and decrypt an URL.
     * In this test I realized that using local variables instead of "synchronized" modifier in the method is a faster
     * solution.
     */
    @Test(invocationCount = 500, threadPoolSize = 500)
    public void testEncyption() {
        final String originalUrl = "contacts/Person/Forward/Update.do?dto(adressId)=1231312&dto(name1)=Fernando";
        final String encryptedUrl = UrlEncryptCipher.i.encrypt(originalUrl);
        final String decryptedUrl = UrlEncryptCipher.i.decrypt(encryptedUrl);
        Assert.assertEquals(decryptedUrl, originalUrl);
    }
}
