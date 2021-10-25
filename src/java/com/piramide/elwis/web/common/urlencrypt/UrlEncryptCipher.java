package com.piramide.elwis.web.common.urlencrypt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Encrypt and decrypt using the TripleDES encryption algorithm
 * and a key specification for the secret key.
 *
 * @author Fernando Montaño
 * @version $Id: UrlEncryptCipher.java 9193 2009-05-14 15:15:22Z fernando $
 */

public class UrlEncryptCipher {
    private static final String ALGORITHM = "DESede";
    private static final String spec = "JxkBX5JVwH5Xl6Ymq08u9dJr";
    private final UrlBase64Encoder urlBase64Encoder;
    private final SecretKey secretKey;
    private static Log log = LogFactory.getLog(UrlEncryptCipher.class);
    public static final UrlEncryptCipher i = new UrlEncryptCipher();

    private UrlEncryptCipher() {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec keySpec = new DESedeKeySpec(spec.getBytes());
            secretKey = keyFactory.generateSecret(keySpec);
            urlBase64Encoder = new UrlBase64Encoder();
        } catch (NoSuchAlgorithmException e) {
            log.error("The algorithm = " + ALGORITHM + " is not supported", e);
            throw new UrlCipherException("The algorithm = " + ALGORITHM + " is not supported", e);
        } catch (InvalidKeyException e) {
            log.error("Invalid key", e);
            throw new UrlCipherException("the key is invalid", e);
        } catch (InvalidKeySpecException e) {
            log.error("Invalid key specification", e);
            throw new UrlCipherException("Invalid key specification", e);
        }
    }

    /**
     * Encrypt a string and then encode in base64 for url
     *
     * @param str string to be encrypted
     * @return encrypted string
     */
    public String encrypt(String str) {
        try {
            final Cipher encryptor = Cipher.getInstance(ALGORITHM);
            encryptor.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptText = encryptor.doFinal(str.getBytes());
            return urlBase64Encoder.encode(encryptText);
        } catch (BadPaddingException e) {
            throw new UrlCipherException("Error on encryption", e);
        } catch (IllegalBlockSizeException e) {
            throw new UrlCipherException("Error on encryption", e);
        } catch (Exception e) {
            throw new UrlCipherException("Error on encryption", e);
        }
    }

    /**
     * Decrypt an encrypted string
     *
     * @param str string to be decrypted
     * @return the original decrypted string.
     */
    public String decrypt(String str) {
        try {
            final Cipher decryptor = Cipher.getInstance(ALGORITHM);
            decryptor.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedText = urlBase64Encoder.decode(str);
            return new String(decryptor.doFinal(decodedText));
        } catch (BadPaddingException e) {
            throw new UrlCipherException("Error on decryption", e);
        } catch (IllegalBlockSizeException e) {
            throw new UrlCipherException("Error on decryption", e);
        } catch (Exception e) {
            throw new UrlCipherException("Error on decryption", e);
        }
    }


}
