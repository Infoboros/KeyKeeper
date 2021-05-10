package Account.ReCodeAccountService

import Account.Account
import Account.DataClass.AccountData
import Account.DataClass.AccountListData
import Settings.Settings
import com.google.gson.Gson
import java.io.File

interface ReCodeAccountService {
    fun reCodeAccounts(accounts: List<Account>, newMasterPass: String, oldMasterPass: String);
}

class ConfigureReCodeAccountService(_settings: Settings) : ReCodeAccountService {
    private val settings = _settings;

    override fun reCodeAccounts(accounts: List<Account>, newMasterPass: String, oldMasterPass: String) {
        val pathToFile = settings.getPasswordPath()
        val oldMasterEncoder = settings.getEncoder().getWithNewKey(oldMasterPass)
        val newMasterEncoder = settings.getEncoder().getWithNewKey(newMasterPass)
        val accountListData = AccountListData(mutableListOf())

        accounts.forEach {
            val encodedAccount = oldMasterEncoder.decodeAccount(it).let { newMasterEncoder.encodeAccount(it) }
            accountListData.list += AccountData(
                encodedAccount.getUID(),
                encodedAccount.getLogin(),
                encodedAccount.getPassword()
            )
        }

        val data = Gson().toJson(accountListData)
        File(pathToFile).writeText(data)
    }
}