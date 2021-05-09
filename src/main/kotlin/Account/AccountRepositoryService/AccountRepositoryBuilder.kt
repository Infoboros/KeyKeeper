package Account.AccountRepositoryService

import Account.Account
import Account.AccountEncoder.AccountEncoder
import Account.AccountRepository.AccountMapRepository
import Account.AccountRepository.AccountRepository
import Account.AccountStore.AccountLoader

interface AccountRepositoryBuilder {
    fun loadMasterPassList();
    fun decodeMasterPassList();
    fun encodeSessionPassword();
    fun restart();
    fun getResult(): AccountRepository;
}

class AccountMapRepositoryBuilder(
    _accountLoader: AccountLoader,
    _sessionPassword: String,
    _masterPassword: String,
    _encoder: AccountEncoder
) : AccountRepositoryBuilder {

    private val accountLoader = _accountLoader;
    private val sessionPassword = _sessionPassword;
    private val masterPassword = _masterPassword;
    private val encoder = _encoder;

    private var accounts = listOf<Account>()

    override fun loadMasterPassList() {
        accounts = accountLoader.getAllAccounts();
    }

    override fun decodeMasterPassList() {
        val decodedAccounts = mutableListOf<Account>()
        val masterEncoder = encoder.getWithNewKey(masterPassword)

        accounts.forEach {
            decodedAccounts.add(masterEncoder.decodeAccount(it));
        }

        accounts = decodedAccounts;
    }

    override fun encodeSessionPassword() {
        val encodedAccounts = mutableListOf<Account>()
        val sessionEncoder = encoder.getWithNewKey(sessionPassword);

        accounts.forEach {
            encodedAccounts.add(sessionEncoder.encodeOnlyPassword(it));
        }

        accounts = encodedAccounts;
    }

    override fun restart() {
        accounts = listOf();
    }

    override fun getResult() =
        AccountMapRepository(accounts);

}