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

    companion object {
        object GENERATING_TRANSACTION : ProgressTracker.Step("Generating transaction based on new IOU.")
        object VERIFYING_TRANSACTION : ProgressTracker.Step("Verifying contract constraints.")
        object SIGNING_TRANSACTION : ProgressTracker.Step("Signing transaction with our private key.")
        object GATHERING_SIGS : ProgressTracker.Step("Gathering the counterparty's signature.") {
            override fun childProgressTracker() = CollectSignaturesFlow.tracker()
        }

        object FINALISING_TRANSACTION : ProgressTracker.Step("Obtaining notary signature and recording transaction.") {
            override fun childProgressTracker() = FinalityFlow.tracker()
        }

        fun tracker() = ProgressTracker(
                GENERATING_TRANSACTION,
                VERIFYING_TRANSACTION,
                SIGNING_TRANSACTION,
                GATHERING_SIGS,
                FINALISING_TRANSACTION
        )
    }

    /** The progress tracker provides checkpoints indicating the progress of the flow to observers. */
    override val progressTracker = tracker()

    /** The flow logic is encapsulated within the call() method. */
    @Suspendable
    override fun call() {
        // Initiator flow logic goes here.
        // We retrieve the notary identity from the network map.
        val notary = serviceHub.networkMapCache.notaryIdentities[0]

        // Stage 1.
        progressTracker.currentStep = GENERATING_TRANSACTION
        // type set.
        var type = ""
        if(from == "sejong") type = "Withdraw"
        else if(to == "sejong") type = "Deposit"
        // We create the transaction components.
        val outputState = IOUState(affiliation, vaultServer, from, to, value, date, type)
        val command = Command(IOUContract.Create(), listOf(affiliation.owningKey, vaultServer.owningKey))
        // We create a transaction builder and add the components.
        val txBuilder = TransactionBuilder(notary)
                .addOutputState(outputState, IOUContract.ID)
                .addCommand(command)

        // Stage 2.
        progressTracker.currentStep = VERIFYING_TRANSACTION
        // Verifying the transaction.
        txBuilder.verify(serviceHub)

        // Stage 3.
        progressTracker.currentStep = SIGNING_TRANSACTION
        // We sign the transaction.
        val signedTx = serviceHub.signInitialTransaction(txBuilder)

        // Stage 4.
        progressTracker.currentStep = GATHERING_SIGS
        // Creating a session with the other party.
        val otherPartySession = initiateFlow(vaultServer)
        // Obtaining the counterparty's signature.
        val fullySignedTx = subFlow(CollectSignaturesFlow(signedTx, setOf(otherPartySession), GATHERING_SIGS.childProgressTracker()))

        // Stage 5.
        progressTracker.currentStep = FINALISING_TRANSACTION
        // We finalise the transaction and then send it to the counterparty.
        subFlow(FinalityFlow(fullySignedTx, setOf(otherPartySession), FINALISING_TRANSACTION.childProgressTracker()))
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
