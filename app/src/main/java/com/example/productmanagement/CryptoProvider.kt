package com.example.productmanagement

import java.security.Provider
/*
加密使用？
 */
class CryptoProvider : Provider("Crypto",1.0,"HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)") {
    init {
        put("SecureRandom.SHA1PRNG", "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl")
        put("SecureRandom.SHA1PRNG ImplementedIn", "Software")
    }
}