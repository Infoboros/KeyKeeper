import Account.Account
import Account.AccountStore.LocalAccountStore
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestLocalAccountStore {

    val kodein = getTestKodein()
    val localAccountStore: LocalAccountStore by kodein.lazy.instance()
    val testAccount: Account

    val updatedPassword = "password"

    init {
        val testAccountList: List<Account> by kodein.lazy.instance()
        testAccount = testAccountList.first()
    }

    private fun equalAccount(account1: Account, account2: Account) =
                (account1.getUID() == account2.getUID()) &&
                (account1.getLogin() == account2.getLogin()) &&
                (account1.getPassword() == account2.getPassword())

    @Before
    fun postTestAccount() {
        localAccountStore.postAccount(
            testAccount.getUID(),
            testAccount.getLogin(),
            testAccount.getPassword()
        )
    }

    @After
    fun deleteTestAccount(){
        localAccountStore.deleteAccount(testAccount.getUID())
    }

    @Test
    fun testPost() {
        val storedAccounts = localAccountStore.getAllAccounts()
        assertEquals(storedAccounts.count(), 1)

        val storedAccount = storedAccounts.first()
        assertTrue {
            equalAccount(testAccount, storedAccount)
        }
    }

    @Test
    fun testUpdate() {
        localAccountStore.updateAccount(testAccount.getUID(), updatedPassword)
        val storedAccount = localAccountStore.getAllAccounts().first()
        assertEquals(storedAccount.getPassword(), updatedPassword)
    }

    @Test
    fun testDelete() {
        deleteTestAccount()

        assertEquals(localAccountStore.getAllAccounts().count(), 0)

        postTestAccount()
    }
}