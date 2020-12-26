package com.primeton.iam.client.cipher;

/**
 * @author huzd@primeton.com
 */
public interface TokenCipher {
    /**
     * 对 token 进行加密
     *
     * @param token token
     * @return 加密后的 token
     */
    String encrypt(String token);

    /**
     * 解密 token
     *
     * @param token 加密的 token
     * @return
     */
    String decrypt(String token);
}
