package Account.AccountRepositoryService

import Account.AccountRepository.AccountRepository

class AccountRepositoryService(_builder: AccountRepositoryBuilder) {
    private val builder = _builder;

    fun construct(): AccountRepository {
        builder.restart();
        builder.loadMasterPassList();
        builder.decodeMasterPassList()
        builder.encodeSessionPassword();
        return builder.getResult();
    }
}