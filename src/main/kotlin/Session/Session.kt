package Session

import java.lang.Exception

interface Session {
    fun getSessionPassword(): String;

    class SessionExpireException : Exception("Время сессии истекло.");
}

class TimeOutSession(_timeOut: Int) : Session {
    private val timeOut = _timeOut * 1000 + System.currentTimeMillis();
    private val sessionPassword = (1..256)
        .map { i -> kotlin.random.Random.nextInt(0, 9) }
        .map({ i -> i.toString() })
        .joinToString("");

    override fun getSessionPassword(): String {
        if (System.currentTimeMillis() < timeOut)
            return sessionPassword;
        throw Session.SessionExpireException();
    }
}
