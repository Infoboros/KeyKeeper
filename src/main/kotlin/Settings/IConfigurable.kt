package Settings

interface IConfigurable {
    fun getSettings(): Settings;
    fun configure(settings: Settings);
}