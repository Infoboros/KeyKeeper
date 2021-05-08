package Account

interface Account {
    fun getUID(): String;
    fun getLogin(): String;
    fun getPassword(): String;
}

class DefaultAccount(_UID: String,
                     _login: String,
                     _password: String) : Account {

    private val UID = _UID;
    private val login = _login;
    private val password = _password;

    override fun getUID() = UID;

    override fun getLogin() = login

    override fun getPassword() = password

}