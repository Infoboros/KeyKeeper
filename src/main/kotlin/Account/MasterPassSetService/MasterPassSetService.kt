package Account.MasterPassSetService

import Account.Account
import Settings.Settings

interface MasterPassSetService {
    fun reEncodeAccounts(accounts: List<Account>, newMasterPass: String);
}

class ConfigureMasterPassSetService(_settings: Settings) : MasterPassSetService {
    val settings = _settings;
    override fun reEncodeAccounts(accounts: List<Account>, newMasterPass: String) {
        TODO("Not yet implemented")
    }
}