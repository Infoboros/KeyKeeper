package Account

interface Account {
    fun getID(): String;
    fun getLogin(): String;
    fun getPassword(): String;
}

class DefaultAccount(_id: String,
                     _login: String,
                     _password: String) : Account {

    private val id = _id;
    private val login = _login;
    private val password = _password;

    override fun getID() = id;

    override fun getLogin() = login

    override fun getPassword() = password

}