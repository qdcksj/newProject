package com.example.productmanagement

import android.os.Build
import android.text.TextUtils
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.SecureRandom.*
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESUtils {
    private val SHA1PRNG = "SHA1PRNG" // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
    private val IV = "qws871bz73msl9x8"
    private val AES = "AES" //AES 加密
    private val CIPHERMODE = "AES/CBC/PKCS5Padding" //algorithm/mode/padding

    /**
     * 加密
     */
    fun encrypt(key: String, cleartext: String): String? {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext
        }
        try {
            val result = encrypt(key, cleartext.toByteArray())
            return parseByte2HexStr(result)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 加密
     */
    @Throws(Exception::class)
    fun encrypt(key: String, clear: ByteArray?): ByteArray {
        val raw = getRawKey(key.toByteArray())
        val skeySpec = SecretKeySpec(raw, AES)
        val cipher: Cipher = Cipher.getInstance(CIPHERMODE)
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(clear)
    }

    /**
     * 解密
     */
    fun decrypt(key: String, encrypted: String): String? {
        if (TextUtils.isEmpty(encrypted)) {
            return encrypted
        }
        try {
            val enc = parseHexStr2Byte(encrypted)
            val result = decrypt(key, enc)
            return String(result)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 解密
     */
    @Throws(Exception::class)
    fun decrypt(key: String, encrypted: ByteArray?): ByteArray {
        val raw = getRawKey(key.toByteArray())
        val skeySpec = SecretKeySpec(raw, AES)
        val cipher: Cipher = Cipher.getInstance(CIPHERMODE)
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(encrypted)
    }

    /**
     * 生成随机数，可以当做动态的密钥
     * 加密和解密的密钥必须一致，不然将不能解密
     */
    fun generateKey(): String? {
        try {
            val secureRandom: SecureRandom = getInstance(SHA1PRNG)
            val key = ByteArray(20)
            secureRandom.nextBytes(key)
            return toHex(key)
        }
        catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 对密钥进行处理
     */
    @Throws(Exception::class)
    fun getRawKey(seed: ByteArray?): ByteArray {
        val kgen: KeyGenerator = KeyGenerator.getInstance(AES)
        //for android
        var sr: SecureRandom? = null
        // 在4.2以上版本中，SecureRandom获取方式发生了改变
        //crypto 加密
        sr = if (Build.VERSION.SDK_INT >= 17) {
            getInstance(SHA1PRNG, CryptoProvider())
        }
        else {
            getInstance(SHA1PRNG)
        }
        // for Java
        // secureRandom = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(seed)
        kgen.init(128, sr) //256 bits or 128 bits,192bits
        //AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
        val skey: SecretKey = kgen.generateKey()
        return skey.getEncoded()
    }

    /**
     * 二进制转字符
     */
    private fun toHex(buf: ByteArray?): String? {
        if (buf == null) return ""
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i])
        }
        return result.toString()
    }

    private fun appendHex(sb: StringBuffer, b: Byte) {
        sb.append(IV[b.toInt() shr 4 and 0x0f]).append(IV[b.toInt() and 0x0f])
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private fun parseByte2HexStr(buf: ByteArray): String? {
        val sb = StringBuilder()
        for (i in buf.indices) {
            var hex = Integer.toHexString(buf[i].toInt() and 0xFF)
            if (hex.length == 1) {
                hex = "0$hex"
            }
            sb.append(hex.toUpperCase(Locale.ROOT))
        }
        return sb.toString()
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private fun parseHexStr2Byte(hexStr: String): ByteArray? {
        if (hexStr.isEmpty()) return null
        val result = ByteArray(hexStr.length / 2)
        for (i in 0 until hexStr.length / 2) {
            val high = hexStr.substring(i * 2, i * 2 + 1).toInt(16)
            val low = hexStr.substring(i * 2 + 1, i * 2 + 2).toInt(16)
            result[i] = (high * 16 + low).toByte()
        }
        return result
    }
}
