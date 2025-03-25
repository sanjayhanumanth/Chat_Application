package com.live.chat_service.util;

import com.live.chat_service.exception.CustomValidationExceptions;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class CommonUtil {

    private static final String ALGORITHM = "AES";
    private static final byte[] SECRET_KEY = "1234567890123456".getBytes();

    public String decryptMessage(String message) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY, ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(message));
            return new String(decryptedData);
        } catch (Exception e) {
            throw new CustomValidationExceptions("Error while decrypting");
        }
    }

    public String encryptMessage(String message) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY, ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(message.getBytes());
             return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new CustomValidationExceptions("Error while encrypting");
        }
    }
}
