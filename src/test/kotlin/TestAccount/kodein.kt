package TestAccount

import Account.Account
import Account.AccountRepository.AccountMapRepository
import Account.AccountRepository.AccountRepository
import Account.DefaultAccount
import com.github.salomonbrys.kodein.*

fun getTestKodein() = Kodein {
    bind<List<Account>>() with provider { listOf() }

    bind<AccountRepository>() with provider {AccountMapRepository(instance())}

    constant(tag = "NotFoundUID") with "355"
}