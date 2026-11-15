package com.example.data.model

enum class MaterialCategory(val label: String) {
    ALL("الكل"),
    EQUIPMENT("الآليات والمعدات"),
    STRUCTURAL("المواد الإنشائية"),
    PIPES("الأنابيب والمنهولات"),
    LABOR("الأعمال والعمالة")
}

data class MaterialItem(
    val id: String,
    val index: Int,
    val name: String,
    val unit: String,
    val defaultPrice: Double,
    val category: MaterialCategory
)

enum class ItemCategory(val label: String) {
    ALL("الكل"),
    EARTHWORK("أعمال ترابية"),
    SUBBASE("حصى وحجر مكسر"),
    CONCRETE_KERBS("صب وقوالب"),
    BUILDINGS_WALLS("بناء وجدران ساندة"),
    SEWAGE_MANHOLES("مجاري ومنهولات"),
    ASPHALT("أعمال الأسفلت"),
    UTILITIES_PARKS("كهرباء وحدائق وماء")
}

sealed interface LineFormula {
    /**
     * Driven directly by a MaterialItem in Sheet 1.
     * unitPrice = (materialPrice / divisor) + addend
     * amount = quantity * unitPrice * amountMultiplier
     */
    data class FromMaterial(
        val materialId: String,
        val divisor: Double = 1.0,
        val addend: Double = 0.0,
        val amountMultiplier: Double = 1.0
    ) : LineFormula

    /**
     * Driven by the rounded finalPrice of another WorkItem (e.g. حفريات 5750, دفن 7500, بلوك 73250, لبخ 6500).
     */
    data class FromWorkItem(
        val sourceWorkItemId: String
    ) : LineFormula

    /**
     * Constant or sub-component rate not present in the 37-item Sheet 1 table.
     */
    data class Fixed(
        val defaultUnitPrice: Double,
        val fixedAmountOverride: Double? = null
    ) : LineFormula
}

data class AnalysisLineSpec(
    val id: String,
    val title: String,
    val quantity: Double,
    val unit: String,
    val formula: LineFormula,
    val sectionHeader: String? = null
)

enum class ProfitRule {
    BOTH,               // Shows both base price and price with 15% profit in summary
    ONLY_WITH_PROFIT,   // Contractor asphalt items: summary displays 15% profit column
    ONLY_WITHOUT_PROFIT,// Direct municipality execution & electrical items: summary displays base column
    NO_PROFIT_SAME      // Prime coat: both columns show base price without +15%
}

data class WorkItemSpec(
    val id: String,
    val summaryOrder: Int,
    val displayNumber: String,
    val title: String,
    val summaryTitle: String = title,
    val unit: String,
    val category: ItemCategory,
    val lines: List<AnalysisLineSpec> = emptyList(),
    val multiplierFromWorkItemId: String? = null,
    val multiplierFactor: Double = 1.0,
    val textOnlyPrice: String? = null,
    val extraNotes: List<Pair<String, Double>> = emptyList(),
    val profitRule: ProfitRule = ProfitRule.BOTH
)

data class ComputedLine(
    val spec: AnalysisLineSpec,
    val unitPrice: Double,
    val amount: Double,
    val isMaterialLinked: Boolean,
    val isWorkItemLinked: Boolean,
    val linkedMaterialName: String? = null,
    val linkedWorkItemName: String? = null,
    val isOverridden: Boolean = false
)

data class ComputedWorkItem(
    val spec: WorkItemSpec,
    val computedLines: List<ComputedLine>,
    val rawSum: Double,
    val finalPrice: Double,
    val defaultFinalPrice: Double,
    val priceWithProfit: Double?,
    val defaultPriceWithProfit: Double?,
    val priceDelta: Double,
    val derivedNoteVariants: List<Pair<String, Double>> = emptyList()
) {
    val isChanged: Boolean get() = priceDelta != 0.0
}
