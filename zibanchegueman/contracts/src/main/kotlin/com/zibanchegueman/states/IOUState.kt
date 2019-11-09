package com.zibanchegueman.states

import com.zibanchegueman.contracts.IOUContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.Party

/*

State : affiliation : 감사 소속 기관 or 은행 기관
        vaultServer : 거래 내역이 저장된 블록체인이 저장될 서버 = 금고서버
        from, to & value : 거래 대상자와 거래금액
        date : 거래가 된 시간
        type : 입금(deposit) or 출금(withdraw)

 */
@BelongsToContract(IOUContract::class)
data class IOUState(
        val affiliation: Party,
        val vaultServer: Party,
        val from: String,
        val to: String,
        val value: Int,
        val date: String,
        var type: String
        ) : ContractState {
       override val participants get() = listOf(affiliation, vaultServer)
}
