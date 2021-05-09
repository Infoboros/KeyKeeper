package AccountViewer

import Account.Account
import Account.AccountRepository.AccountRepository
import Account.AccountRepositoryService.AccountMapRepositoryBuilder
import Account.AccountRepositoryService.AccountRepositoryService
import Account.AccountSpecification.AccountAllSpecification
import Account.AccountSpecification.AccountLoginSpecification
import Account.AccountSpecification.AccountUIDSpecification
import Account.AccountStore.LocalAccountStore
import Account.ReCodeAccountService.ConfigureReCodeAccountService
import Session.ILoginAble
import Session.Session
import Session.TimeOutSession
import Settings.IConfigurable
import Settings.LocalSettingsService
import Settings.Settings

class AccountViewer(_settings: Settings,
                    _masterPassword: String) : IAccountCRUD, ILoginAble, IConfigurable {

    private var settings = _settings
    private var session: Session
    private var accountStore: LocalAccountStore
    private var accountRepository: AccountRepository

    init {
        settings.validate(_masterPassword, raiseException = true)
        session = TimeOutSession(settings.getSessionTyme())
        accountStore = LocalAccountStore(settings.getPasswordPath())
        val accountRepositoryBuilder = AccountMapRepositoryBuilder(accountStore,
                session.getSessionPassword(),
                _masterPassword,
                settings.getEncoder())
        accountRepository = AccountRepositoryService(accountRepositoryBuilder).construct()
    }

    private fun masterPasswordValid(password: String, raiseException: Boolean = false): Boolean {
        if (settings.getHashMasterPass() == password.hashCode())
            return true
        if (raiseException)
            throw PasswordIncorrect(password)
        return false
    }

    private fun getEncoderWithPassword(password: String) =
            settings.getEncoder()

    override fun filterAccounts(param: String): List<Account> {
        var specification = AccountUIDSpecification(param).or(AccountLoginSpecification(param))
        if (param == "")
            specification = specification.or(AccountAllSpecification())
        return accountRepository.filter(specification)
    }

    override fun getAccountPassword(UID: String): String {
        val encoder = getEncoderWithPassword(session.getSessionPassword())
        val encodePassword = accountRepository.getPassword(UID)

        return encoder.decodeString(encodePassword)
    }

    override fun updateAccount(UID: String, password: String, masterPassword: String) {
        if (masterPasswordValid(masterPassword, raiseException = true)) {
            val encoder = getEncoderWithPassword(masterPassword)
            accountStore.updateAccount(encoder.encodeString(UID),
                    encoder.encodeString(password))
        }
    }

    override fun postAccount(login: String, password: String, masterPassword: String) {
        if (masterPasswordValid(masterPassword, raiseException = true)) {
            val encoder = getEncoderWithPassword(masterPassword)
            accountStore.postAccount(encoder.encodeString(login),
                    encoder.encodeString(password))
        }
    }

    override fun deleteAccount(UID: String, masterPassword: String) {
        if (masterPasswordValid(masterPassword, raiseException = true)) {
            val encoder = getEncoderWithPassword(masterPassword)
            accountStore.deleteAccount(encoder.encodeString(UID))
        }
    }

    override fun login(masterPassword: String) {
        if (masterPasswordValid(masterPassword, raiseException = true)){
            session = TimeOutSession(settings.getSessionTyme())
            val accountRepositoryBuilder = AccountMapRepositoryBuilder(accountStore,
                                                                       session.getSessionPassword(),
                                                                       masterPassword,
                                                                       settings.getEncoder())
            accountRepository = AccountRepositoryService(accountRepositoryBuilder).construct()
        }
    }

    override fun getSettings() =
            settings

    override fun configure(newSettings: Settings, newMasterPassword: String, oldMasterPassword: String) {
        if (masterPasswordValid(oldMasterPassword) && newSettings.validate(newMasterPassword, raiseException = true)) {
            settings = newSettings
            session = TimeOutSession(settings.getSessionTyme())

            val reCodeService = ConfigureReCodeAccountService(settings)
            reCodeService.reCodeAccounts(accountStore.getAllAccounts(), newMasterPassword, oldMasterPassword)
            accountStore = LocalAccountStore(settings.getPasswordPath())

            login(newMasterPassword)

            LocalSettingsService().saveSettings(settings)
        }
    }

    class PasswordIncorrect(_password: String) : Exception("Password $_password is incorrect!")
}