package TestAccount

import Account.Account
import Account.AccountRepository.AccountRepository
import Account.AccountSpecification.*
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.factory
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class TestLocalAccountRepository {

    private val kodein = getTestKodein()

    private val accountList: List<Account> by kodein.lazy.instance()
    private val accountRepository: AccountRepository by kodein.lazy.instance()
    private val notFoundUID: String by kodein.lazy.instance(tag = "NotFoundUID")

    @Test
    fun testFilterAll(){
        val specification = AccountAllSpecification()
        val accounts = accountRepository.filter(specification)
        assertEquals(accounts.count(), accountList.count())
    }

    @Test
    fun testUIDFilter(){
        accountList.forEach {
            val specification = AccountUIDSpecification(it.getUID())
            val filtered = accountRepository.filter(specification)
            assertEquals(filtered.count(), 1)
            assertEquals(it.getUID(), filtered.first().getUID())
        }
    }

    @Test
    fun testGetPassword(){
        accountList.forEach {
            assertEquals(it.getPassword(), accountRepository.getPassword(it.getUID()))
        }
    }

    @Test
    fun testPasswordNotFoundException(){
        assertFails {
            accountRepository.getPassword(notFoundUID)
        }
    }

}