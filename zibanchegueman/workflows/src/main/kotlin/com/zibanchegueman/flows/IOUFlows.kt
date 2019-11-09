package com.zibanchegueman.flows

import co.paralleluniverse.fibers.Suspendable
import com.zibanchegueman.contracts.IOUContract
import com.zibanchegueman.states.IOUState
import net.corda.core.contracts.Command
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker

@InitiatingFlow
@StartableByRPC
class IOUFlow (
        val affiliation: Party,
        val vaultServer: Party,
        val from: String,
        val to: String,
        val value: Int,
        val date: String
        //val type: String
        ) : FlowLogic<Unit>() {

    /** The progress tracker provides checkpoints indicating the progress of the flow to observers. */
    override val progressTracker = ProgressTracker()

    /** The flow logic is encapsulated within the call() method. */
    @Suspendable
    override fun call() {
        // Initiator flow logic goes here.
        // We retrieve the notary identity from the network map.
        val notary = serviceHub.networkMapCache.notaryIdentities[0]

        // type set.
        var type = ""
        if(from == "sejong") type = "Withdraw"
        else if(to == "sejong") type = "Deposit"

        // We create the transaction components.
        val outputState = IOUState(affiliation, vaultServer, from, to, value, date, type)
        val command = Command(IOUContract.Create(), listOf(affiliation.owningKey, vaultServer.owningKey))

        // We create a transaction builder and add the components.
        val txBuilder = TransactionBuilder(notary = notary)
                .addOutputState(outputState, IOUContract.ID)
                .addCommand(command)

        // Verifying the transaction.
        txBuilder.verify(serviceHub)

        // We sign the transaction.
        val signedTx = serviceHub.signInitialTransaction(txBuilder)

        // Creating a session with the other party.
        val otherPartySession = initiateFlow(vaultServer)

        // Obtaining the counterparty's signature.
        val fullySignedTx = subFlow(CollectSignaturesFlow(signedTx,listOf(otherPartySession),CollectSignaturesFlow.tracker()))

        // We finalise the transaction and then send it to the counterparty.
        subFlow(FinalityFlow(fullySignedTx, otherPartySession))
    }
}

// Responder
@InitiatedBy(IOUFlow::class)
class IOUFlowResponder(val otherPartySession: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val signTransactionFlow = object : SignTransactionFlow(otherPartySession) {
            override fun checkTransaction(stx: SignedTransaction) = requireThat {
                val output = stx.tx.outputs.single().data
                "This must be an IOU transaction." using (output is IOUState)
            }
        }

        val expectedTxId = subFlow(signTransactionFlow).id

        subFlow(ReceiveFinalityFlow(otherPartySession, expectedTxId))
    }
}
