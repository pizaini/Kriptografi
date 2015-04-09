/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kriptografi;

import java.security.Provider;
import java.security.Security;

/**
 *
 * @author Pizaini
 */
public class TestProvider {
    public static void main(String[] abc) {
        String providerName = "BC";
        if (Security.getProvider(providerName) != null){
            System.out.println(providerName + " is installed.");
            Provider[] providers = Security.getProviders();
            //Security.addProvider(new BouncyCastleProvider());
            for (Provider prov : providers) {
                //System.out.println(prov.getInfo());
                
            }
        }else {
            System.out.println(providerName + " provider not installed");
        }
    }
}