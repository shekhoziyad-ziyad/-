package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.MaterialCategory
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.EditPriceDialog
import com.example.ui.components.SearchField
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmberGoldContainer
import com.example.ui.theme.OnAmberGoldContainer
import com.example.ui.viewmodel.MaterialDisplayRow
import com.example.ui.viewmodel.PricingUiState
import com.example.ui.viewmodel.formatDinar

@Composable
fun MaterialsScreen(
    state: PricingUiState,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (MaterialCategory) -> Unit,
    onToggleOnlyModified: () -> Unit,
    onUpdateMaterialPrice: (String, Double) -> Unit,
    onResetMaterialPrice: (String) -> Unit,
    onJumpToSummary: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editingRow by remember { mutableStateOf<MaterialDisplayRow?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        // Search & Filter controls
        Column(
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchField(
                    query = state.materialSearch,
                    onQueryChange = onSearchChange,
                    placeholder = "ابحث عن مادة أو آلية (مثلاً: بلدوزر، سمنت، حديد)...",
                    testTag = "materials_search_field",
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = state.onlyModifiedMaterials,
                    onClick = onToggleOnlyModified,
                    label = { Text("المعدلة") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.FilterList,
                            contentDescription = "عرض المواد المعدلة فقط",
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    modifier = Modifier.testTag("filter_modified_materials")
                )
            }

            CategoryFilterRow(
                categories = MaterialCategory.entries,
                selectedCategory = state.materialCategory,
                labelProvider = { it.label },
                onSelect = onCategorySelect
            )
        }

        // Live ripple feedback banner when materials are modified
        AnimatedVisibility(visible = state.modifiedMaterialsCount > 0) {
            Surface(
                color = AmberGoldContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onJumpToSummary)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "تم تعديل ${state.modifiedMaterialsCount} مادة — تحدّثت ${state.changedSummaryItemsCount} فقرة مباشرة",
                        style = MaterialTheme.typography.labelMedium,
                        color = OnAmberGoldContainer,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "عرض الملخص",
                            style = MaterialTheme.typography.labelMedium,
                            color = OnAmberGoldContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = OnAmberGoldContainer,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        if (state.filteredMaterials.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Inventory2,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "لا توجد مواد مطابقة للبحث",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (state.onlyModifiedMaterials || state.materialSearch.isNotEmpty()) {
                        TextButton(onClick = {
                            onSearchChange("")
                            if (state.onlyModifiedMaterials) onToggleOnlyModified()
                            onCategorySelect(MaterialCategory.ALL)
                        }) {
                            Text("إظهار جميع المواد")
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.filteredMaterials, key = { it.item.id }) { row ->
                    MaterialPriceCard(
                        row = row,
                        onEditClick = { editingRow = row },
                        onResetClick = { onResetMaterialPrice(row.item.id) }
                    )
                }
            }
        }
    }

    editingRow?.let { row ->
        EditPriceDialog(
            title = row.item.name,
            subtitle = "تحديث سعر المادة سيقوم فوراً بإعادة احتساب جميع الفقرات المرتبطة (${row.affectedItemsCount} فقرة).",
            currentPrice = row.currentPrice,
            defaultPrice = row.item.defaultPrice,
            unitLabel = row.item.unit,
            onDismiss = { editingRow = null },
            onConfirm = { newPrice ->
                onUpdateMaterialPrice(row.item.id, newPrice)
                editingRow = null
            },
            onResetDefault = {
                onResetMaterialPrice(row.item.id)
                editingRow = null
            }
        )
    }
}

@Composable
private fun MaterialPriceCard(
    row: MaterialDisplayRow,
    onEditClick: () -> Unit,
    onResetClick: () -> Unit
) {
    val item = row.item
    val borderColor = if (row.isModified) AmberGold else MaterialTheme.colorScheme.outlineVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditClick)
            .testTag("material_card_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(if (row.isModified) 1.5.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (row.isModified) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Index badge
            Surface(
                color = if (row.isModified) AmberGoldContainer else MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = item.index.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        color = if (row.isModified) OnAmberGoldContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Name, unit & impact count
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "الوحدة: ${item.unit}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (row.affectedItemsCount > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "تؤثر على ${row.affectedItemsCount} فقرة",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }

            // Price pill + edit/reset
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    color = if (row.isModified) AmberGoldContainer else MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = formatDinar(row.currentPrice),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (row.isModified) OnAmberGoldContainer else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "د.ع",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (row.isModified) OnAmberGoldContainer else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "تعديل السعر",
                            tint = if (row.isModified) OnAmberGoldContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                if (row.isModified) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = formatDinar(item.defaultPrice),
                            style = MaterialTheme.typography.labelSmall.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        IconButton(
                            onClick = onResetClick,
                            modifier = Modifier
                                .size(24.dp)
                                .testTag("reset_material_${item.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.RestartAlt,
                                contentDescription = "إعادة السعر الافتراضي",
                                tint = AmberGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
