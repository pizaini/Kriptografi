/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beta01;

import beta01.SimpleGenCert;
import java.io.FileInputStream;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.SignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemReader;

/**
 *
 * @author Pizaini
 */
public class CreateCertByCsr {

    public CreateCertByCsr() throws Exception{
        //read p12
        KeyStore pkcs12Store = KeyStore.getInstance("PKCS12", "BC");
        pkcs12Store.load(new FileInputStream("D:\\rootPrivateKey.p12"), "pass".toCharArray());
        
        //read root key pair and certificate
        PrivateKey privateKey = null;
        PublicKey publicKey = null;
        X509Certificate rootCert = null;
        for (Enumeration en = pkcs12Store.aliases(); en.hasMoreElements();){
            String alias = (String) en.nextElement();
                if (pkcs12Store.isCertificateEntry(alias)) {
                    rootCert = (X509Certificate)pkcs12Store.getCertificate(alias);
                    Certificate cert = pkcs12Store.getCertificate(alias);
                    publicKey = cert.getPublicKey();
                }else if (pkcs12Store.isKeyEntry(alias)){
                    privateKey = (PrivateKey) pkcs12Store.getKey(alias, "pass".toCharArray());
                }
        }
        //read CSR
        String fileName = "CSR_DSA";
        FileReader fileReader = new FileReader("D:\\"+fileName+".p10");
        PemReader pemReader = new PemReader(fileReader);
        PKCS10CertificationRequest csr = new PKCS10CertificationRequest(pemReader.readPemObject().getContent());
        
      
        //create certf
        JcaX509CertificateHolder holder = new JcaX509CertificateHolder(rootCert);
        X509v3CertificateBuilder certBuilder;
        certBuilder = new X509v3CertificateBuilder(
                holder.getSubject(), BigInteger.valueOf(System.currentTimeMillis()), 
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()+ 7 * 24 * 60 * 60 * 1000), 
                csr.getSubject(), csr.getSubjectPublicKeyInfo());
        certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature));
        
        SignatureAlgorithmIdentifierFinder algFinder = new DefaultSignatureAlgorithmIdentifierFinder();
        AlgorithmIdentifier sigAlg = algFinder.find("SHA512withRSA");
        AlgorithmIdentifier digAlg = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlg);
        
        //RSAPrivateKey rsa = (RSAPrivateKey) privateKey;
        //AsymmetricCipherKeyPair ss =new AsymmetricCipherKeyPair
       // RSAKeyParameters rsaP = new RSAPrivateCrtKeyParameters(rsa.getModulus(), rsa.getPublicExponent(), 
               // rsa.getPrivateExponent(), rsa., BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE);
        //ContentSigner signer = new BcRSAContentSignerBuilder(sigAlg, digAlg).build((AsymmetricKeyParameter) privateKey);
       
        
       // AsymmetricCipherKeyPair sd = new AsymmetricCipherKeyPair(null, null)
        
        ContentSigner signer = new JcaContentSignerBuilder("SHA512withRSA").setProvider("BC").build(privateKey);
        X509CertificateHolder holder2 = certBuilder.build(signer);
        new SimpleGenCert().converToPem(holder2, fileName);
    }
    

    
    public static void main(String[] args) throws Exception {
        CreateCertByCsr csr = new CreateCertByCsr();
    }
}