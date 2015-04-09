/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beta01;

import cwguide.BcCredential;
import cwguide.BcUtils;
import cwguide.JcaUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

/**
 *
 * @author Pizaini
 */
public class SimpleGenCert {
    
    public SimpleGenCert() throws Exception {
         //BcCredential credential = BcUtils.createCredentials();
    }
    
    public static void main(String[] args) throws Exception {
       SimpleGenCert s = new SimpleGenCert();
        s.generateRoot();

        
        
    }
    
    private void generateRoot() throws Exception{
        KeyPair keyRoot = JcaUtils.generateRSAKeyPair();
        BcCredential rootCredential;
        rootCredential = BcUtils.createRootCredential();
        X509Certificate holderRoot = JcaUtils.buildRootCert(keyRoot);
      
        Certificate[]  chain = new Certificate[1];
        
        chain[0] = holderRoot;

        KeyStore pkcs12 = KeyStore.getInstance("PKCS12", "BC");
        pkcs12.load(null, null);
        //pkcs12.setCertificateEntry("r2oot", holderRoot);
        pkcs12.setKeyEntry("root", keyRoot.getPrivate(), null, chain);
        
        //store
        char[] password = "pass".toCharArray();
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        pkcs12.store(bOut, password);
        
        System.out.println("Public Key: "+keyRoot.getPublic());
        ASN1InputStream asnInput = new ASN1InputStream(bOut.toByteArray());
        bOut.reset();
//        DEROutputStream derOut = new BEROutputStream(bOut);
        //derOut.writeObject(asnInput.readObject());
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
        // reload from scratch
        pkcs12 = KeyStore.getInstance("PKCS12", "BC");

        pkcs12.load(new ByteArrayInputStream(bOut.toByteArray()), password);
        Enumeration en = pkcs12.aliases();
        while (en.hasMoreElements()){
            String alias = (String)en.nextElement();
            System.out.println("found " + alias+ ", isCertificate? " + pkcs12.isCertificateEntry(alias));
        }
       
      

    }
    
    private void generateRootBc(){
        
    }
    
    public void converToPem(X509CertificateHolder holder, String fileName) throws CertificateException, IOException{
        X509Certificate cert;
        cert = toX509Certificate(holder);
        System.out.println("Subject: "+cert.getSubjectX500Principal()+" - Issuer: "+cert.getIssuerX500Principal());
        
        //convert
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        JcaPEMWriter  pemWrt = new JcaPEMWriter(new OutputStreamWriter(bOut));
        pemWrt.writeObject(cert);
        pemWrt.close();
        
       //write to file
        try{
            File file = new File("D:\\"+fileName+".cer");
            FileOutputStream fos = new FileOutputStream(file);
            bOut.close();
            fos.write(bOut.toByteArray());
            fos.flush();
            fos.close();
        }catch(IOException ex){         
           
        }
        
        
        //System.out.println(new String(bOut.toByteArray()));
    }
    
    static void storeKey(){
        
    }
    
    private X509Certificate toX509Certificate(X509CertificateHolder holder) throws CertificateException{
        JcaX509CertificateConverter conv = new JcaX509CertificateConverter();
        conv.setProvider("BC");
        X509Certificate cert = conv.getCertificate(holder);
        return cert;
    }
}
