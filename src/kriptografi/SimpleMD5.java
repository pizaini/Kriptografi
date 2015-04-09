/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kriptografi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Pizaini
 */
public class SimpleMD5 {
    public static void main(String[] arg) throws NoSuchAlgorithmException{
        String text = "ARaihlah tentang apapun yang ingin kamu cita-citakan, pergilah ke tempat-tempat kamu ingin pergi, jadilah dirimu seperti apa yang kamu inginkan, karena engkau hanya memiliki sebuah kehidupan dan satu kesempatan untuk melakukan sesuatu yang ingin kamu lakukan";
        MessageDigest digest = MessageDigest.getInstance("md5");
        byte[] output = digest.digest(text.getBytes());
        System.out.println("MD5: "+ DatatypeConverter.printHexBinary(output));
    }
}
