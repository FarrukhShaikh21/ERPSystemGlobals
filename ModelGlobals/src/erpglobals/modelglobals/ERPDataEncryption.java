package erpglobals.modelglobals;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ERPDataEncryption {
   
    public ERPDataEncryption() {
        super();
    }
 //$rPl0G!NK$Ysyste
    private static final String ERPERPALGORITHMTYPE = "AES";
    private static final byte[] ERPkeyValue =
            new byte[] { '$', 'r', 'P', 'l', '0', 'G', '!', 'N', 'K','$','Y','s','y','s','t','E'};
 
 public static void main(String[] args) {
        try {
//            ERPdoEncrypt("DgMCm786");
//            ERPdoDecrypt("4A0A93688CBF7D1155F9F5C29D9F22B5");
            //2vCbqN65k8ld7kAndNMkgQ==
            //djJ9EGKW3TiqBsOcvfNsWQ==
        } catch (Exception e) {
            
        }
    }
    public static String ERPdoEncrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ERPERPALGORITHMTYPE);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encValue);
        System.out.println(encryptedValue);
        return encryptedValue;
    }
    public static String ERPdoDecrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ERPERPALGORITHMTYPE);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue 
                = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        System.out.println(decryptedValue);
        return decryptedValue;
    }
    private static Key generateKey() throws Exception {
        Key erpKey = new SecretKeySpec(ERPkeyValue, ERPERPALGORITHMTYPE);
        return erpKey;
    }
}