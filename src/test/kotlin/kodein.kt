import Account.Account
import Account.AccountEncoder.*
import Account.AccountRepository.AccountMapRepository
import Account.AccountRepository.AccountRepository
import Account.AccountStore.LocalAccountStore
import Account.DefaultAccount
import Settings.LocalSettingsService
import Settings.Settings
import Settings.SettingsService
import Settings.StringSettings
import com.github.salomonbrys.kodein.*

fun getTestKodein() = Kodein {
    bind<List<Account>>() with provider { listOf(
        DefaultAccount("123", "google", "321"),
        DefaultAccount("321", "yandex", "123")
    ) }

    bind<AccountRepository>() with provider {AccountMapRepository(instance())}

    constant(tag = "NotFoundUID") with "355"

    bind<LocalAccountStore>() with provider { LocalAccountStore(System.currentTimeMillis().toString()) }

    bind<AbstractAccountEncoder>() with provider { AesEncoder() }

    bind<SettingsService>() with provider { LocalSettingsService() }
    bind<Settings>() with provider { StringSettings(
        "passwordDir",
        instance(),
        "120",
        "masterPassword"
    ) }
}