package com.example.data.catalog

import com.example.data.model.ComputedLine
import com.example.data.model.ComputedWorkItem
import com.example.data.model.LineFormula
import com.example.data.model.ProfitRule
import com.example.data.model.WorkItemSpec
import kotlin.math.round

object BoqCalculatorEngine {
    val allWorkItems: List<WorkItemSpec> =
        WorkItemsCatalogPart1.items + WorkItemsCatalogPart2.items

    /**
     * Excel MROUND(value, 250): rounds to nearest multiple of 250 dinars,
     * with exact halves (125) rounding away from zero.
     */
    fun mround250(value: Double): Double {
        if (value <= 0.0) return 0.0
        val quotient = value / 250.0
        return kotlin.math.floor(quotient + 0.5) * 250.0
    }

    /**
     * Counts how many work items directly or transitively depend on each materialId.
     */
    val materialImpactCount: Map<String, Int> by lazy {
        val directDeps = mutableMapOf<String, MutableSet<String>>()
        for (item in allWorkItems) {
            val mats = item.lines.mapNotNull { (it.formula as? LineFormula.FromMaterial)?.materialId }
            for (m in mats) {
                directDeps.getOrPut(m) { mutableSetOf() }.add(item.id)
            }
        }
        // Propagate transitive dependencies (e.g. mat -> item_3 -> item_20, or base_soil_15 -> item_5)
        val itemDependents = mutableMapOf<String, MutableSet<String>>()
        for (item in allWorkItems) {
            item.multiplierFromWorkItemId?.let { src ->
                itemDependents.getOrPut(src) { mutableSetOf() }.add(item.id)
            }
            for (line in item.lines) {
                (line.formula as? LineFormula.FromWorkItem)?.let { ref ->
                    itemDependents.getOrPut(ref.sourceWorkItemId) { mutableSetOf() }.add(item.id)
                }
            }
        }
        val result = mutableMapOf<String, Int>()
        for (mat in MaterialsCatalog.items) {
            val visited = mutableSetOf<String>()
            val queue = ArrayDeque<String>()
            directDeps[mat.id]?.forEach {
                visited.add(it)
                queue.add(it)
            }
            while (queue.isNotEmpty()) {
                val curr = queue.removeFirst()
                itemDependents[curr]?.forEach { next ->
                    if (visited.add(next)) {
                        queue.add(next)
                    }
                }
            }
            result[mat.id] = visited.count { id ->
                allWorkItems.firstOrNull { it.id == id }?.summaryOrder?.let { it > 0 } ?: false
            }
        }
        result
    }

    private val defaultPrices: Map<String, Double> =
        MaterialsCatalog.items.associate { it.id to it.defaultPrice }

    private val baselineComputedById: Map<String, ComputedWorkItem> by lazy {
        computeMap(defaultPrices, emptyMap(), 0.15, baselineMap = null)
    }

    fun computeAll(
        materialPrices: Map<String, Double>,
        lineOverrides: Map<String, Double> = emptyMap(),
        profitRate: Double = 0.15
    ): List<ComputedWorkItem> {
        val computedMap = computeMap(materialPrices, lineOverrides, profitRate, baselineMap = baselineComputedById)
        return allWorkItems.mapNotNull { computedMap[it.id] }
    }

