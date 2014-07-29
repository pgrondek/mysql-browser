package info.nerull7.mysqlbrowser;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by nerull7 on 28.07.14.
 */
public class Crypto {
    private static final String KEY_FILE = "null_file"; // to trick h4x0r5
//    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding"; // doesn't work TODO: Maybe fix?
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String KEY_ALGORITHM = "AES";
    private static final int OUTPUT_KEY_LENGTH = 256;

    private SecretKey secretKey;
    private Context context;

    public Crypto(Context context){
        this.context = context;

        try {
            getSecretKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SecretKey generateKey() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();

        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(OUTPUT_KEY_LENGTH, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();

        return secretKey;
    }

    private void getSecretKey() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        String key;

        // First try to open file
        File keyFile = new File(context.getFilesDir(), KEY_FILE);
        if(!keyFile.exists()) { // new key
            secretKey = generateKey();
            FileOutputStream fileOutputStream =  new FileOutputStream(keyFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(secretKey);
            objectOutputStream.close();
            fileOutputStream.close();
        } else { // read existing key from file
            FileInputStream fileInputStream = new FileInputStream(keyFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            secretKey = (SecretKey) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }
    }

    public byte[] encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] output;
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        output = cipher.doFinal(input.getBytes(Charset.defaultCharset()));

        return output;
    }

    public String decrypt(byte[] input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String output;
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        output = new String(cipher.doFinal(input), Charset.defaultCharset());

        return output;
    }

    public String decryptBase64(String encodedString) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte [] encryptedString = Base64.decode(encodedString, Base64.DEFAULT);
        String decrypted = decrypt(encryptedString);
        return decrypted;
    }
}
