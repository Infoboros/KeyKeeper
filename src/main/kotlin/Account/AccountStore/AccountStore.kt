package Account.AccountStore

import Account.Account

interface PasswordStore {
    fun updatePassword(UID: String, password: String);
    fun postPassword(login: String, password: String);
    fun deletePassword();
}

interface AccountLoader {
    fun getAllAccounts(): List<Account>;
}

class LocalAccountStore(_pathToData: String): PasswordStore, AccountLoader {

    private val pathToData = _pathToData;

    override fun getAllAccounts(): List<Account> {
        TODO("Not yet implemented")
    }

    override fun updatePassword(UID: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun postPassword(login: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun deletePassword() {
        TODO("Not yet implemented")
    }

}