package com.piramide.elwis.utils;

import com.piramide.elwis.exception.ServiceUnavailableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Object that contains util methods to encrypt and decrypt string using SHA algorithm
 *
 * @author Fernando Montaño
 * @version $Id: EncryptUtil.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class EncryptUtil {

    private Log log = LogFactory.getLog(this.getClass());
    /**
     * Singleton instance.
     */
    public static final EncryptUtil i = new EncryptUtil();

    private EncryptUtil() {
    }

    /**
     * Encrypt a text and obtain a unique hash string using the SHA-1 (Secure Hash Algorithm 1)
     *
     * @param text the text to encrypt
     * @return an encrypted text
     * @throws ServiceUnavailableException if the algorithm or encoding is not supported.
     */
    public synchronized String encryt(String text) throws ServiceUnavailableException {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceUnavailableException(e.getMessage());
        }
        try {
            md.update(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ServiceUnavailableException(e.getMessage());
        }

        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }


    public synchronized String cipher(String cad, String keyCad) throws Exception {
        //generates the key
        final byte src[] = i.getKey(keyCad);
        final byte[] ivBytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};

        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        //create secret key
        SecretKeySpec myKey = new SecretKeySpec(src, "DES");

        //get an instance of the cipher and setting up the sypher for encrypting
        Cipher encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, myKey, iv);


        //encripting the cad string
        byte[] encryptedPasswordBytes = encryptCipher.doFinal(cad.getBytes("UTF-8"));

        String encodedEncryptedPassword = new BASE64Encoder().encode(encryptedPasswordBytes);

        return encodedEncryptedPassword;
    }

    public synchronized String desCipher(String cad, String keyCad) throws Exception {
        //generate the key
        final byte src[] = i.getKey(keyCad);
        final byte[] ivBytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};

        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        //create secret key
        SecretKeySpec myKey = new SecretKeySpec(src, "DES");

        //get an instance of the cipher and setting up the sypher for decrypting
        Cipher decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, myKey, iv);

        //decripting the cad string
        byte[] passwordBytes = decryptCipher.doFinal(new BASE64Decoder().decodeBuffer(cad));

        String recoveredPassword = new String(passwordBytes, "UTF-8");
        return recoveredPassword;
    }

    public byte[] getKey(String cad) {
        byte[] result = {0x01, 0x02, 0x04, 0x08, 0x08, 0x04, 0x02, 0x01};
        byte[] partial = cad.getBytes();

        if (partial.length > 8) {
            for (int i = 0; i < result.length; i++) {
                result[i] = partial[i];
            }
        } else {
            for (int i = 0; i < partial.length; i++) {
                result[i] = partial[i];
            }
        }

        return result;
    }
}

