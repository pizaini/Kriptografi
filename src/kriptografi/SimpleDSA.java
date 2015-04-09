/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kriptografi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Pizaini
 */
public class SimpleDSA {
    public static void main(String [] a) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException{
        String data = "Ini adalah dokumen dengan digital signature";
        //key pairs gen
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        keyGen.initialize(512, random);
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        PublicKey pub = pair.getPublic();
        //System.out.println("Public Key: "+pub);
        
        //sign the data - Proses penandaan
        Signature dsa = Signature.getInstance("SHA1withDSA");
        dsa.initSign(priv);
        
        //verify algorithm Type from Public Key
        PublicKey typePub = pair.getPublic();
        System.out.println("Key type: "+typePub.getAlgorithm()+" - "+typePub.getEncoded().length);
       
        dsa.update(data.getBytes());
        byte[] realSign = dsa.sign();
        System.out.println("Signed: "+realSign.length+" " +DatatypeConverter.printHexBinary(realSign));
        
        //save to file
        try{
            FileOutputStream fos = new FileOutputStream("signatureDSA");
            fos.write(realSign);
            fos.close();
        }catch(Exception exc){
        }
        //verify
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pub.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        
        try {
            PublicKey pk2 = keyFactory.generatePublic(pubKeySpec);
            System.out.println("Compare 2 public key "+Arrays.toString(pub.getEncoded()));
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(SimpleDSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dsa.initVerify(pub);
        dsa.update(data.getBytes());
        System.out.println("Is verivied? "+dsa.verify(realSign));
    }
}
