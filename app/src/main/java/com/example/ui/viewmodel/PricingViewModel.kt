package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.catalog.BoqCalculatorEngine
import com.example.data.catalog.MaterialsCatalog
import com.example.data.model.ComputedWorkItem
import com.example.data.model.ItemCategory
import com.example.data.model.MaterialCategory
import com.example.data.model.MaterialItem
import com.example.data.model.ProfitRule
import com.example.data.repository.PricingRepository
import com.example.data.repository.StoredPricingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToLong

enum class SheetTab(val title: String) {
    MATERIALS("أسعار المواد"),
    ANALYSIS("تحليل الفقرات"),
    SUMMARY("ملخص الفقرات")
}

data class MaterialDisplayRow(
    val item: MaterialItem,
    val currentPrice: Double,
    val isModified: Boolean,
    val affectedItemsCount: Int
)

data class PricingUiState(
    val activeTab: SheetTab = SheetTab.MATERIALS,
    val materials: List<MaterialDisplayRow> = emptyList(),
    val filteredMaterials: List<MaterialDisplayRow> = emptyList(),
    val analysisItems: List<ComputedWorkItem> = emptyList(),
    val filteredAnalysisItems: List<ComputedWorkItem> = emptyList(),
    val summaryItems: List<ComputedWorkItem> = emptyList(),
    val filteredSummaryItems: List<ComputedWorkItem> = emptyList(),
    val selectedItem: ComputedWorkItem? = null,
    val materialSearch: String = "",
    val materialCategory: MaterialCategory = MaterialCategory.ALL,
    val onlyModifiedMaterials: Boolean = false,
    val itemSearch: String = "",
    val itemCategory: ItemCategory = ItemCategory.ALL,
    val onlyChangedSummaryItems: Boolean = false,
    val profitPercent: Int = 15,
    val modifiedMaterialsCount: Int = 0,
    val changedSummaryItemsCount: Int = 0,
    val estimatorQuantities: Map<String, Double> = emptyMap()
)

class PricingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PricingRepository(application.applicationContext)

    private val activeTab = MutableStateFlow(SheetTab.MATERIALS)
    private val materialSearch = MutableStateFlow("")
    private val materialCategory = MutableStateFlow(MaterialCategory.ALL)
    private val onlyModifiedMaterials = MutableStateFlow(false)
    private val itemSearch = MutableStateFlow("")
    private val itemCategory = MutableStateFlow(ItemCategory.ALL)
    private val onlyChangedSummary = MutableStateFlow(false)
    private val selectedItemId = MutableStateFlow<String?>(null)
    private val estimatorQuantities = MutableStateFlow<Map<String, Double>>(emptyMap())

    // In-memory fast mirror synced from DataStore for instant zero-lag recomputation
    private val storedState = repository.storedStateFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, StoredPricingState())

    private data class FilterState(
        val tab: SheetTab,
        val matSearch: String,
        val matCat: MaterialCategory,
        val onlyModMat: Boolean,
        val itmSearch: String,
        val itmCat: ItemCategory,
        val onlyChgSum: Boolean,
        val selectedId: String?,
        val quantities: Map<String, Double>
    )

    private val filterFlow = combine(
        activeTab,
        materialSearch,
        materialCategory,
        onlyModifiedMaterials,
        itemSearch,
        itemCategory,
        onlyChangedSummary,
        selectedItemId,
        estimatorQuantities
    ) { values ->
        FilterState(
            tab = values[0] as SheetTab,
            matSearch = values[1] as String,
            matCat = values[2] as MaterialCategory,
            onlyModMat = values[3] as Boolean,
            itmSearch = values[4] as String,
            itmCat = values[5] as ItemCategory,
            onlyChgSum = values[6] as Boolean,
            selectedId = values[7] as String?,
            quantities = @Suppress("UNCHECKED_CAST") (values[8] as Map<String, Double>)
        )
    }

    val uiState: StateFlow<PricingUiState> = combine(storedState, filterFlow) { stored, filters ->
        val materialRows = MaterialsCatalog.items.map { mat ->
            val current = stored.materialPrices[mat.id] ?: mat.defaultPrice
            MaterialDisplayRow(
                item = mat,
                currentPrice = current,
                isModified = current != mat.defaultPrice,
                affectedItemsCount = BoqCalculatorEngine.materialImpactCount[mat.id] ?: 0
            )
        }

        val filteredMats = materialRows.filter { row ->
            val matchesCat = filters.matCat == MaterialCategory.ALL || row.item.category == filters.matCat
            val matchesMod = !filters.onlyModMat || row.isModified
            val q = filters.matSearch.trim()
            val matchesSearch = q.isEmpty() ||
                row.item.name.contains(q, ignoreCase = true) ||
                row.item.index.toString() == q
            matchesCat && matchesMod && matchesSearch
        }

        val allComputed = BoqCalculatorEngine.computeAll(
            materialPrices = stored.materialPrices,
            lineOverrides = stored.lineOverrides,
            profitRate = stored.profitRate
        )

        // Show items that have breakdowns (or text note / multiplier) in Analysis tab
        val analysisItems = allComputed.filter {
            it.computedLines.isNotEmpty() || it.spec.textOnlyPrice != null
        }

        val filteredAnalysis = analysisItems.filter { computed ->
            val matchesCat = filters.itmCat == ItemCategory.ALL || computed.spec.category == filters.itmCat
            val q = filters.itmSearch.trim()
            val matchesSearch = q.isEmpty() ||
                computed.spec.title.contains(q, ignoreCase = true) ||
                computed.spec.displayNumber.contains(q, ignoreCase = true) ||
                computed.computedLines.any { it.spec.title.contains(q, ignoreCase = true) }
            matchesCat && matchesSearch
        }

        val summaryItems = allComputed
            .filter { it.spec.summaryOrder > 0 }
            .sortedBy { it.spec.summaryOrder }

        val filteredSummary = summaryItems.filter { computed ->
            val matchesCat = filters.itmCat == ItemCategory.ALL || computed.spec.category == filters.itmCat
            val matchesChanged = !filters.onlyChgSum || computed.isChanged
            val q = filters.itmSearch.trim()
            val matchesSearch = q.isEmpty() ||
                computed.spec.summaryTitle.contains(q, ignoreCase = true) ||
                computed.spec.title.contains(q, ignoreCase = true) ||
                computed.spec.displayNumber.contains(q, ignoreCase = true)
            matchesCat && matchesChanged && matchesSearch
        }

        val selected = filters.selectedId?.let { id -> allComputed.firstOrNull { it.spec.id == id } }

        PricingUiState(
            activeTab = filters.tab,
            materials = materialRows,
            filteredMaterials = filteredMats,
            analysisItems = analysisItems,
            filteredAnalysisItems = filteredAnalysis,
            summaryItems = summaryItems,
            filteredSummaryItems = filteredSummary,
            selectedItem = selected,
            materialSearch = filters.matSearch,
            materialCategory = filters.matCat,
            onlyModifiedMaterials = filters.onlyModMat,
            itemSearch = filters.itmSearch,
            itemCategory = filters.itmCat,
            onlyChangedSummaryItems = filters.onlyChgSum,
            profitPercent = (stored.profitRate * 100.0).toInt(),
            modifiedMaterialsCount = materialRows.count { it.isModified },
            changedSummaryItemsCount = summaryItems.count { it.isChanged },
            estimatorQuantities = filters.quantities
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PricingUiState())

    fun selectTab(tab: SheetTab) {
        activeTab.value = tab
    }

    fun updateMaterialSearch(query: String) {
        materialSearch.value = query
    }

    fun selectMaterialCategory(category: MaterialCategory) {
        materialCategory.value = category
    }

    fun toggleOnlyModifiedMaterials() {
        onlyModifiedMaterials.value = !onlyModifiedMaterials.value
    }

    fun updateItemSearch(query: String) {
        itemSearch.value = query
    }

    fun selectItemCategory(category: ItemCategory) {
        itemCategory.value = category
    }

    fun toggleOnlyChangedSummary() {
        onlyChangedSummary.value = !onlyChangedSummary.value
    }

    fun openItemDetail(itemId: String) {
        selectedItemId.value = itemId
    }

    fun closeItemDetail() {
        selectedItemId.value = null
    }

    fun updateMaterialPrice(materialId: String, newPrice: Double) {
        val mat = MaterialsCatalog.byId[materialId] ?: return
        viewModelScope.launch {
            if (newPrice == mat.defaultPrice) {
                repository.saveMaterialPrice(materialId, null)
            } else {
                repository.saveMaterialPrice(materialId, newPrice.coerceAtLeast(0.0))
            }
        }
    }

    fun resetMaterialPrice(materialId: String) {
        viewModelScope.launch {
            repository.saveMaterialPrice(materialId, null)
        }
    }

    fun updateLineOverride(lineId: String, newUnitPrice: Double) {
        viewModelScope.launch {
            repository.saveLineOverride(lineId, newUnitPrice.coerceAtLeast(0.0))
        }
    }

    fun resetLineOverride(lineId: String) {
        viewModelScope.launch {
            repository.saveLineOverride(lineId, null)
        }
    }

    fun updateProfitPercent(percent: Int) {
        val clamped = percent.coerceIn(0, 100)
        viewModelScope.launch {
            repository.saveProfitRate(clamped / 100.0)
        }
    }

    fun updateEstimatorQuantity(itemId: String, qty: Double?) {
        val updated = estimatorQuantities.value.toMutableMap()
        if (qty == null || qty <= 0.0) {
            updated.remove(itemId)
        } else {
            updated[itemId] = qty
        }
        estimatorQuantities.value = updated
    }

    fun resetAllToDefaults() {
        viewModelScope.launch {
            repository.resetAll()
            estimatorQuantities.value = emptyMap()
        }
    }

    fun buildShareableSummary(): String {
        val state = uiState.value
        val sb = StringBuilder()
        sb.appendLine("ملخص أسعار الفقرات الإنشائية (بالدينار)")
        sb.appendLine("نسبة الربح المعتمدة: ${state.profitPercent}%")
        sb.appendLine("─────────────────────────")
        for (item in state.summaryItems) {
            val spec = item.spec
            sb.append("${spec.displayNumber}. ${spec.summaryTitle} (${spec.unit}): ")
            if (spec.textOnlyPrice != null) {
                sb.appendLine(spec.textOnlyPrice)
            } else {
                val base = when (spec.profitRule) {
                    ProfitRule.ONLY_WITH_PROFIT -> "—"
                    else -> formatDinar(item.finalPrice)
                }
                val withProfit = item.priceWithProfit?.let { formatDinar(it) } ?: "—"
                sb.appendLine("بدون ربح: $base | مع ربح ${state.profitPercent}%: $withProfit")
            }
        }
        return sb.toString()
    }
}

fun formatDinar(amount: Double): String {
    val rounded = amount.roundToLong()
    return NumberFormat.getNumberInstance(Locale.US).format(rounded)
}

fun formatQuantity(qty: Double): String {
    return if (qty == qty.toLong().toDouble()) {
        qty.toLong().toString()
    } else {
        qty.toBigDecimal().stripTrailingZeros().toPlainString()
    }
}
