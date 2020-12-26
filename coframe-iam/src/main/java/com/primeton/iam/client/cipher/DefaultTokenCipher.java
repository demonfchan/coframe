package com.primeton.iam.client.cipher;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author huzd@primeton.com
 */
public class DefaultTokenCipher implements TokenCipher, InitializingBean {
	
	Logger log = LoggerFactory.getLogger(DefaultTokenCipher.class);
	
    private String algorithm = "AES/CBC/PKCS5Padding";

    private String key;
    private String ivParam = "0123456789abcdef";

    private SecretKey secretKey;
    private IvParameterSpec ivParameterSpec;
    private Charset utf8 = Charset.forName("UTF8");

    @Override
    public String encrypt(String token) {
        if (this.secretKey == null || token == null) {
            return token;
        }
        try {
            Cipher encodeCipher = Cipher.getInstance(getAlgorithm());
            encodeCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] bytes = encodeCipher.doFinal(token.getBytes(utf8));
            return Base64.getEncoder().encodeToString(bytes);
//          return Base64.getUrlEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("cookie token error:" + e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String token) {
        if (this.secretKey == null || token == null) {
            return token;
        }
        try {
            Cipher decodeCipher = Cipher.getInstance(getAlgorithm());
            decodeCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] bytes = Base64.getDecoder().decode(token.getBytes(utf8));
//          byte[] bytes = Base64.getUrlDecoder().decode(token.getBytes(utf8));
            byte[] ret = decodeCipher.doFinal(bytes);
            return new String(ret, utf8);
        } catch (Exception e) {
            throw new RuntimeException("cookie token error:" + e.getMessage(), e);
        }
    }


    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setIvParam(String ivParam) {
        this.ivParam = ivParam;
    }

    public String getKey() {
        return key;
    }

    public String getIvParam() {
        return ivParam;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public IvParameterSpec getIvParameterSpec() {
        return ivParameterSpec;
    }

    public void setIvParameterSpec(IvParameterSpec ivParameterSpec) {
        this.ivParameterSpec = ivParameterSpec;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.getKey() != null) {
            byte[] buf = Arrays.copyOf(this.key.getBytes(utf8), 16);
            this.setSecretKey(new SecretKeySpec(buf,
                    getAlgorithm().substring(0, getAlgorithm().indexOf("/"))));
        }
        if (this.getIvParam() != null) {
            this.setIvParameterSpec(new IvParameterSpec(this.ivParam.getBytes(utf8)));
        }
    }
}
