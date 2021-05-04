package Session

import java.lang.Exception

interface Session {
    fun getSessionPassword(): String;
}

class TimeOutSession(_timeOut: Int): Session{
    private val timeOut = _timeOut + System.currentTimeMillis();
    private val sessionPassword = (1..256)
            .map { i -> kotlin.random.Random.nextInt(0, 9) }
            .map({i -> i.toString()})
            .joinToString("");

    override fun getSessionPassword(): String{
        if (timeOut < System.currentTimeMillis())
            return sessionPassword;
        throw SessionExpireException();
    }

    class SessionExpireException: Exception("Время сессии истекло.");
}