    private fun computeMap(
        materialPrices: Map<String, Double>,
        lineOverrides: Map<String, Double>,
        profitRate: Double,
        baselineMap: Map<String, ComputedWorkItem>?
    ): Map<String, ComputedWorkItem> {
        val results = LinkedHashMap<String, ComputedWorkItem>(allWorkItems.size)
        val workItemTitles = allWorkItems.associate { it.id to it.title }

        // Resolve items in dependency order: base items first, then derived items
        val remaining = allWorkItems.toMutableList()
        var guard = 0
        while (remaining.isNotEmpty() && guard < 10) {
            guard++
            val iterator = remaining.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                val deps = buildList {
                    item.multiplierFromWorkItemId?.let { add(it) }
                    item.lines.forEach { line ->
                        (line.formula as? LineFormula.FromWorkItem)?.let { add(it.sourceWorkItemId) }
                    }
                }
                if (deps.all { it in results }) {
                    results[item.id] = computeSingle(
                        spec = item,
                        materialPrices = materialPrices,
                        lineOverrides = lineOverrides,
                        resolvedItems = results,
                        workItemTitles = workItemTitles,
                        profitRate = profitRate,
                        baseline = baselineMap?.get(item.id)
                    )
                    iterator.remove()
                }
            }
        }
        return results
    }

    private fun computeSingle(
        spec: WorkItemSpec,
        materialPrices: Map<String, Double>,
        lineOverrides: Map<String, Double>,
        resolvedItems: Map<String, ComputedWorkItem>,
        workItemTitles: Map<String, String>,
        profitRate: Double,
        baseline: ComputedWorkItem?
    ): ComputedWorkItem {
        if (spec.textOnlyPrice != null) {
            return ComputedWorkItem(
                spec = spec,
                computedLines = emptyList(),
                rawSum = 0.0,
                finalPrice = 0.0,
                defaultFinalPrice = 0.0,
                priceWithProfit = null,
                defaultPriceWithProfit = null,
                priceDelta = 0.0
            )
        }

        if (spec.multiplierFromWorkItemId != null) {
            val baseItem = resolvedItems.getValue(spec.multiplierFromWorkItemId)
            val finalPrice = baseItem.finalPrice * spec.multiplierFactor
            val priceWithProfit = computeProfitPrice(finalPrice, spec.profitRule, profitRate)
            val defaultFinal = baseline?.finalPrice ?: finalPrice
            val defaultProfit = baseline?.priceWithProfit ?: priceWithProfit
            return ComputedWorkItem(
                spec = spec,
                computedLines = baseItem.computedLines,
                rawSum = baseItem.rawSum * spec.multiplierFactor,
                finalPrice = finalPrice,
                defaultFinalPrice = defaultFinal,
                priceWithProfit = priceWithProfit,
                defaultPriceWithProfit = defaultProfit,
                priceDelta = finalPrice - defaultFinal
            )
        }

        val computedLines = spec.lines.map { line ->
            val overridePrice = lineOverrides[line.id]
            when (val formula = line.formula) {
                is LineFormula.FromMaterial -> {
                    val matPrice = materialPrices[formula.materialId]
                        ?: defaultPrices[formula.materialId]
                        ?: 0.0
                    val matName = MaterialsCatalog.byId[formula.materialId]?.name
                    val unitPrice = overridePrice ?: ((matPrice / formula.divisor) + formula.addend)
                    val amount = round(line.quantity * unitPrice * formula.amountMultiplier)
                    ComputedLine(
                        spec = line,
                        unitPrice = unitPrice,
                        amount = amount,
                        isMaterialLinked = true,
                        isWorkItemLinked = false,
                        linkedMaterialName = matName,
                        isOverridden = overridePrice != null
                    )
                }

                is LineFormula.FromWorkItem -> {
                    val srcFinal = resolvedItems.getValue(formula.sourceWorkItemId).finalPrice
                    val unitPrice = overridePrice ?: srcFinal
                    val amount = round(line.quantity * unitPrice)
                    ComputedLine(
                        spec = line,
                        unitPrice = unitPrice,
                        amount = amount,
                        isMaterialLinked = false,
                        isWorkItemLinked = true,
                        linkedWorkItemName = workItemTitles[formula.sourceWorkItemId],
                        isOverridden = overridePrice != null
                    )
                }

                is LineFormula.Fixed -> {
                    val unitPrice = overridePrice ?: formula.defaultUnitPrice
                    val amount = if (overridePrice == null && formula.fixedAmountOverride != null) {
                        formula.fixedAmountOverride
                    } else {
                        round(line.quantity * unitPrice)
                    }
                    ComputedLine(
                        spec = line,
                        unitPrice = unitPrice,
                        amount = amount,
                        isMaterialLinked = false,
                        isWorkItemLinked = false,
                        isOverridden = overridePrice != null
                    )
                }
            }
        }

        val rawSum = computedLines.sumOf { it.amount }
        val finalPrice = mround250(rawSum)
        val priceWithProfit = computeProfitPrice(finalPrice, spec.profitRule, profitRate)
        val defaultFinal = baseline?.finalPrice ?: finalPrice
        val defaultProfit = baseline?.priceWithProfit ?: priceWithProfit
        val variants = spec.extraNotes.map { (label, factor) ->
            label to (finalPrice * factor)
        }

        return ComputedWorkItem(
            spec = spec,
            computedLines = computedLines,
            rawSum = rawSum,
            finalPrice = finalPrice,
            defaultFinalPrice = defaultFinal,
            priceWithProfit = priceWithProfit,
            defaultPriceWithProfit = defaultProfit,
            priceDelta = finalPrice - defaultFinal,
            derivedNoteVariants = variants
        )
    }

    private fun computeProfitPrice(
        finalPrice: Double,
        rule: ProfitRule,
        profitRate: Double
    ): Double? = when (rule) {
        ProfitRule.BOTH, ProfitRule.ONLY_WITH_PROFIT -> mround250(finalPrice * (1.0 + profitRate))
        ProfitRule.ONLY_WITHOUT_PROFIT -> null
        ProfitRule.NO_PROFIT_SAME -> finalPrice
    }
}
