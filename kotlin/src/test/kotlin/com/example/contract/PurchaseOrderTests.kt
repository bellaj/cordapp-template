package com.example.contract

import com.example.model.Address
import com.example.model.Item
import com.example.model.PurchaseOrder
import net.corda.core.utilities.TEST_TX_TIME
import net.corda.testing.*
import org.junit.Test
import java.time.Duration
import java.time.temporal.TemporalAmount
import java.util.*

class PurchaseOrderTests {
    @Test
    fun `place`() {
        val address = Address("London", "UK")
        val items = listOf(Item("Hammer", 1))
        val deliveryTime = TEST_TX_TIME.plus(Duration.ofDays(7))
        val purchaseOrder = PurchaseOrder(1, Date(deliveryTime.toEpochMilli()), address, items)
        ledger {
            transaction {
                output { PurchaseOrderState(purchaseOrder, MINI_CORP, MEGA_CORP, PurchaseOrderContract()) }
                timestamp(TEST_TX_TIME)
                `fails with`("Required com.example.contract.PurchaseOrderContract.Commands.Place command")
                command(MEGA_CORP_PUBKEY, MINI_CORP_PUBKEY) { PurchaseOrderContract.Commands.Place() }
                verifies()
            }
        }
    }
}