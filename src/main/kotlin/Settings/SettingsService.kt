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

        try {
            val jsonData = file.readText()
            val settingsData = Gson().fromJson<SettingsModel>(jsonData, SettingsModel::class.java)

            return deserializeSettings(settingsData)
        } catch (e: Exception) {
            throw SettingsService.SettingsNotFound(e.toString())
        }
    }

    override fun saveSettings(settings: Settings) {
        val file = File(pathToSettings)
        val settingsData = serializeSettings(settings)
        val jsonData = Gson().toJson(settingsData)
        file.writeText(jsonData)
    }

    data class SettingsModel(
        val passwordPath: String,
        val encoderName: String,
        val sessionTime: Int,
        val hashMasterPass: Int
    )

    private fun deserializeSettings(settingsData: SettingsModel) =
        LocalSettings(
            settingsData.passwordPath,
            AbstractAccountEncoder.deserialize(settingsData.passwordPath),
            settingsData.sessionTime,
            settingsData.hashMasterPass
        )

    private fun serializeSettings(settings: Settings) =
        SettingsModel(
            settings.getPasswordPath(),
            settings.getEncoder().serialize(),
            settings.getSessionTyme(),
            settings.getHashMasterPass()
        )
}