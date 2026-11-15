package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.ComputedLine
import com.example.data.model.ComputedWorkItem
import com.example.data.model.ItemCategory
import com.example.data.model.LineFormula
import com.example.data.model.ProfitRule
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.EditPriceDialog
import com.example.ui.components.PriceDeltaBadge
import com.example.ui.components.SearchField
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmberGoldContainer
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.OnAmberGoldContainer
import com.example.ui.theme.OnEmeraldContainer
import com.example.ui.viewmodel.PricingUiState
import com.example.ui.viewmodel.formatDinar
import com.example.ui.viewmodel.formatQuantity

@Composable
fun AnalysisScreen(
    state: PricingUiState,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (ItemCategory) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchField(
                query = state.itemSearch,
                onQueryChange = onSearchChange,
                placeholder = "ابحث في تحليل الفقرات أو اسم المادة (مثلاً: منهولات، قوالب)...",
                testTag = "analysis_search_field",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CategoryFilterRow(
                categories = ItemCategory.entries,
                selectedCategory = state.itemCategory,
                labelProvider = { it.label },
                onSelect = onCategorySelect
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.filteredAnalysisItems, key = { it.spec.id }) { item ->
                AnalysisSummaryCard(
                    item = item,
                    profitPercent = state.profitPercent,
                    onClick = { onItemClick(item.spec.id) }
                )
            }
        }
    }
}

