package net.minecraft.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CryptManager {

    private static final Logger field_180198_a = LogManager.getLogger();

    public static KeyPair func_75891_b() {
        try {
            KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");

            keypairgenerator.initialize(1024);
            return keypairgenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            nosuchalgorithmexception.printStackTrace();
            CryptManager.field_180198_a.error("Key pair generation failed!");
            return null;
        }
    }

    public static byte[] func_75895_a(String s, PublicKey publickey, SecretKey secretkey) {
        try {
            return func_75893_a("SHA-1", new byte[][] { s.getBytes("ISO_8859_1"), secretkey.getEncoded(), publickey.getEncoded()});
        } catch (UnsupportedEncodingException unsupportedencodingexception) {
            unsupportedencodingexception.printStackTrace();
            return null;
        }
    }

    private static byte[] func_75893_a(String s, byte[]... abyte) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance(s);
            byte[][] abyte1 = abyte;
            int i = abyte.length;

            for (int j = 0; j < i; ++j) {
                byte[] abyte2 = abyte1[j];

                messagedigest.update(abyte2);
            }

            return messagedigest.digest();
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            nosuchalgorithmexception.printStackTrace();
            return null;
        }
    }

    public static PublicKey func_75896_a(byte[] abyte) {
        try {
            X509EncodedKeySpec x509encodedkeyspec = new X509EncodedKeySpec(abyte);
            KeyFactory keyfactory = KeyFactory.getInstance("RSA");

            return keyfactory.generatePublic(x509encodedkeyspec);
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            ;
        } catch (InvalidKeySpecException invalidkeyspecexception) {
            ;
        }

        CryptManager.field_180198_a.error("Public key reconstitute failed!");
        return null;
    }

    public static SecretKey func_75887_a(PrivateKey privatekey, byte[] abyte) {
        return new SecretKeySpec(func_75889_b(privatekey, abyte), "AES");
    }

    public static byte[] func_75889_b(Key key, byte[] abyte) {
        return func_75885_a(2, key, abyte);
    }

    private static byte[] func_75885_a(int i, Key key, byte[] abyte) {
        try {
            return func_75886_a(i, key.getAlgorithm(), key).doFinal(abyte);
        } catch (IllegalBlockSizeException illegalblocksizeexception) {
            illegalblocksizeexception.printStackTrace();
        } catch (BadPaddingException badpaddingexception) {
            badpaddingexception.printStackTrace();
        }

        CryptManager.field_180198_a.error("Cipher data failed!");
        return null;
    }

    private static Cipher func_75886_a(int i, String s, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(s);

            cipher.init(i, key);
            return cipher;
        } catch (InvalidKeyException invalidkeyexception) {
            invalidkeyexception.printStackTrace();
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            nosuchalgorithmexception.printStackTrace();
        } catch (NoSuchPaddingException nosuchpaddingexception) {
            nosuchpaddingexception.printStackTrace();
        }

        CryptManager.field_180198_a.error("Cipher creation failed!");
        return null;
    }

    public static Cipher func_151229_a(int i, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");

            cipher.init(i, key, new IvParameterSpec(key.getEncoded()));
            return cipher;
        } catch (GeneralSecurityException generalsecurityexception) {
            throw new RuntimeException(generalsecurityexception);
        }
    }
}
