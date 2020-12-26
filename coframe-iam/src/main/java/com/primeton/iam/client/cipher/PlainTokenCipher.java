package com.primeton.iam.client.cipher;

/**
 * @author huzd@primeton.com
 */
public class PlainTokenCipher implements TokenCipher {

    @Override
    public String encrypt(String token) {
        return token;
    }

    @Override
    public String decrypt(String token) {
        return token;
    }

}
