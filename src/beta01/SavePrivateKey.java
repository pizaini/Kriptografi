/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beta01;

import cwguide.JcaUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 *
 * @author Pizaini
 */
public class SavePrivateKey {
    private final String PASSWORD = "pass";

    public SavePrivateKey() throws Exception {
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
        kpGen.initialize(2048, new SecureRandom());
        KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
        keyStore.load(null, null);
        
        X509Certificate cert = JcaUtils.buildRootCert(kpGen.genKeyPair());
        Certificate[]  chain = new Certificate[1]; 
        chain[0] = cert;
        
        keyStore.setKeyEntry("private key", kpGen.generateKeyPair().getPrivate(), null, chain);
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        keyStore.store(bOut, PASSWORD.toCharArray());
        byte[] derFormat = bOut.toByteArray();
        try{
            File file = new File("D:\\rootPrivateKeySS.p12");
            FileOutputStream fos = new FileOutputStream(file);
            bOut.close();
            fos.write(derFormat);
            fos.flush();
            fos.close();
        }catch(IOException ex){         
           
        }
        
    }
    
    public static void main(String[] args) throws Exception {
        SavePrivateKey savePrivateKey = new SavePrivateKey();
    }
}
