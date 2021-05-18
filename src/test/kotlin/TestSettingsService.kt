import Settings.Settings
import Settings.SettingsService
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import kotlin.test.Test
import kotlin.test.assertTrue

class TestSettingsService {

    private val kodein = getTestKodein()

    private val settingsService: SettingsService by kodein.lazy.instance()
    private val testSettings: Settings by kodein.lazy.instance()

    private fun equalSettings(settings: Settings) =
                (settings.getPasswordPath() == testSettings.getPasswordPath()) &&
                (settings.getEncoder().serialize() == settings.getEncoder().serialize()) &&
                (settings.getSessionTyme() == settings.getSessionTyme()) &&
                (settings.getHashMasterPass() == settings.getHashMasterPass())

    @Test
    fun testSaveSettings() {
        settingsService.saveSettings(testSettings)
        val storedSettings = settingsService.loadSettings()
        assertTrue {
            equalSettings(storedSettings)
        }
    }

}