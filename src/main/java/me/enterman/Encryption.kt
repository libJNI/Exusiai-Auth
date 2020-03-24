package me.enterman

import oshi.SystemInfo
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigInteger
import java.net.URL
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

//import oshi.SystemInfo;
//import oshi.hardware.Processor;
fun main() {
    val kp:KeyPair = Encryption.genKeyPair();
    println(Base64.getEncoder().encodeToString(kp.private.encoded))
    println(Base64.getEncoder().encodeToString(kp.public.encoded))
}
class Encryption {
    // var deleteThisBecauseForGeneratingPasteBin = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClfOuuUlWE8RifLJJr/7W7USJ9Ci9eDFY4OyqSVRpaOcxMk1uNEvjFb9LRaCXk9DzHxbnjn05SN5UE79FuQPd3ZrSGE/zqc7SIm1Sgf8H64B0ZZtJAQDTBPmLk7v+LAKm8dI2sMOCV5XRUUXC4+ARysiqjYOvFRTXObfvyhJThWwIDAQAB"

    companion object {
        var pri = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKV8665SVYTxGJ8skmv/tbtRIn0KL14MVjg7KpJVGlo5zEyTW40S+MVv0tFoJeT0PMfFueOfTlI3lQTv0W5A93dmtIYT/OpztIibVKB/wfrgHRlm0kBANME+YuTu/4sAqbx0jaww4JXldFRRcLj4BHKyKqNg68VFNc5t+/KElOFbAgMBAAECgYBEWDUVh1deUhvzEPtfsvmg5L8zmNM7Kikpi/Xm/VKm1Jv1PB5hQuaO08HjTfnodp3re1NsGEzGU8IticWanSYV9LBNdjI4dBzBCuwQs8hu/wFukaXZCgm9FG3FrpaFpBgyEPqQ24IM+++7s1ndmO3qWeFg1uXIXDh99c83xes5AQJBAOaEEdLkYEDV5fR9tsUWx/Str5/whYmO/pnowejFw2z7cUH3nAra1E+qDP1S9XnV5YmkoyR5IAYRf4Zs92tNqpsCQQC3yHesadZ3wMFDFMY0e1A1Q1w0EKyVcPjbMfMWABTYigw7TQIckw84LvT0dWB+gg5+bUUb+xlM+xZD15F51rBBAkA96KBk1ELphsjQhebNPdYL542wVcd3bzj/mtxhKKPYpBsBzX9SBv1YO+JrMpmM1B5mfhn2lLU+C/sE3kUAw4YbAkBiayBrXGJVzwuv+LgL8t2JKIGAAE3r2YvrFlyvM2v2ajcSqrKuyrIjzEGxiAoctVk9qxaPXXC3IN9ThWWKccYBAkBGxMvKlN83+Cc79CwliO1bhpDvKPDfYjZC8geOF8Iy8eBr+C4tqPLRWmJiwR1huvMuTLjf5Zb7aY37OAJyzToY"

        @Throws(NoSuchAlgorithmException::class)
        fun hid(): String {
            val systemInfo = SystemInfo()
            val tohash = StringBuffer()
            val p = systemInfo.hardware.processor.processorIdentifier
            tohash.append(p.family)
            tohash.append(p.identifier)
            tohash.append(p.isCpu64bit)
            tohash.append(p.model)
            tohash.append(p.processorID)
            tohash.append(systemInfo.operatingSystem.family)
            tohash.append(systemInfo.operatingSystem.manufacturer)
            tohash.append(System.getenv("COMPUTERNAME"))
            return secureHash(tohash.toString())
        }
        @Throws(IllegalBlockSizeException::class, InvalidKeyException::class, BadPaddingException::class, NoSuchAlgorithmException::class, NoSuchPaddingException::class)
        fun decrypt(data: String, base64PrivateKey: String): String {
            return decrypt(Base64.getDecoder().decode(data.toByteArray()), getPrivateKey(base64PrivateKey))
        }

        @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class, BadPaddingException::class, IllegalBlockSizeException::class)
        fun decrypt(data: ByteArray?, privateKey: PrivateKey?): String {
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            return String(cipher.doFinal(data))
        }


        fun getPrivateKey(base64PrivateKey: String): PrivateKey? {
            var privateKey: PrivateKey? = null
            val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.toByteArray()))
            var keyFactory: KeyFactory? = null
            try {
                keyFactory = KeyFactory.getInstance("RSA")
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            try {
                privateKey = keyFactory!!.generatePrivate(keySpec)
            } catch (e: InvalidKeySpecException) {
                e.printStackTrace()
            }
            return privateKey
        }

        @Throws(NoSuchAlgorithmException::class)
        fun secureHash(`in`: String): String {
            var `in` = `in`
            val CHARSET = Arrays.asList(*"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split("".toRegex()).toTypedArray())
            `in` = md5(`in`)
            val r = Random(ByteBuffer.wrap(`in`.toByteArray()).long)
            CHARSET.shuffle(r)
            val inBuilder = StringBuilder()
            for (i in 0..63) {
                inBuilder.append(CHARSET[r.nextInt(CHARSET.size)])
            }
            `in` = inBuilder.toString()
            return `in`
        }

        @Throws(NoSuchAlgorithmException::class)
        fun md5(`in`: String): String {
            val md = MessageDigest.getInstance("MD5")
            md.update(`in`.toByteArray(StandardCharsets.UTF_8))
            return BigInteger(1, md.digest()).toString(16)
        }

        @Throws(NoSuchAlgorithmException::class)
        fun genKeyPair(): KeyPair {
            val keyGen = KeyPairGenerator.getInstance("RSA")
            keyGen.initialize(1024)
            return keyGen.genKeyPair()
        }

        @Throws(BadPaddingException::class, IllegalBlockSizeException::class, InvalidKeyException::class, NoSuchPaddingException::class, NoSuchAlgorithmException::class)
        fun encrypt(data: String, publicKey: String): ByteArray {
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey))
            return cipher.doFinal(data.toByteArray())
        }

        fun getPublicKey(base64PublicKey: String): PublicKey? {
            var publicKey: PublicKey? = null
            try {
                val keySpec = X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.toByteArray()))
                val keyFactory = KeyFactory.getInstance("RSA")
                publicKey = keyFactory.generatePublic(keySpec)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: InvalidKeySpecException) {
                e.printStackTrace()
            }
            return publicKey
        }

        @Throws(IOException::class)
        operator fun get(url: String?): String {
            val u = URL(url)
            val uc = u.openConnection()
            val sb = StringBuilder()
            var s: String?
            val br = BufferedReader(InputStreamReader(uc.getInputStream()))
            while (br.readLine().also { s = it } != null) {
                sb.append(s)
            }
            return sb.toString()
        }
    }
}