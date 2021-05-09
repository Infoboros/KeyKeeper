package Settings

interface IConfigurable {
    fun getSettings(): Settings;
    fun configure(newSettings: Settings, newMasterPassword: String, oldMasterPassword: String);
}