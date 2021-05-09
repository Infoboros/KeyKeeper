package UI

import Account.AccountEncoder.AbstractAccountEncoder
import AccountViewer.AccountViewer
import Settings.LocalSettings
import Settings.LocalSettingsService
import Settings.Settings
import Settings.SettingsService

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
        print("Input master password: ")
        accountViewer = AccountViewer(settings!!, customInputString())
    }

    private fun customInputString(): String {
        var data: String? = null
        while (data == null)
            data = readLine()
        return data
    }

    private fun inputSettings(): Settings {
        print("Input path to passwords: ")
        val pathToPasswords = customInputString()

        print("Input type of encoder: ")
        val encoderType = customInputString()

        print("Input time out of session: ")
        val timeOutSession = customInputString().toInt()

        print("Input master password: ")
        val hashMasterPass = customInputString().hashCode()


        return LocalSettings(
            pathToPasswords,
            AbstractAccountEncoder.deserialize(encoderType),
            timeOutSession, hashMasterPass
        )
    }

}