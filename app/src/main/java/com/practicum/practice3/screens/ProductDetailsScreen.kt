package com.practicum.practice3.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.practicum.practice3.domain.model.Nutriments
import com.practicum.practice3.domain.model.Product
import com.practicum.practice3.storage.MockProductRepository
import com.practicum.practice3.viewmodel.ProductDetailsViewModel
import com.practicum.practice3.viewmodel.ProductDetailsViewModelFactory

@Composable
fun ProductDetailsScreen(
    productCode: String,
    onNavigateBack: () -> Unit,
    viewModel: ProductDetailsViewModel = viewModel(factory = ProductDetailsViewModelFactory()),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(productCode) {
        viewModel.loadProduct(productCode)
    }

    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
        }
        state.product != null -> {

            ProductDetailsContent(
                product = state.product!!,
                onBackClick = onNavigateBack
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsContent(
    product: Product,
    onBackClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(modifier = Modifier.height(280.dp)) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            )
                        )
                )

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = Color.Black
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = product.productName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (product.brands.isNotBlank()) {
                        Text(
                            text = product.brands,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (product.nutriscoreGrade != null) {
                    ScoreBadge(
                        label = "Nutri-Score",
                        grade = product.nutriscoreGrade,
                        color = getNutriscoreColor(product.nutriscoreGrade)
                    )
                }
                if (product.ecoscoreGrade != null) {
                    ScoreBadge(
                        label = "Eco-Score",
                        grade = product.ecoscoreGrade,
                        color = getNutriscoreColor(product.ecoscoreGrade)
                    )
                }
                if (product.quantity.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Объём",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = product.quantity,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (product.categories.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Категории",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.foundation.layout.FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        product.categories.forEach { category ->
                            SuggestionChip(
                                onClick = { },
                                label = { Text(category) }
                            )
                        }
                    }
                }
            }
        }

        item {
            NutritionTable(nutriments = product.nutriments)
        }

        if (product.ingredientsText.isNotBlank()) {
            item {
                SectionCard(
                    title = "Состав",
                    icon = Icons.Filled.RestaurantMenu
                ) {
                    Text(
                        text = product.ingredientsText,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        if (product.allergens.isNotEmpty()) {
            item {
                SectionCard(
                    title = "Аллергены",
                    icon = Icons.Filled.Warning
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        product.allergens.forEach { allergen ->
                            SuggestionChip(
                                onClick = { },
                                label = { Text(allergen) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color(0xFFFFEBEE),
                                    labelColor = Color(0xFFC62828)
                                )
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun ScoreBadge(label: String, grade: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color,
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = grade.uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun NutritionTable(nutriments: Nutriments) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Science,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Пищевая ценность (на 100 г)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            ) {

                val (calories, fat, carbs, sugars, proteins, salt) = createRefs()

                val verticalGuideline = createGuidelineFromStart(0.5f)

                val row1Guideline = createGuidelineFromTop(0f)
                val row2Guideline = createGuidelineFromTop(0.33f)
                val row3Guideline = createGuidelineFromTop(0.66f)

                NutritionCell(
                    icon = Icons.Filled.LocalFireDepartment,
                    label = "Калории",
                    value = "${nutriments.energyKcal}",
                    unit = "ккал",
                    color = Color(0xFFFF5722),
                    modifier = Modifier.constrainAs(calories) {
                        start.linkTo(parent.start)
                        end.linkTo(verticalGuideline)
                        top.linkTo(parent.top)
                        bottom.linkTo(row2Guideline)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                )

                NutritionCell(
                    icon = Icons.Filled.WaterDrop,
                    label = "Жиры",
                    value = "${nutriments.fat}",
                    unit = "г",
                    color = Color(0xFFFFC107),
                    modifier = Modifier.constrainAs(fat) {
                        start.linkTo(verticalGuideline)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(row2Guideline)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                )

                NutritionCell(
                    icon = Icons.Filled.BakeryDining,
                    label = "Углеводы",
                    value = "${nutriments.carbohydrates}",
                    unit = "г",
                    color = Color(0xFF8BC34A),
                    modifier = Modifier.constrainAs(carbs) {
                        start.linkTo(parent.start)
                        end.linkTo(verticalGuideline)
                        top.linkTo(row2Guideline)
                        bottom.linkTo(row3Guideline)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                )

                NutritionCell(
                    icon = Icons.Filled.Cake,
                    label = "Сахар",
                    value = "${nutriments.sugars}",
                    unit = "г",
                    color = Color(0xFFE91E63),
                    modifier = Modifier.constrainAs(sugars) {
                        start.linkTo(verticalGuideline)
                        end.linkTo(parent.end)
                        top.linkTo(row2Guideline)
                        bottom.linkTo(row3Guideline)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                )

                NutritionCell(
                    icon = Icons.Filled.FitnessCenter,
                    label = "Белки",
                    value = "${nutriments.proteins}",
                    unit = "г",
                    color = Color(0xFF2196F3),
                    modifier = Modifier.constrainAs(proteins) {
                        start.linkTo(parent.start)
                        end.linkTo(verticalGuideline)
                        top.linkTo(row3Guideline)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                )

                NutritionCell(
                    icon = Icons.Filled.WaterDrop,
                    label = "Соль",
                    value = "${nutriments.salt}",
                    unit = "г",
                    color = Color(0xFF607D8B),
                    modifier = Modifier.constrainAs(salt) {
                        start.linkTo(verticalGuideline)
                        end.linkTo(parent.end)
                        top.linkTo(row3Guideline)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                )
            }
        }
    }
}

@Composable
private fun NutritionCell(
    icon: ImageVector,
    label: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

private fun getNutriscoreColor(grade: String): Color {
    return when (grade.lowercase()) {
        "a" -> Color(0xFF038141)
        "b" -> Color(0xFF85BB2F)
        "c" -> Color(0xFFFECB02)
        "d" -> Color(0xFFEE8100)
        "e" -> Color(0xFFE63E11)
        else -> Color.Gray
    }
}