package Settings

import Account.AccountEncoder.*
import com.google.gson.Gson
import java.io.File

interface SettingsService {
    fun loadSettings(): Settings;
    fun saveSettings(settings: Settings);

    class SettingsNotFound(_msg: String = "Settings not found!") : Exception(_msg)
}

class LocalSettingsService : SettingsService {

    private val pathToSettings = "local_settings.json"

    override fun loadSettings(): Settings {
        val file = File(pathToSettings)
        if (!file.canRead())
            throw SettingsService.SettingsNotFound()

        val jsonData = file.readText()
        val settingsData = Gson().fromJson<SettingsData>(jsonData, SettingsData::class.java)

        return deserializeSettings(settingsData)
    }

    override fun saveSettings(settings: Settings) {
        val file = File(pathToSettings)
        val settingsData = serializeSettings(settings)
        val jsonData = Gson().toJson(settingsData)
        file.writeText(jsonData)
    }

    private fun deserializeSettings(settingsData: SettingsData) =
        LocalSettings(
            settingsData.passwordPath,
            AbstractAccountEncoder.deserialize(settingsData.encoderName),
            settingsData.sessionTime,
            settingsData.hashMasterPass
        )

    private fun serializeSettings(settings: Settings) =
        SettingsData(
            settings.getPasswordPath(),
            settings.getEncoder().serialize(),
            settings.getSessionTyme(),
            settings.getHashMasterPass()
        )
}