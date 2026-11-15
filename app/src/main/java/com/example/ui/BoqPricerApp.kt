package com.example.ui

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.TableChart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BlueprintHeroHeader
import com.example.ui.components.ConfirmResetAllDialog
import com.example.ui.components.ProfitPercentDialog
import com.example.ui.screens.AnalysisScreen
import com.example.ui.screens.ItemAnalysisDetailSheet
import com.example.ui.screens.MaterialsScreen
import com.example.ui.screens.SummaryScreen
import com.example.ui.theme.BlueprintNavy
import com.example.ui.viewmodel.PricingViewModel
import com.example.ui.viewmodel.SheetTab
import kotlinx.coroutines.launch

@Composable
fun BoqPricerApp(
    viewModel: PricingViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var showProfitDialog by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    // Support back button to return from secondary tabs to the Materials sheet
    BackHandler(enabled = state.selectedItem != null || state.activeTab != SheetTab.MATERIALS) {
        if (state.selectedItem != null) {
            viewModel.closeItemDetail()
        } else {
            viewModel.selectTab(SheetTab.MATERIALS)
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useRail = maxWidth >= 600.dp

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                bottomBar = {
                    if (!useRail) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 4.dp
                        ) {
                            SheetTab.entries.forEach { tab ->
                                val selected = state.activeTab == tab
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { viewModel.selectTab(tab) },
                                    icon = {
                                        TabIconWithBadge(
                                            tab = tab,
                                            selected = selected,
                                            modifiedMaterialsCount = state.modifiedMaterialsCount,
                                            changedSummaryCount = state.changedSummaryItemsCount
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.title,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    },
                                    modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (useRail) {
                        NavigationRail(
                            containerColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .fillMaxHeight()
                                .statusBarsPadding()
                        ) {
                            Spacer(modifier = Modifier.weight(0.2f))
                            SheetTab.entries.forEach { tab ->
                                val selected = state.activeTab == tab
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { viewModel.selectTab(tab) },
                                    icon = {
                                        TabIconWithBadge(
                                            tab = tab,
                                            selected = selected,
                                            modifiedMaterialsCount = state.modifiedMaterialsCount,
                                            changedSummaryCount = state.changedSummaryItemsCount
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.title,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    modifier = Modifier.testTag("rail_tab_${tab.name.lowercase()}")
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top Blueprint Header respecting status bar insets
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BlueprintNavy)
                                .statusBarsPadding()
                        ) {
                            BlueprintHeroHeader(
                                title = "حاسبة الفقرات الإنشائية",
                                subtitle = when (state.activeTab) {
                                    SheetTab.MATERIALS -> "أدخل أسعار المواد لتتحدث جميع الفقرات تلقائياً"
                                    SheetTab.ANALYSIS -> "تفاصيل تحليل السعر لكل فقرة وحاسبة الكميات"
                                    SheetTab.SUMMARY -> "ملخص أسعار الفقرات النهائي (بدون ومع الربح)"
                                },
                                modifiedMaterialsCount = state.modifiedMaterialsCount,
                                changedItemsCount = state.changedSummaryItemsCount,
                                profitPercent = state.profitPercent,
                                onProfitClick = { showProfitDialog = true },
                                onResetAllClick = { showResetConfirmDialog = true }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .widthIn(max = 920.dp)
                        ) {
                            when (state.activeTab) {
                                SheetTab.MATERIALS -> MaterialsScreen(
                                    state = state,
                                    onSearchChange = viewModel::updateMaterialSearch,
                                    onCategorySelect = viewModel::selectMaterialCategory,
                                    onToggleOnlyModified = viewModel::toggleOnlyModifiedMaterials,
                                    onUpdateMaterialPrice = viewModel::updateMaterialPrice,
                                    onResetMaterialPrice = viewModel::resetMaterialPrice,
                                    onJumpToSummary = { viewModel.selectTab(SheetTab.SUMMARY) }
                                )

                                SheetTab.ANALYSIS -> AnalysisScreen(
                                    state = state,
                                    onSearchChange = viewModel::updateItemSearch,
                                    onCategorySelect = viewModel::selectItemCategory,
                                    onItemClick = viewModel::openItemDetail
                                )

                                SheetTab.SUMMARY -> SummaryScreen(
                                    state = state,
                                    onSearchChange = viewModel::updateItemSearch,
                                    onCategorySelect = viewModel::selectItemCategory,
                                    onToggleOnlyChanged = viewModel::toggleOnlyChangedSummary,
                                    onItemClick = { itemId ->
                                        val clicked = state.summaryItems.firstOrNull { it.spec.id == itemId }
                                        if (clicked != null && clicked.spec.textOnlyPrice == null) {
                                            viewModel.openItemDetail(itemId)
                                        }
                                    },
                                    onCopySummary = {
                                        val text = viewModel.buildShareableSummary()
                                        clipboardManager.setText(AnnotatedString(text))
                                        scope.launch {
                                            snackbarHostState.showSnackbar("تم نسخ ملخص أسعار الفقرات إلى الحافظة")
                                        }
                                    },
                                    onShareSummary = {
                                        val text = viewModel.buildShareableSummary()
                                        val intent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT, text)
                                        }
                                        context.startActivity(
                                            Intent.createChooser(intent, "مشاركة ملخص أسعار الفقرات")
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Item breakdown modal sheet
            state.selectedItem?.let { selected ->
                ItemAnalysisDetailSheet(
                    item = selected,
                    profitPercent = state.profitPercent,
                    estimatorQuantity = state.estimatorQuantities[selected.spec.id],
                    onEstimatorQuantityChange = { qty ->
                        viewModel.updateEstimatorQuantity(selected.spec.id, qty)
                    },
                    onOverrideLinePrice = viewModel::updateLineOverride,
                    onResetLineOverride = viewModel::resetLineOverride,
                    onDismiss = viewModel::closeItemDetail
                )
            }

            if (showProfitDialog) {
                ProfitPercentDialog(
                    currentPercent = state.profitPercent,
                    onDismiss = { showProfitDialog = false },
                    onConfirm = { percent ->
                        viewModel.updateProfitPercent(percent)
                        showProfitDialog = false
                    }
                )
            }

            if (showResetConfirmDialog) {
                ConfirmResetAllDialog(
                    onDismiss = { showResetConfirmDialog = false },
                    onConfirm = {
                        viewModel.resetAllToDefaults()
                        showResetConfirmDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("تمت استعادة الأسعار الافتراضية بنجاح")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun TabIconWithBadge(
    tab: SheetTab,
    selected: Boolean,
    modifiedMaterialsCount: Int,
    changedSummaryCount: Int
) {
    val icon: ImageVector = when (tab) {
        SheetTab.MATERIALS -> if (selected) Icons.Rounded.Inventory2 else Icons.Outlined.Inventory2
        SheetTab.ANALYSIS -> if (selected) Icons.Rounded.Analytics else Icons.Outlined.Analytics
        SheetTab.SUMMARY -> if (selected) Icons.Rounded.TableChart else Icons.Outlined.TableChart
    }
    val badgeCount = when (tab) {
        SheetTab.MATERIALS -> modifiedMaterialsCount
        SheetTab.ANALYSIS -> 0
        SheetTab.SUMMARY -> changedSummaryCount
    }

    if (badgeCount > 0) {
        BadgedBox(
            badge = {
                Badge {
                    Text(badgeCount.toString())
                }
            }
        ) {
            Icon(imageVector = icon, contentDescription = tab.title)
        }
    } else {
        Icon(imageVector = icon, contentDescription = tab.title)
    }
}
