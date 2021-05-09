package Account.AccountStore

import Account.Account
import Settings.Settings

interface AccountStore {
    fun updateAccount(UID: String, password: String);
    fun postAccount(login: String, password: String);
    fun deleteAccount(UID: String);
}

interface AccountLoader {
    fun getAllAccounts(): List<Account>;
}

class LocalAccountStore(_pathToData: String): AccountStore, AccountLoader {

    private val _pathToData = _pathToData;

    override fun getAllAccounts(): List<Account> {
        TODO("Not yet implemented")
    }

    override fun updateAccount(UID: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun postAccount(login: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun deleteAccount(UID: String) {
        TODO("Not yet implemented")
    }

}