@Composable
private fun AnalysisSummaryCard(
    item: ComputedWorkItem,
    profitPercent: Int,
    onClick: () -> Unit
) {
    val spec = item.spec
    val borderColor = if (item.isChanged) AmberGold else MaterialTheme.colorScheme.outlineVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("analysis_card_${spec.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(if (item.isChanged) 1.5.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    color = if (spec.summaryOrder < 0) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = spec.displayNumber,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (spec.summaryOrder < 0) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = spec.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${spec.category.label} • الوحدة: ${spec.unit} • ${item.computedLines.size} بند تحليل",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                PriceDeltaBadge(delta = item.priceDelta)
            }

            if (spec.textOnlyPrice != null) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = spec.textOnlyPrice,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            } else {
                // Preview first 3 lines of analysis
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item.computedLines.take(3).forEach { line ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${line.spec.title} (${formatQuantity(line.spec.quantity)} ${line.spec.unit})",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${formatDinar(line.amount)} د.ع",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    if (item.computedLines.size > 3) {
                        Text(
                            text = "+ عرض كل البنود (${item.computedLines.size}) وحاسبة الكميات...",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Totals strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TotalMetricChip(
                        label = "المجموع",
                        value = formatDinar(item.rawSum),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    TotalMetricChip(
                        label = "المبلغ النهائي",
                        value = formatDinar(item.finalPrice),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                    if (item.priceWithProfit != null && spec.profitRule != ProfitRule.NO_PROFIT_SAME) {
                        TotalMetricChip(
                            label = "مع ربح $profitPercent%",
                            value = formatDinar(item.priceWithProfit),
                            containerColor = EmeraldContainer,
                            contentColor = OnEmeraldContainer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TotalMetricChip(
    label: String,
    value: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.85f),
                maxLines = 1
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemAnalysisDetailSheet(
    item: ComputedWorkItem,
    profitPercent: Int,
    estimatorQuantity: Double?,
    onEstimatorQuantityChange: (Double?) -> Unit,
    onOverrideLinePrice: (String, Double) -> Unit,
    onResetLineOverride: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var editingLine by remember { mutableStateOf<ComputedLine?>(null) }
    var qtyInput by remember(item.spec.id) {
        mutableStateOf(estimatorQuantity?.let { formatQuantity(it) } ?: "")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Sheet Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "تحليل سعر الفقرة (${item.spec.displayNumber})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.spec.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "إغلاق")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (item.spec.multiplierFromWorkItemId != null) {
                    item {
                        Surface(
                            color = AmberGoldContainer,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "هذه الفقرة محسوبة تلقائياً بمضاعفة تحليل الطبقة الواحدة × ${formatQuantity(item.spec.multiplierFactor)}",
                                style = MaterialTheme.typography.labelMedium,
                                color = OnAmberGoldContainer,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }

                // Table header
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "المواد والآليات",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.weight(1.7f)
                            )
                            Text(
                                text = "الكمية",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.75f)
                            )
                            Text(
                                text = "السعر",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.95f)
                            )
                            Text(
                                text = "المبلغ",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                items(item.computedLines, key = { it.spec.id }) { line ->
                    AnalysisLineRow(
                        line = line,
                        onEditClick = { editingLine = line },
                        onResetOverride = { onResetLineOverride(line.spec.id) }
                    )
                }

                // Totals summary card
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SummaryRowLine("المجموع الفعلي", "${formatDinar(item.rawSum)} د.ع", false)
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            SummaryRowLine("المبلغ النهائي (مقرّب لأقرب 250)", "${formatDinar(item.finalPrice)} د.ع", true)
                            if (item.priceWithProfit != null && item.spec.profitRule != ProfitRule.NO_PROFIT_SAME) {
                                SummaryRowLine("السعر مع ربح $profitPercent%", "${formatDinar(item.priceWithProfit)} د.ع", true)
                            }
                            item.derivedNoteVariants.forEach { (label, value) ->
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                SummaryRowLine(label, "${formatDinar(value)} د.ع", true)
                            }
                        }
                    }
                }

                // Project Quantity Calculator
                if (item.spec.textOnlyPrice == null) {
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Calculate,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "حاسبة الكلفة الإجمالية للكمية (${item.spec.unit})",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                OutlinedTextField(
                                    value = qtyInput,
                                    onValueChange = { raw ->
                                        qtyInput = raw
                                        onEstimatorQuantityChange(raw.toDoubleOrNull())
                                    },
                                    label = { Text("أدخل الكمية المطلوبة (${item.spec.unit})") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("quantity_estimator_input")
                                )

                                val parsedQty = qtyInput.toDoubleOrNull() ?: 0.0
                                if (parsedQty > 0.0) {
                                    val totalBase = parsedQty * item.finalPrice
                                    val totalProfit = item.priceWithProfit?.let { parsedQty * it }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        TotalMetricChip(
                                            label = "الكلفة بدون ربح",
                                            value = "${formatDinar(totalBase)} د.ع",
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (totalProfit != null) {
                                            TotalMetricChip(
                                                label = "الكلفة مع ربح $profitPercent%",
                                                value = "${formatDinar(totalProfit)} د.ع",
                                                containerColor = EmeraldContainer,
                                                contentColor = OnEmeraldContainer,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    editingLine?.let { line ->
        val defaultVal = when (val f = line.spec.formula) {
            is LineFormula.Fixed -> f.defaultUnitPrice
            else -> line.unitPrice
        }
        EditPriceDialog(
            title = line.spec.title,
            subtitle = if (line.isMaterialLinked) {
                "ملاحظة: هذا البند مرتبط تلقائياً بـ (${line.linkedMaterialName}). تعديله هنا يضع قيمة مخصصة لهذا البند فقط."
            } else {
                "تعديل سعر هذا البند ينعكس مباشرة على المجموع النهائي للفقرة."
            },
            currentPrice = line.unitPrice,
            defaultPrice = defaultVal,
            unitLabel = line.spec.unit,
            onDismiss = { editingLine = null },
            onConfirm = { newUnitPrice ->
                onOverrideLinePrice(line.spec.id, newUnitPrice)
                editingLine = null
            },
            onResetDefault = {
                onResetLineOverride(line.spec.id)
                editingLine = null
            }
        )
    }
}

@Composable
private fun AnalysisLineRow(
    line: ComputedLine,
    onEditClick: () -> Unit,
    onResetOverride: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (line.spec.sectionHeader != null) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    text = line.spec.sectionHeader,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Surface(
            color = if (line.isOverridden) AmberGoldContainer.copy(alpha = 0.55f) else MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onEditClick)
        ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1.7f)) {
                        Text(
                            text = line.spec.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                        val linkText = when {
                            line.isOverridden -> "سعر مخصص (اضغط للإعادة)"
                            line.linkedMaterialName != null -> "مرتبط: ${line.linkedMaterialName}"
                            line.linkedWorkItemName != null -> "مرتبط: ${line.linkedWorkItemName}"
                            else -> null
                        }
                        if (linkText != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Link,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = linkText,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Text(
                        text = "${formatQuantity(line.spec.quantity)} ${line.spec.unit}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(0.75f)
                    )

                    Row(
                        modifier = Modifier.weight(0.95f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatDinar(line.unitPrice),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "تعديل",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .size(12.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatDinar(line.amount),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        if (line.isOverridden) {
                            IconButton(
                                onClick = onResetOverride,
                                modifier = Modifier.size(22.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.RestartAlt,
                                    contentDescription = "إعادة الافتراضي",
                                    tint = AmberGold,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRowLine(label: String, value: String, bold: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (bold) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = if (bold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            color = if (bold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
    }
}
