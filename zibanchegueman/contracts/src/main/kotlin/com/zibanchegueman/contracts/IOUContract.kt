package com.zibanchegueman.contracts

import com.zibanchegueman.states.IOUState
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.requireSingleCommand
import net.corda.core.contracts.requireThat
import net.corda.core.transactions.LedgerTransaction

/*
제약 조건 : 1. IOU와 관련된 트랜잭션은 입력을 전혀 하지않고 출력 하나를 생성해야 합니다.
          2. 트랜잭션에는 Create 트랜잭션의 의도를 나타내는 명령도 포함되어야 합니다.
          3. 발행된 속성에 대해서 그 값은 음수가 아니어야 합니다.
          4. 소속기관과 금고서버는 같을 수 없습니다.
          5. 소속기관은 반드시 서명을 해야 합니다.
          6. 금고서버는 반드시 서명을 해야 합니다.
 */

class IOUContract : Contract {
    companion object {
        const val ID = "com.zibanchegueman.contracts.IOUContract"
    }

    // Our Create command.
    // 모든 명령은 Create 를 통해서 만들어야 합니다.
    class Create : CommandData

    override fun verify(tx: LedgerTransaction) {

        // requireSingleCommand 를 통해 create 명령이 있는지 확인합니다.
        // tx.inputs : 입력을 나열합니다.
        // tx.outputs : 출력을 나열합니다.
        // tx.commands : 명령 및 관련 서명자를 나열합니다.

        val command = tx.commands.requireSingleCommand<Create>()

        requireThat {
            // Constraints on the shape of the transaction.
            "No inputs should be consumed when issuing an IOU." using (tx.inputs.isEmpty())
            "There should be one output state of type IOUState." using (tx.outputs.size == 1)

            // IOU-specific constraints.
            val output = tx.outputsOfType<IOUState>().single()
            "The IOU's value must be non-negative." using (output.value > 0)
            "The affiliation and the vaultServer cannot be the same entity." using (output.affiliation != output.vaultServer)

            // Constraints on the signers.
            val expectedSigners = listOf(output.vaultServer.owningKey, output.affiliation.owningKey)
            "There must be two signers." using (command.signers.toSet().size == 2)
            "The vaultServer and affiliation must be signers." using (command.signers.containsAll(expectedSigners))
        }
    }
}