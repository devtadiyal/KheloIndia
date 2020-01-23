package kheloindia.com.assessment.util;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import android.util.Log;

import io.fabric.sdk.android.services.network.HttpRequest;

public class Crypto {

    public static final String TAG = "smsfwd";
    private  Cipher aesCipher;
    private SecretKey secretKey;
    private IvParameterSpec ivParameterSpec;
    private  String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private String CIPHER_ALGORITHM = "AES";
    // Replace me with a 16-byte key, share between Java and C#
    private  byte[] rawSecretKey = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private  String MESSAGEDIGEST_ALGORITHM = "MD5";

    public Crypto(String passphrase) {
        byte[] passwordKey = encodeDigest(passphrase);

        try {
            aesCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm " + CIPHER_ALGORITHM, e);
        } catch (NoSuchPaddingException e) {
            Log.e(TAG, "No such padding PKCS5", e);
        }

        secretKey = new SecretKeySpec(passwordKey, CIPHER_ALGORITHM);
        ivParameterSpec = new IvParameterSpec(rawSecretKey);
    }

    public String encryptAsBase64(String text) {
        byte[] encryptedData = encrypt(text);
        String encryptedText=HttpRequest.Base64.encodeBytes(encryptedData);
        return encryptedText;
    }


    public byte[] encrypt(String text) {
        try {
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "Invalid key", e);
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            Log.e(TAG, "Invalid algorithm " + CIPHER_ALGORITHM, e);
            return null;
        }

        byte[] encryptedData;
        try {
            encryptedData = aesCipher.doFinal(text.getBytes());
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG, "Illegal block size", e);
            return null;
        } catch (BadPaddingException e) {
            Log.e(TAG, "Bad padding", e);
            return null;
        }
        return encryptedData;
    }

    private byte[] encodeDigest(String text) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(MESSAGEDIGEST_ALGORITHM);
            return digest.digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm " + MESSAGEDIGEST_ALGORITHM, e);
        }

        return null;
    }
}
