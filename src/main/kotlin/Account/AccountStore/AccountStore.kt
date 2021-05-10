package Account.AccountStore

import Account.Account
import Account.DataClass.AccountData
import Account.DataClass.AccountListData
import Account.DefaultAccount
import com.google.gson.Gson
import java.io.File

interface AccountStore {
    fun updateAccount(UID: String, password: String);
    fun postAccount(UID: String, login: String, password: String);
    fun deleteAccount(UID: String);
}

interface AccountLoader {
    fun getAllAccounts(): List<Account>;
}

class LocalAccountStore(_pathToData: String) : AccountStore, AccountLoader {

    private val pathToData = _pathToData
    private val dataFile = File(pathToData)
    private val tmpView: MutableMap<String, Account> = mutableMapOf()

    init {
        if (!dataFile.exists())
            dataFile.writeText(Gson().toJson(AccountListData(mutableListOf())))
        loadTmpView()
    }

    private fun loadTmpView() {
        val data = dataFile.readText()
        val accountListData = Gson().fromJson<AccountListData>(data, AccountListData::class.java)
        accountListData.list.forEach {
            tmpView += Pair(it.UID, DefaultAccount(it.UID, it.login, it.password))
        }
    }

    private fun saveTmpView() {
        val accountListData = AccountListData(mutableListOf())
        tmpView.forEach { UID, account ->
            accountListData.list += AccountData(UID, account.getLogin(), account.getPassword())
        }
        val data = Gson().toJson(accountListData)
        dataFile.writeText(data)
    }

    override fun getAllAccounts(): MutableList<Account> {
        val resultList = mutableListOf<Account>()
        tmpView.forEach {
            resultList += it.value
        }
        return resultList
    }

    override fun updateAccount(UID: String, password: String) {
        val oldAccount = tmpView[UID] ?: throw AccountNotFound(UID)
        tmpView[UID] = DefaultAccount(UID, oldAccount.getLogin(), password)
        saveTmpView()
    }

    override fun postAccount(UID: String, login: String, password: String) {
        tmpView += Pair(UID, DefaultAccount(UID, login, password))
        saveTmpView()
    }

    override fun deleteAccount(UID: String) {
        if (!tmpView.containsKey(UID))
            throw AccountNotFound(UID)
        tmpView.remove(UID)
        saveTmpView()
    }

    class AccountNotFound(UID: String) : Exception("Account with UID $UID not found!")
}