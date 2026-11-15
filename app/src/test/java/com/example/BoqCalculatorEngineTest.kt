package com.example

import com.example.data.catalog.BoqCalculatorEngine
import com.example.data.catalog.MaterialsCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BoqCalculatorEngineTest {

    private val defaultPrices = MaterialsCatalog.items.associate { it.id to it.defaultPrice }

    @Test
    fun `default material prices match all key PDF summary figures`() {
        val computed = BoqCalculatorEngine.computeAll(defaultPrices)
        val byId = computed.associateBy { it.spec.id }

        // Earthwork & subbase
        assertEquals(7_500.0, byId.getValue("item_2").finalPrice, 0.0)
        assertEquals(8_750.0, byId.getValue("item_2").priceWithProfit!!, 0.0)
        assertEquals(5_750.0, byId.getValue("item_3").finalPrice, 0.0)
        assertEquals(6_500.0, byId.getValue("item_3").priceWithProfit!!, 0.0)
        assertEquals(8_500.0, byId.getValue("item_4").finalPrice, 0.0)
        assertEquals(9_750.0, byId.getValue("item_4").priceWithProfit!!, 0.0)

        // Layered soil replacement & subbase
        assertEquals(4_500.0, byId.getValue("item_5").finalPrice, 0.0)
        assertEquals(5_250.0, byId.getValue("item_5").priceWithProfit!!, 0.0)
        assertEquals(5_500.0, byId.getValue("item_6").finalPrice, 0.0)
        assertEquals(6_250.0, byId.getValue("item_6").priceWithProfit!!, 0.0)
        assertEquals(7_000.0, byId.getValue("item_7").finalPrice, 0.0)
        assertEquals(8_000.0, byId.getValue("item_7").priceWithProfit!!, 0.0)
        assertEquals(8_000.0, byId.getValue("item_8").finalPrice, 0.0)
        assertEquals(9_250.0, byId.getValue("item_8").priceWithProfit!!, 0.0)
        assertEquals(5_500.0, byId.getValue("item_9").finalPrice, 0.0)
        assertEquals(8_500.0, byId.getValue("item_10").finalPrice, 0.0)
        assertEquals(5_500.0, byId.getValue("item_11").finalPrice, 0.0)

        // Kerbs & concrete
        assertEquals(29_500.0, byId.getValue("item_12").finalPrice, 0.0)
        assertEquals(34_000.0, byId.getValue("item_12").priceWithProfit!!, 0.0)
        assertEquals(13_000.0, byId.getValue("item_13").finalPrice, 0.0)
        assertEquals(15_000.0, byId.getValue("item_13").priceWithProfit!!, 0.0)
        assertEquals(33_500.0, byId.getValue("item_14").finalPrice, 0.0)
        assertEquals(20_000.0, byId.getValue("item_15").finalPrice, 0.0)
        assertEquals(16_500.0, byId.getValue("item_16").finalPrice, 0.0)
        assertEquals(10_500.0, byId.getValue("item_17").finalPrice, 0.0)

        // Masonry & sewage
        assertEquals(73_250.0, byId.getValue("item_18").finalPrice, 0.0)
        assertEquals(84_250.0, byId.getValue("item_18").priceWithProfit!!, 0.0)
        assertEquals(6_500.0, byId.getValue("item_19").finalPrice, 0.0)
        assertEquals(91_750.0, byId.getValue("item_20").finalPrice, 0.0)
        assertEquals(168_500.0, byId.getValue("item_21").finalPrice, 0.0)
        assertEquals(1_269_500.0, byId.getValue("item_22").finalPrice, 0.0)
        assertEquals(1_460_000.0, byId.getValue("item_22").priceWithProfit!!, 0.0)
        assertEquals(449_250.0, byId.getValue("item_23").finalPrice, 0.0)
        assertEquals(1_082_750.0, byId.getValue("item_24").finalPrice, 0.0)

        // Asphalt & utilities
        assertEquals(9_000.0, byId.getValue("item_25_contractor").priceWithProfit!!, 0.0)
        assertEquals(6_250.0, byId.getValue("item_25_direct").finalPrice, 0.0)
        assertEquals(9_500.0, byId.getValue("item_26").finalPrice, 0.0)
        assertEquals(13_250.0, byId.getValue("item_27").priceWithProfit!!, 0.0)
        assertEquals(12_750.0, byId.getValue("item_28").finalPrice, 0.0)
        assertEquals(16_750.0, byId.getValue("item_29").priceWithProfit!!, 0.0)
        assertEquals(750.0, byId.getValue("item_30").finalPrice, 0.0)
        assertEquals(172_250.0, byId.getValue("item_31").finalPrice, 0.0)
        assertEquals(1_062_500.0, byId.getValue("item_32").finalPrice, 0.0)
        assertEquals(5_750.0, byId.getValue("item_33").finalPrice, 0.0)
        assertEquals(86_000.0, byId.getValue("item_34").finalPrice, 0.0)
        assertEquals(98_500.0, byId.getValue("item_35").finalPrice, 0.0)
        assertEquals(79_500.0, byId.getValue("item_36").finalPrice, 0.0)
        assertEquals(79_000.0, byId.getValue("item_37").finalPrice, 0.0)
        assertEquals(349_750.0, byId.getValue("item_38").finalPrice, 0.0)
        assertEquals(69_000.0, byId.getValue("item_39").finalPrice, 0.0)
        assertEquals(2_499_500.0, byId.getValue("item_48").finalPrice, 0.0)
        assertEquals(2_874_500.0, byId.getValue("item_48").priceWithProfit!!, 0.0)
        assertEquals(2_750.0, byId.getValue("item_49").finalPrice, 0.0)
        assertEquals(6_000.0, byId.getValue("item_51").finalPrice, 0.0)
        assertEquals(260_000.0, byId.getValue("item_52").finalPrice, 0.0)
        assertEquals(6_000.0, byId.getValue("item_53").finalPrice, 0.0)
        assertEquals(7_250.0, byId.getValue("item_54").finalPrice, 0.0)
    }

    @Test
    fun `changing material prices ripples through analyses and summary`() {
        val updated = defaultPrices.toMutableMap().apply {
            // Double concrete C25 price (mat_14: 69,750 -> 100,000)
            put("mat_14", 100_000.0)
        }
        val computed = BoqCalculatorEngine.computeAll(updated).associateBy { it.spec.id }

        val slab20 = computed.getValue("item_15")
        assertTrue(slab20.isChanged)
        assertTrue(slab20.finalPrice > 20_000.0)
    }
}
