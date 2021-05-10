package Account.DataClass

data class AccountListData(val list: MutableList<AccountData>)
data class AccountData(val UID: String, val login: String, val password: String);