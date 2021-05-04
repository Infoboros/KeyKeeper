package Settings

import AccountEncoder.AccountEncoder
import java.lang.Exception

interface Settings {
    fun getPasswordDir(): String;
    fun getEncoder(): AccountEncoder;
    fun getSessionTyme(): Int;
    fun getHashMasterPass(): String;
    fun setPasswordDir(passwordDir: String);
    fun setEncoder(encoder: AccountEncoder);
    fun setSessionTyme(sessionTime: Int);
    fun setPassword(password: String);
    fun validate(raiseException: Boolean = false): Boolean;
}

class LocalSettings(
        _passwordDir: String,
        _encoder: AccountEncoder,
        _sessionTime: Int,
        _hashMasterPass: String
) : Settings {

    private var passwordDir = _passwordDir;
    private var encoder = _encoder;
    private var sessionTime = _sessionTime;
    private var hashMasterPass = _hashMasterPass;

    override fun validate(raiseException: Boolean): Boolean =
            true;

    override fun getPasswordDir() = passwordDir;

    override fun getEncoder() = encoder;

    override fun getSessionTyme() = sessionTime;

    override fun getHashMasterPass() = hashMasterPass;

    override fun setPasswordDir(passwordDir: String) {
        this.passwordDir = passwordDir;
    }

    override fun setSessionTyme(sessionTime: Int) {
        this.sessionTime = sessionTime;
    }

    override fun setPassword(password: String) {
        hashMasterPass = password.hashCode().toString();
    }

    override fun setEncoder(encoder: AccountEncoder) {
        this.encoder = encoder;
    }

    class SettingsValidateException(_msg: String): Exception(_msg);

}