package AccountViewer

import Account.Account

interface IAccountCRUD {
    fun filterAccounts(param: String): List<Account>;
    fun getAccountPassword(UID: String): String;
    fun updateAccount(UID: String, password: String, masterPassword: String);
    fun postAccount(login: String, password: String, masterPassword: String);
    fun deleteAccount(UID: String, masterPassword: String);
}