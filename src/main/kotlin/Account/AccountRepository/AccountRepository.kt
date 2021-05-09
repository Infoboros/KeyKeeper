package Account.AccountRepository

import Account.Account
import Specification.ISpecification
import java.lang.Exception

interface AccountRepository {
    fun filter(specification: ISpecification<Account>): List<Account>;
    fun getPassword(UID: String): String;

    class PasswordNotFound(_msg: String) : Exception(_msg);
}

class AccountMapRepository(_accounts: List<Account>) : AccountRepository {

    private val accounts: MutableMap<String, Account> = mutableMapOf();

    init {
        _accounts.forEach {
            accounts.plus(Pair(it.getUID(), it));
        }
    }

    override fun filter(specification: ISpecification<Account>): List<Account> {
        val result: MutableList<Account> = mutableListOf();
        accounts.map { (_, account) ->
            {
                if (specification.isSatisfiedBy(account))
                    result += account;
            }
        };
        return result;
    }

    override fun getPassword(UID: String): String {
        val account = accounts[UID];
        if (account != null)
            return account.getPassword();
        throw AccountRepository.PasswordNotFound("Account with UID $UID not found!");
    }


}