package com.zibanchegueman

import net.corda.core.identity.CordaX500Name
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.utilities.getOrThrow
import net.corda.node.services.Permissions.Companion.startFlow
import net.corda.testing.core.ALICE_NAME
import net.corda.testing.driver.DriverParameters
import net.corda.testing.driver.driver
import net.corda.testing.node.User
import net.corda.testing.node.internal.FINANCE_CORDAPPS
import java.nio.file.Paths

/**
 * Allows you to run your nodes through an IDE (as opposed to using deployNodes). Do not use in a production
 * environment.
 */
enum class PrintOrVisualise {
    Print,
    Visualise
}

@Suppress("DEPRECATION")
fun main(args: Array<String>) {
    require(args.isNotEmpty()) { "Usage: <binary> [Print|Visualise]" }
    val printOrVisualise = PrintOrVisualise.valueOf(args[0])

    val baseDirectory = Paths.get("build/rpc-api-tutorial")
    val user = User("user", "password", permissions = setOf(startFlow<CashIssueFlow>(),
            startFlow<CashPaymentFlow>(),
            startFlow<CashExitFlow>(),
            invokeRpc(CordaRPCOps::nodeInfo)
    ))
    driver(DriverParameters(driverDirectory = baseDirectory, cordappsForAllNodes = FINANCE_CORDAPPS, waitForAllNodesToFinish = true)) {
        val node = startNode(providedName = ALICE_NAME, rpcUsers = listOf(user)).get()