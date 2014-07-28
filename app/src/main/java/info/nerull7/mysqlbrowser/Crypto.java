package info.nerull7.mysqlbrowser;

import android.util.Log;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by nerull7 on 28.07.14.
 */
public class Crypto {
    private static final String KEY_FILE = "null_file"; // to trick h4x0r5
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final int OUTPUT_KEY_LENGTH = 256;

    private static SecretKey secretKey;

    private static SecretKey generateKey() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();

        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keyGenerator.init(OUTPUT_KEY_LENGTH, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();

        return secretKey;
    }

    private static SecretKey getSecretKey() throws NoSuchAlgorithmException {
        if(secretKey==null)
            secretKey = generateKey();

        Log.d("SecureKEY", "Hash:" + secretKey.hashCode());
        return secretKey;
    }

    public static byte[] encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] output;
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
        output = cipher.doFinal(input.getBytes(Charset.defaultCharset()));

        return output;
    }

    public static String decrypt(byte[] input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String output;
        byte [] tmp; // TODO: REMOVE

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
//        output = String.valueOf(cipher.doFinal(input));
        tmp = cipher.doFinal(input);

//        output = tmp.toString();
        output = new String(input, Charset.defaultCharset());

        return output;
    }

}
