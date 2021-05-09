package Account.AccountSpecification

import Account.Account
import Specification.CompositeSpecification

class AccountUIDSpecification(_UID: String): CompositeSpecification<Account>(){

    private val UID = _UID

    override fun isSatisfiedBy(candidate: Account) =
            candidate.getUID() == UID

}

class AccountLoginSpecification(_login: String): CompositeSpecification<Account>(){

    private val login = _login

    override fun isSatisfiedBy(candidate: Account) =
            candidate.getLogin() == login

}

class AccountAllSpecification(): CompositeSpecification<Account>(){
    override fun isSatisfiedBy(candidate: Account) = true
}