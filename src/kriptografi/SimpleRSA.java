/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kriptografi;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Pizaini
 */
public class SimpleRSA {
    public static void main(String[] args) throws Exception {
        final String alg = "RSA/ECB/PKCS1Padding";
    String inputStr = "Jadilah dirimu seperti apa yang kamu inginkan, karena engkau hanya memiliki sebuah kehidupan dan satu kesempatan untuk melakukan sesuatu yang ingin kamu lakukan";
    byte[] input = inputStr.getBytes("UTF8");
    Cipher cipher = Cipher.getInstance(alg);

    //KeyFactory keyFactory = KeyFactory.getInstance(alg);
    //key gen
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        
        //param
        System.out.println("Public: "+publicKey);
        System.out.println("Private: "+privateKey);
        byte[] pub = publicKey.getPublicExponent().toByteArray();
        System.out.println("Public: "+ new String(pub));
        byte[] priv = privateKey.getPrivateExponent().toByteArray();
        System.out.println("Private: "+ DatatypeConverter.printHexBinary(priv));

   // RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger("12345678", 16), new BigInteger("11", 16));
    //RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(new BigInteger("12345678", 16), new BigInteger("12345678", 16));

   // RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
   // RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);

    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte[] cipherText = cipher.doFinal(input);
   // System.out.println("Input: " + DatatypeConverter.printHexBinary(input));
    System.out.println("cipher: " + DatatypeConverter.printHexBinary(cipherText));

    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] plainText = cipher.doFinal(cipherText);
   // System.out.println("plain : " + DatatypeConverter.printHexBinary(plainText));
        System.out.println("Plaintext is: "+Arrays.equals(input, plainText));
        
        //System.out.println("Param: "+cipher.getBlockSize());
  }
}
