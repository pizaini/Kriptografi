/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beta01;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

/**
 *
 * @author Pizaini
 */
public class ReadPkc12 {
    public static void main(String[] args) throws KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        KeyStore pkcs12Store = KeyStore.getInstance("PKCS12", "BC");
        pkcs12Store.load(new FileInputStream("D:\\rootPrivateKeySS.p12"), "pass".toCharArray());
        System.out.println("########## KeyStore Dump. Size: "+pkcs12Store.size());
        
        //KeyPair
        PrivateKey privateKey = null;
        
        
        for (Enumeration en = pkcs12Store.aliases(); en.hasMoreElements();){
            String alias = (String) en.nextElement();
                if (pkcs12Store.isCertificateEntry(alias)) {
                    X509Certificate cc = (X509Certificate)pkcs12Store.getCertificate(alias);
                    X509CertificateHolder holder = new JcaX509CertificateHolder(cc);
                    
                    SubjectPublicKeyInfo pkInfo = holder.getSubjectPublicKeyInfo();
                    Certificate cert = pkcs12Store.getCertificate(alias);
                    //System.out.println("Signature: "+cert.getType()+" "+cert.getPublicKey());
                    PublicKey publicKey = cert.getPublicKey();
                    
                    System.out.println("Certificate Entry: " + alias + ", Subject: " +
                        (((X509Certificate)pkcs12Store.getCertificate(alias)).getSubjectDN()));
                    //test signing
                    Signature dsa = Signature.getInstance("SHA512withRSA");
                    String data = "DATA55555556666666...";
                    try {
                        dsa.initSign(privateKey);
                        dsa.update(data.getBytes());
                        byte[] realSign = dsa.sign();
                        //X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
                        //KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                        //pk2 = keyFactory.generatePublic(pubKeySpec);
                        dsa.initVerify(publicKey);
                        dsa.update(data.getBytes());
                        System.out.println("Is verivied? "+dsa.verify(realSign));
                    } catch (InvalidKeyException | SignatureException ex) {
                        Logger.getLogger(ReadPkc12.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if (pkcs12Store.isKeyEntry(alias)){
                    privateKey = (PrivateKey) pkcs12Store.getKey(alias, "pass".toCharArray());
                    System.out.println("Key Entry: ["+privateKey.getAlgorithm()+"]" + alias + ", Subject: " +
                        (((X509Certificate)pkcs12Store.getCertificate(alias)).getSubjectDN()));
                }
        }
        
        
        
    }
}
