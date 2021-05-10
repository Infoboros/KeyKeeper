package Account.AccountEncoder

import Account.Account
import Account.DefaultAccount

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
                "Gost" -> GostEncoder()
                "Idea" -> IdeaEncoder()
                else -> AesEncoder()
            }
    }

    abstract fun serialize(): String
}

class AesEncoder(_masterPass: String = "") : AbstractAccountEncoder(_masterPass) {
    override fun encodeString(data: String): String {
        return data + masterPass
    }

    override fun decodeString(data: String): String {
        return data.removeSuffix(masterPass)
    }

    override fun getWithNewKey(key: String) =
        AesEncoder(key)

    override fun serialize() =
        "Aes"
}

class GostEncoder(_masterPass: String = "") : AbstractAccountEncoder(_masterPass) {
    override fun encodeString(data: String): String {
        return data + masterPass
    }

    override fun decodeString(data: String): String {
        return data.removeSuffix(masterPass)
    }

    override fun getWithNewKey(key: String) =
        GostEncoder(key)

    override fun serialize() =
        "Gost"
}

class IdeaEncoder(_masterPass: String = "") : AbstractAccountEncoder(_masterPass) {
    override fun encodeString(data: String): String {
        return data + masterPass
    }

    override fun decodeString(data: String): String {
        return data.removeSuffix(masterPass)
    }

    override fun getWithNewKey(key: String) =
        IdeaEncoder(key)

    override fun serialize() =
        "Idea"
}