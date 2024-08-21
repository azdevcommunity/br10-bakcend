package fib.br10.core.utility;

import fib.br10.configuration.SecurityEnv;
import fib.br10.core.enums.Algorithms;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
@Log4j2
public class EncryptionUtil {
    SecurityEnv securityEnv;

    private SecretKey getSecretKey(String key) throws Exception {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance(Algorithms.SHA256.getValue());
        keyBytes = sha.digest(keyBytes);
        return new SecretKeySpec(keyBytes, 0, 16, securityEnv.getEncryption().algorithm());
    }

    public Optional<String> encrypt(String data)  {
        try {
            Cipher cipher = Cipher.getInstance(securityEnv.getEncryption().algorithm());
            SecretKey secretKey = getSecretKey(securityEnv.getEncryption().secret());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Optional.of(Base64.getEncoder().encodeToString(encryptedData));
        } catch (Exception e) {
            log.error(e);
            return Optional.empty();
        }
    }

    public Optional<String> decrypt(String encryptedData) {
       try{
           Cipher cipher = Cipher.getInstance(securityEnv.getEncryption().algorithm());
           SecretKey secretKey = getSecretKey(securityEnv.getEncryption().secret());
           cipher.init(Cipher.DECRYPT_MODE, secretKey);
           byte[] decodedData = Base64.getDecoder().decode(encryptedData);
           byte[] originalData = cipher.doFinal(decodedData);
           return Optional.of(new String(originalData, StandardCharsets.UTF_8));
       }catch (Exception e){
           log.error(e);
           return Optional.empty();
       }
    }
}