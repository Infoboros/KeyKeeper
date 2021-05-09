package Account.ReCodeAccountService

import Account.Account
import Settings.Settings

interface ReCodeAccountService {
    fun reCodeAccounts(accounts: List<Account>, newMasterPass: String, oldMasterPass: String);
}

class ConfigureReCodeAccountService(_settings: Settings) : ReCodeAccountService {
    val settings = _settings;
    override fun reCodeAccounts(accounts: List<Account>, newMasterPass: String, oldMasterPass: String) {
        TODO("Not yet implemented")
    }
}