package UI

import Account.AccountEncoder.AbstractAccountEncoder
import AccountViewer.AccountViewer
import AccountViewer.IAccountCRUD
import Settings.*

class ConsoleUI {
    private val accountViewer: AccountViewer

    init {
        var settings: Settings? = null
        try {
            settings = LocalSettingsService().loadSettings()
        } catch (e: SettingsService.SettingsNotFound) {
            settings = inputSettings()
            LocalSettingsService().saveSettings(settings)
        }
        println("Input master password:")
        val masterPassword = customInputString()
        settings!!.validate(masterPassword, raiseException = true)
        accountViewer = AccountViewer(settings, masterPassword)
    }

    private fun customInputString(): String {

        var data: String? = null
        while (data == null) {
            print("$>")
            data = readLine()
        }
        return data
    }

    private fun inputSettings(): Settings {
        println("Input path to passwords: ")
        val pathToPasswords = customInputString()

        println("Input type of encoder: ")
        val encoderType = customInputString()

        println("Input time out of session: ")
        val timeOutSession = customInputString()

        println("Input master password: ")
        val hashMasterPass = customInputString()


        return StringSettings(
            pathToPasswords,
            AbstractAccountEncoder.deserialize(encoderType),
            timeOutSession,
            hashMasterPass
        )
    }

    private fun displayListOfAccounts() {
        println("Input search request(empty for all): ")
        val searchRequest = customInputString()

        val filteredAccounts = accountViewer.filterAccounts(searchRequest)
        if (filteredAccounts.isNotEmpty())
            filteredAccounts.forEach { println(it) }
        else
            println("~Accounts responsible request not found~")
    }

    private fun showPassword() {
        println("Input UID account: ")
        val UID = customInputString()
        try {
            println("Your Password: ${accountViewer.getAccountPassword(UID)}")
        } catch (e: IAccountCRUD.AccountNotFound) {
            println(e.message)
        }
    }

    private fun manipulateWithAccounts() {
        println("1-Add new account")
        println("2-Update exist account")
        println("3-Delete exist account")
        val inputFlag = customInputString().toInt()
        println("Input master password:")
        val masterPass = customInputString()

        when (inputFlag) {
            1 -> {
                println("Input login:")
                val login = customInputString()

                println("Input password:")
                val password = customInputString()

                accountViewer.postAccount(login, password, masterPass)
            }
            2 -> {
                println("Input UID:")
                val UID = customInputString()

                println("Input password:")
                val password = customInputString()

                accountViewer.updateAccount(UID, password, masterPass)
            }
            3 -> {
                println("Input UID:")
                val UID = customInputString()

                accountViewer.deleteAccount(UID, masterPass)
            }
            else -> manipulateWithAccounts()
        }
    }

    private fun activateSession() {
        println("Input master password:")
        val masterPass = customInputString()
        accountViewer.login(masterPass)
    }

    private fun reSetSettings() {
        val settings = inputSettings()

        println("Input new master password again:")
        val newMasterPass = customInputString()

        println("Input old master password:")
        val oldMasterPass = customInputString()

        accountViewer.configure(settings, newMasterPass, oldMasterPass)
    }

    fun start() {
        while (true) {
            try {
                println()
                println("1-Display list of accounts")
                println("2-Show Password")
                println("3-Manipulate with accounts")
                println("4-Activate Session")
                println("5-ReSet Settings")
                println("6-exit")
                when (customInputString().toInt()) {
                    1 -> displayListOfAccounts()
                    2 -> showPassword()
                    3 -> manipulateWithAccounts()
                    4 -> activateSession()
                    5 -> reSetSettings()
                    6 -> break
                    else -> continue
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}