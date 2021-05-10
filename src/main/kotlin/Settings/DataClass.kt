package Settings

data class SettingsData(
    val passwordPath: String,
    val encoderName: String,
    val sessionTime: Int,
    val hashMasterPass: Int
)