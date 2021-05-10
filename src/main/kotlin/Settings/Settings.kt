package Settings

import Account.AccountEncoder.AbstractAccountEncoder
import java.lang.Exception

interface Settings {
    fun getPasswordPath(): String;
    fun getEncoder(): AbstractAccountEncoder;
    fun getSessionTyme(): Int;
    fun getHashMasterPass(): Int;
    fun validate(masterPassword: String, raiseException: Boolean = false): Boolean;

    class SettingsValidateException(_msg: String): Exception(_msg);
}

class LocalSettings(
        _passwordDir: String,
        _encoder: AbstractAccountEncoder,
        _sessionTime: Int,
        _hashMasterPass: Int
) : Settings {

    private var passwordPath = _passwordDir;
    private var encoder = _encoder;
    private var sessionTime = _sessionTime;
    private var hashMasterPass = _hashMasterPass;

    override fun validate(masterPassword: String, raiseException: Boolean): Boolean{
        if (hashMasterPass != masterPassword.hashCode())
            if (raiseException)
                throw Settings.SettingsValidateException("Input master password incorrect!")
            else
                return false
        return true
    }


    override fun getPasswordPath() = passwordPath;

    override fun getEncoder() = encoder;

    override fun getSessionTyme() = sessionTime;

    override fun getHashMasterPass() = hashMasterPass;

}