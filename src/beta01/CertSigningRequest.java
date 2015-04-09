/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beta01;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

/**
 *
 * @author Pizaini
 * email 
 * web
 */
public class CertSigningRequest {

    public CertSigningRequest() {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    
    
    private void genaretKeyPairDsa() throws Exception{
        String signatureAlg = "SHA1withDSA";
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA", "BC");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        
        X500NameBuilder x500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        x500NameBuilder.addRDN(BCStyle.C, "ID");
        x500NameBuilder.addRDN(BCStyle.CN, "Pizaini");
        //x500NameBuilder.addRDN(BCStyle.O, "Institut Pertanian Bogor");

        X500Name subject = x500NameBuilder.build();
        
        PKCS10CertificationRequestBuilder requestBuilder = new JcaPKCS10CertificationRequestBuilder(subject, kp.getPublic());
        try {
            PKCS10CertificationRequest request = requestBuilder.build(new JcaContentSignerBuilder(signatureAlg).setProvider("BC").build(kp.getPrivate()));
            
            //verify signature
            if (request.isSignatureValid(new JcaContentVerifierProviderBuilder().setProvider("BC").build(kp.getPublic())) ) {
                System.out.println(signatureAlg + ": PKCS#10 request verified.");
                //CSR Output
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //PemWriter pemWrtb = new PemWriter(new OutputStreamWriter(baos));
                JcaPEMWriter jcaPem = new JcaPEMWriter(new OutputStreamWriter(baos));
                jcaPem.writeObject(request);
                jcaPem.close();
                try{
                    File file = new File("D:\\CSR_"+kpg.getAlgorithm()+".p10");
                    FileOutputStream fos = new FileOutputStream(file);
                    baos.close();
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                }catch(IOException ex){         

                }
                
                //store Private Key p8   
                try{
                    File file = new File("D:\\PrivateKey_"+kpg.getAlgorithm()+".p8");
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(kp.getPrivate().getEncoded());
                    fos.flush();
                    fos.close();
                    System.out.println("Privated key stored as "+kp.getPrivate().getFormat());
                }catch(IOException ex){         
                }
                
                //p12
                /*KeyStore pkcs12 = KeyStore.getInstance("PKCS12", "BC");
                pkcs12.load(null, null);
                //pkcs12.setCertificateEntry("r2oot", holderRoot);
                pkcs12.setKeyEntry("PIZAINI_ECDSA", kp.getPrivate(), null, null);
                char[] password = "pass".toCharArray();
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                pkcs12.store(bOut, password);
                
                ASN1InputStream asnInput = new ASN1InputStream(bOut.toByteArray());
                bOut.reset();
                DEROutputStream derOut = new DEROutputStream(bOut);
                derOut.writeObject(asnInput.readObject());
                byte[] derFormat = bOut.toByteArray();
                try{
                File file = new File("D:\\Pizaini_ECDSA_Private.p12");
                FileOutputStream fos = new FileOutputStream(file);
                bOut.close();
                fos.write(derFormat);
                fos.flush();
                fos.close();
                }catch(IOException ex){
                
                }*/
                
                
            } else {
                System.out.println(signatureAlg + ": Failed verify check.");
            }
        } catch (OperatorCreationException | PKCSException ex) {
            
        }
        

        
    }
    
    private void generateKeyPairEcdsa(){
        
    }
    
    private void showPemFormat(){
        
    }
    
    public static void main(String[] args) throws Exception {
        CertSigningRequest csr = new CertSigningRequest();
        try {
            csr.genaretKeyPairDsa();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException ex) {
        }
    }
}