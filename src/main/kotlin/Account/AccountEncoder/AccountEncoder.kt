package Account.AccountEncoder

import Account.Account
import Account.DefaultAccount
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.SecretKeySpec

interface AccountEncoder {
    fun encodeAccount(account: Account): Account
    fun decodeAccount(account: Account): Account
    fun encodeOnlyPassword(account: Account): Account
    fun decodeOnlyPassword(account: Account): Account
    fun encodeString(data: String): String
    fun decodeString(data: String): String
    fun getWithNewKey(key: String): AccountEncoder
}

abstract class AbstractAccountEncoder(_masterPass: String) : AccountEncoder {

    protected val masterPass = _masterPass


    override fun encodeAccount(account: Account): Account =
        DefaultAccount(
            encodeString(account.getUID()),
            encodeString(account.getLogin()),
            encodeString(account.getPassword())
        )

    override fun decodeAccount(account: Account): Account =
        DefaultAccount(
            decodeString(account.getUID()),
            decodeString(account.getLogin()),
            decodeString(account.getPassword())
        )

    override fun encodeOnlyPassword(account: Account): Account =
        DefaultAccount(
            account.getUID(),
            account.getLogin(),
            encodeString(account.getPassword())
        )

    override fun decodeOnlyPassword(account: Account): Account =
        DefaultAccount(
            account.getUID(),
            account.getLogin(),
            decodeString(account.getPassword())
        )

    companion object {
        fun deserialize(data: String): AbstractAccountEncoder =
            when (data) {
                "Aes" -> AesEncoder()
                "Des" -> DesEncoder()
                else -> AesEncoder()
            }
    }

    abstract fun serialize(): String
}

class AesEncoder(_masterPass: String = "") : AbstractAccountEncoder(_masterPass) {
    private fun getAesKey() = masterPass.padStart(16).slice(0..15)

    override fun encodeString(data: String): String {
        val cipher = Cipher.getInstance("AES")

        val keySpec = SecretKeySpec(getAesKey().toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)

        val encrypt = cipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encrypt)
    }

    override fun decodeString(data: String): String {
        val cipher = Cipher.getInstance("AES")

        val keySpec: SecretKeySpec = SecretKeySpec(getAesKey().toByteArray(), "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)

        val encrypt = cipher.doFinal(Base64.getDecoder().decode(data))
        return String(encrypt)
    }

    override fun getWithNewKey(key: String) =
        AesEncoder(key)

    override fun serialize() =
        "Aes"
}

class DesEncoder(_masterPass: String = "") : AbstractAccountEncoder(_masterPass) {
    private fun getDesKey() = masterPass.padStart(8).slice(0..7)

    override fun encodeString(data: String): String {
        val c = Cipher.getInstance("DES")

        val kf = SecretKeyFactory.getInstance("DES")
        val keySpec = DESKeySpec(getDesKey().toByteArray())

        val key = kf.generateSecret(keySpec)
        c.init(Cipher.ENCRYPT_MODE, key)

        val encrypt = c.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encrypt)
    }

    override fun decodeString(data: String): String {
        val c = Cipher.getInstance("DES")

        val kf = SecretKeyFactory.getInstance("DES")
        val keySpec = DESKeySpec(getDesKey().toByteArray())

        val key = kf.generateSecret(keySpec)
        c.init(Cipher.DECRYPT_MODE, key)

        val encrypt = c.doFinal(Base64.getDecoder().decode(data))
        return String(encrypt)
    }

    override fun getWithNewKey(key: String) =
        DesEncoder(key)

    override fun serialize() =
        "Des"
}