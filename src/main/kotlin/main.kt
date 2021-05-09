import Account.AccountEncoder.AesEncoder
import Settings.LocalSettings
import Settings.LocalSettingsService

fun makeSettings(){
    val settings = LocalSettings("", AesEncoder(), 120, "qwer1234".hashCode())
    val settingsService = LocalSettingsService()
    settingsService.saveSettings(settings)
}

fun main(args: Array<String>) {
    makeSettings()
    val settingsService = LocalSettingsService()
    val settings = settingsService.loadSettings()
    return
}