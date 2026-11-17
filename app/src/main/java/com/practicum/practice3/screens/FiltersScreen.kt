package com.practicum.practice3.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.practicum.practice3.domain.model.FilterOptions
import com.practicum.practice3.domain.model.ProductFilters
import com.practicum.practice3.viewmodel.FiltersViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FiltersScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FiltersViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.applied.collect { onNavigateBack() }
    }

    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        FiltersContent(
            filters = state.filters,
            onQueryChanged = viewModel::onQueryChanged,
            onCategorySelected = viewModel::onCategorySelected,
            onMinNutriscoreSelected = viewModel::onMinNutriscoreSelected,
            onReset = viewModel::onResetClicked,
            onApply = viewModel::onApplyClicked,
            modifier = modifier
        )
    }
}

@Composable
private fun FiltersContent(
    filters: ProductFilters,
    onQueryChanged: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onMinNutriscoreSelected: (String?) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = filters.query,
            onValueChange = onQueryChanged,
            label = { Text("Название продукта") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        SectionTitle("Категория")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filters.category == null,
                onClick = { onCategorySelected(null) },
                label = { Text("Все") }
            )
            FilterOptions.categories.forEach { category ->
                FilterChip(
                    selected = filters.category == category,
                    onClick = { onCategorySelected(category) },
                    label = { Text(category.replaceFirstChar { it.uppercase() }) }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        SectionTitle("Минимальная оценка Nutri-Score")
        Text(
            text = "Показывать продукты с оценкой не хуже выбранной",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filters.minNutriscore == null,
                onClick = { onMinNutriscoreSelected(null) },
                label = { Text("Любая") }
            )
            FilterOptions.nutriscoreGrades.forEach { grade ->
                FilterChip(
                    selected = filters.minNutriscore == grade,
                    onClick = { onMinNutriscoreSelected(grade) },
                    label = { Text(grade.uppercase()) }
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(onClick = onReset, modifier = Modifier.weight(1f)) {
                Text("Сбросить")
            }
            Button(onClick = onApply, modifier = Modifier.weight(1f)) {
                Text("Готово")
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}