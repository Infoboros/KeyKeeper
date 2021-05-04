package Settings

import AccountEncoder.AesEncoder

interface SettingsService {
    fun loadSettings(): Settings;
    fun saveSettings(settings: Settings);
}

class LocalSettingsService : SettingsService{
    override fun loadSettings() =
            LocalSettings("", AesEncoder(), 60, "asd")

    override fun saveSettings(settings: Settings) {
        TODO("Not yet implemented")
    }

}