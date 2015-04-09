/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kriptografi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Pizaini
 */
public class DESEncryption {
    private static final String UNICODE_FORMAT = "UTF8";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    private byte[] keyAsBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    private SecretKey key;
 
    public DESEncryption() throws Exception {
        myEncryptionKey = "iniadalahkuncirahasia";
        myEncryptionScheme = "DES";
        keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        myKeySpec = new DESKeySpec(keyAsBytes);
        mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = mySecretKeyFactory.generateSecret(myKeySpec);
    }
 
    /**
     * Method To Encrypt The String
     */
    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            System.out.println("Hex: "+ cipher.getBlockSize());

            BASE64Encoder base64encoder = new BASE64Encoder();
            encryptedString = base64encoder.encode(encryptedText);
            //encryptedString = DatatypeConverter.printHexBinary(encryptedText);
        } catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
        }
        return encryptedString;
    }
    /**
     * Method To Decrypt An Ecrypted String
     */
    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] encryptedText = base64decoder.decodeBuffer(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= bytes2String(plainText);
        } catch (InvalidKeyException | IOException | IllegalBlockSizeException | BadPaddingException e) {
        }
        return decryptedText;
    }
    /**
     * Returns String From An Array Of Bytes
     */
    private static String bytes2String(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }
 
    /**
     * Testing the DES Encryption And Decryption Technique
     * @param args
     * @throws java.lang.Exception
     */
    public static void main(String args []) throws Exception {
        DESEncryption myEncryptor= new DESEncryption();
 
        String stringToEncrypt = "Raihlah tentang apapun yang ingin kamu cita-citakan, pergilah ke tempat-tempat kamu ingin pergi, jadilah dirimu seperti apa yang kamu inginkan, karena engkau hanya memiliki sebuah kehidupan dan satu kesempatan untuk melakukan sesuatu yang ingin kamu lakukan";
        String encrypted = myEncryptor.encrypt(stringToEncrypt);
        String decrypted = myEncryptor.decrypt(encrypted);
 
        System.out.println("String To Encrypt: "+stringToEncrypt);
        System.out.println("Encrypted Value: " + encrypted);
        System.out.println("Decrypted Value: "+decrypted);

    } 
}
