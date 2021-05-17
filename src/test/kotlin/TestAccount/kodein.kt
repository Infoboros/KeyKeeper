package TestAccount

import Account.Account
import Account.AccountRepository.AccountMapRepository
import Account.AccountRepository.AccountRepository
import Account.AccountStore.LocalAccountStore
import Account.DefaultAccount
import com.github.salomonbrys.kodein.*

fun getTestKodein() = Kodein {
    bind<List<Account>>() with provider { listOf(
        DefaultAccount("123", "google", "321"),
        DefaultAccount("321", "yandex", "123")
    ) }

    bind<AccountRepository>() with provider {AccountMapRepository(instance())}

    constant(tag = "NotFoundUID") with "355"

    bind<LocalAccountStore>() with provider { LocalAccountStore(System.currentTimeMillis().toString()) }
}