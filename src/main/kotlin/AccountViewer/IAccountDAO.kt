package AccountViewer

import Account.Account

interface IAccountDAO {
    fun filterAccounts(param: String): List<String>
    fun getAccountPassword(UID: String): String
    fun updateAccount(UID: String, password: String, masterPassword: String)
    fun postAccount(login: String, password: String, masterPassword: String)
    fun deleteAccount(UID: String, masterPassword: String)

    class AccountNotFound(UID: String): Exception("Account with UID $UID not found!")
}