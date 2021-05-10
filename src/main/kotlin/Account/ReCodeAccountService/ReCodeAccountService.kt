package Account.ReCodeAccountService

import Account.Account
import Account.AccountEncoder.AccountEncoder
import Account.DataClass.AccountData
import Account.DataClass.AccountListData
import Settings.Settings
import com.google.gson.Gson
import java.io.File

interface ReCodeAccountService {
    fun reCodeAccounts(accounts: List<Account>, newEncoder: AccountEncoder, oldEncoder: AccountEncoder);
}

class ConfigureReCodeAccountService(_settings: Settings) : ReCodeAccountService {
    private val settings = _settings;

    override fun reCodeAccounts(accounts: List<Account>, newEncoder: AccountEncoder, oldEncoder: AccountEncoder) {
        val pathToFile = settings.getPasswordPath()
        val accountListData = AccountListData(mutableListOf())

        accounts.forEach {
            val encodedAccount = oldEncoder.decodeAccount(it).let { newEncoder.encodeAccount(it) }
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