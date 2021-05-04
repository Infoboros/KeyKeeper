package AccountEncoder

import Account.Account
import Account.DefaultAccount

interface AccountEncoder {
    fun encodeAccount(account: Account): Account;
    fun decodeAccount(account: Account): Account;
    fun encodeOnlyPassword(account: Account): Account;
    fun decodeOnlyPassword(account: Account): Account;
    fun setKey(key: String);
}

abstract class AbstractAccountEncoder : AccountEncoder {

    protected var masterPass = "";

    protected abstract fun encodeString(data: String): String;

    protected abstract fun decodeString(data: String): String;

    override fun encodeAccount(account: Account): Account =
            DefaultAccount(
                    encodeString(account.getID()),
                    encodeString(account.getLogin()),
                    encodeString(account.getPassword())
            )

    override fun decodeAccount(account: Account): Account =
            DefaultAccount(
                    decodeString(account.getID()),
                    decodeString(account.getLogin()),
                    decodeString(account.getPassword())
            )

    override fun encodeOnlyPassword(account: Account): Account =
            DefaultAccount(
                    account.getID(),
                    account.getLogin(),
                    encodeString(account.getPassword())
            )

    override fun decodeOnlyPassword(account: Account): Account =
            DefaultAccount(
                    account.getID(),
                    account.getLogin(),
                    decodeString(account.getPassword())
            )

    override fun setKey(key: String) {
        masterPass = key;
    }
}

class AesEncoder : AbstractAccountEncoder() {
    override fun encodeString(data: String): String {
        return data + masterPass;
    }

    override fun decodeString(data: String): String {
        return data.removeSuffix(data)
    }
}

class GostEncoder : AbstractAccountEncoder() {
    override fun encodeString(data: String): String {
        return data + masterPass;
    }

    override fun decodeString(data: String): String {
        return data.removeSuffix(data)
    }
}

class IdeaEncoder : AbstractAccountEncoder() {
    override fun encodeString(data: String): String {
        return data + masterPass;
    }

    override fun decodeString(data: String): String {
        return data.removeSuffix(data)
    }
}