package AccountViewer

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

class AccountViewer(
    _settings: Settings,
    _masterPassword: String
) : IAccountCRUD, ILoginAble, IConfigurable {

    private var settings = _settings
    private var session: Session
    private var accountStore: LocalAccountStore
    private var accountRepository: AccountRepository

    init {
        session = TimeOutSession(settings.getSessionTyme())
        accountStore = LocalAccountStore(settings.getPasswordPath())
        accountRepository = reBuildAccountRepository(_masterPassword)
    }

    private fun masterPasswordValid(password: String, raiseException: Boolean = false): Boolean {
        if (settings.getHashMasterPass() == password.hashCode())
            return true
        if (raiseException)
            throw PasswordIncorrect(password)
        return false
    }

    private fun getEncoderWithPassword(password: String) =
        settings.getEncoder().getWithNewKey(password)

    private fun reBuildAccountRepository(masterPassword: String): AccountRepository {
        val accountRepositoryBuilder = AccountMapRepositoryBuilder(
            accountStore,
            session.getSessionPassword(),
            masterPassword,
            settings.getEncoder()
        )
        return AccountRepositoryService(accountRepositoryBuilder).construct()
    }

    override fun filterAccounts(param: String): List<String> {
        var specification = AccountUIDSpecification(param).or(AccountLoginSpecification(param))
        if (param == "")
            specification = specification.or(AccountAllSpecification())
        return accountRepository.filter(specification).map {
            "${it.getUID()}||${it.getLogin()}||${it.getPassword().padEnd(50).slice(0..49)}"
        }
    }

    override fun getAccountPassword(UID: String): String {
        val encoder = getEncoderWithPassword(session.getSessionPassword())
        try {
            val encodePassword = accountRepository.getPassword(UID)
            return encoder.decodeString(encodePassword)
        } catch (e: AccountRepository.PasswordNotFound) {
            throw IAccountCRUD.AccountNotFound(UID)
        }
    }

    override fun updateAccount(UID: String, password: String, masterPassword: String) {
        if (masterPasswordValid(masterPassword, raiseException = true)) {
            val encoder = getEncoderWithPassword(masterPassword)
            accountStore.updateAccount(
                encoder.encodeString(UID),
                encoder.encodeString(password)
            )
            accountRepository = reBuildAccountRepository(masterPassword)
        }
    }

    override fun postAccount(login: String, password: String, masterPassword: String) {
        if (masterPasswordValid(masterPassword, raiseException = true)) {
            val encoder = getEncoderWithPassword(masterPassword)
            val UID = (login+password+System.currentTimeMillis().toString())
                .hashCode()
                .toString()

            accountStore.postAccount(
                encoder.encodeString(UID),
                encoder.encodeString(login),
                encoder.encodeString(password)
            )
            accountRepository = reBuildAccountRepository(masterPassword)
        }
    }

    override fun deleteAccount(UID: String, masterPassword: String) {
        if (masterPasswordValid(masterPassword, raiseException = true)) {
            val encoder = getEncoderWithPassword(masterPassword)
            accountStore.deleteAccount(encoder.encodeString(UID))
            accountRepository = reBuildAccountRepository(masterPassword)
        }
    }

    override fun login(masterPassword: String) {
        if (masterPasswordValid(masterPassword, raiseException = true)) {
            session = TimeOutSession(settings.getSessionTyme())
            accountRepository = reBuildAccountRepository(masterPassword)
        }
    }

    override fun getSettings() =
        settings

    override fun configure(newSettings: Settings, newMasterPassword: String, oldMasterPassword: String) {
        if (masterPasswordValid(oldMasterPassword) && newSettings.validate(newMasterPassword, raiseException = true)) {
            val oldEncoder = settings.getEncoder().getWithNewKey(oldMasterPassword)
            val newEncoder = newSettings.getEncoder().getWithNewKey(newMasterPassword)
            settings = newSettings
            session = TimeOutSession(settings.getSessionTyme())

            val reCodeService = ConfigureReCodeAccountService(settings)
            reCodeService.reCodeAccounts(accountStore.getAllAccounts(), newEncoder, oldEncoder)
            accountStore = LocalAccountStore(settings.getPasswordPath())

            login(newMasterPassword)

            LocalSettingsService().saveSettings(settings)
        }
    }

    class PasswordIncorrect(_password: String) : Exception("Password $_password is incorrect!")
}