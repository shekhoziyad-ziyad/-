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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.ComputedWorkItem
import com.example.data.model.ItemCategory
import com.example.data.model.ProfitRule
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.PriceDeltaBadge
import com.example.ui.components.SearchField
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldAccent
import com.example.ui.viewmodel.PricingUiState
import com.example.ui.viewmodel.formatDinar

@Composable
fun SummaryScreen(
    state: PricingUiState,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (ItemCategory) -> Unit,
    onToggleOnlyChanged: () -> Unit,
    onItemClick: (String) -> Unit,
    onCopySummary: () -> Unit,
    onShareSummary: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Search + Modified filter + Share/Copy actions
        Column(
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchField(
                    query = state.itemSearch,
                    onQueryChange = onSearchChange,
                    placeholder = "ابحث في ملخص الفقرات...",
                    testTag = "summary_search_field",
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = state.onlyChangedSummaryItems,
                    onClick = onToggleOnlyChanged,
                    label = { Text("المتغيرة") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.FilterList,
                            contentDescription = "عرض الفقرات المتغيرة فقط",
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    modifier = Modifier.testTag("filter_changed_summary")
                )

                IconButton(
                    onClick = onCopySummary,
                    modifier = Modifier.testTag("copy_summary_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ContentCopy,
                        contentDescription = "نسخ ملخص الفقرات",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = onShareSummary,
                    modifier = Modifier.testTag("share_summary_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "مشاركة ملخص الفقرات",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            CategoryFilterRow(
                categories = ItemCategory.entries,
                selectedCategory = state.itemCategory,
                labelProvider = { it.label },
                onSelect = onCategorySelect
            )
        }

        // Table Header matching Excel Sheet "ملخص الفقرات"
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ت",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.35f)
                )
                Text(
                    text = "الفقرة",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1.9f)
                )
                Text(
                    text = "الوحدة",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.55f)
                )
                Text(
                    text = "بدون ربح",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(0.95f)
                )
                Text(
                    text = "مع ربح ${state.profitPercent}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(0.95f)
                )
            }
        }

        if (state.filteredSummaryItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.onlyChangedSummaryItems) {
                        "لم تتغير أسعار أي فقرة حتى الآن — عدّل سعر أي مادة في تبويب أسعار المواد لترى التحديث المباشر هنا."
                    } else {
                        "لا توجد فقرات مطابقة للبحث"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(state.filteredSummaryItems, key = { it.spec.id }) { item ->
                    SummaryTableRow(
                        item = item,
                        onClick = { onItemClick(item.spec.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryTableRow(
    item: ComputedWorkItem,
    onClick: () -> Unit
) {
    val spec = item.spec
    val borderColor = if (item.isChanged) AmberGold else MaterialTheme.colorScheme.outlineVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("summary_row_${spec.id}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(if (item.isChanged) 1.4.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = spec.displayNumber,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.35f)
                )

                Text(
                    text = spec.summaryTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1.9f)
                        .padding(horizontal = 4.dp)
                )

                Text(
                    text = spec.unit,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.55f)
                )

                if (spec.textOnlyPrice != null) {
                    Text(
                        text = spec.textOnlyPrice,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1.9f)
                    )
                } else {
                    val baseText = when (spec.profitRule) {
                        ProfitRule.ONLY_WITH_PROFIT -> "—"
                        else -> formatDinar(item.finalPrice)
                    }
                    val profitText = item.priceWithProfit?.let { formatDinar(it) } ?: "—"

                    Text(
                        text = baseText,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(0.95f)
                    )

                    Text(
                        text = profitText,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (item.priceWithProfit != null) EmeraldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(0.95f)
                    )
                }
            }

            if (item.isChanged) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "السعر الأصلي: ${formatDinar(item.defaultFinalPrice)} د.ع",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    PriceDeltaBadge(delta = item.priceDelta)
                }
            }
        }
    }
}
