/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beta01;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.cert.X509Certificate;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;

/**
 *
 * @author Pizaini
 */
public class ReadCertificate {

    public ReadCertificate() throws Exception {
        //input certificate
        //FileReader fileReader = new FileReader("D:\\CSR_ECDSA.cer");
        //PemReader pemReader = new PemReader(fileReader);
        //Object obj = pemReader.readPemObject();
        //pemReader.close();
        
        InputStream inStream = new FileInputStream("D:\\CSR_ECDSA.cer");
        X509Certificate cert = X509Certificate.getInstance(inStream);
        inStream.close();
        
        //read public key
        System.out.println("Public Key: "+cert.getPublicKey());
        
        //input and read private key p8
        InputStream privFile = new FileInputStream("D:\\PrivateKey_ECDSA.p8");
        byte[] privaData = new byte[privFile.available()];  
        privFile.read(privaData);
        privFile.close();
        AsymmetricKeyParameter asym = PrivateKeyFactory.createKey(privaData);
        
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(privaData);
        KeyFactory kf = KeyFactory.getInstance("ECDSA", "BC");
        PrivateKey pk = kf.generatePrivate(pkcs8);
        System.out.println("Private PKCS#8: "+pk);
        
        //try signature
        //test signing
        Signature dsa = Signature.getInstance("SHA256withECDSA", "BC");
        String data = "Testing...";
        try {
            dsa.initSign(pk);
            dsa.update(data.getBytes());
            byte[] realSign = dsa.sign();
            dsa.initVerify(cert.getPublicKey());
            dsa.update(data.getBytes());
            System.out.println("Is verivied? "+dsa.verify(realSign));
        } catch (InvalidKeyException | SignatureException ex) {
            Logger.getLogger(ReadPkc12.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) throws Exception {
        ReadCertificate readCertificate = new ReadCertificate();
    }
}
