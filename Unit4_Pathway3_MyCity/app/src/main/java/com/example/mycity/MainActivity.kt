import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ====================================================================================
// 1. LỚP DỮ LIỆU (DATA LAYER)
// ====================================================================================

data class Recommendation(
    val id: Int,
    val name: String,
    val description: String,
    val address: String
)

data class Category(
    val key: String,
    val name: String,
    val icon: ImageVector,
    val items: List<Recommendation>
)

object MockData {
    val categories = listOf(
        Category(
            "Cafes", "Quán Cà Phê", Icons.Filled.Coffee,
            listOf(
                Recommendation(1, "Cộng Cà Phê", "Phong cách bao cấp độc đáo, nổi tiếng với cà phê cốt dừa.", "Nhiều chi nhánh"),
                Recommendation(2, "Giảng Cafe", "Nơi khai sinh ra món cà phê trứng trứ danh của Hà Nội.", "39 P. Nguyễn Hữu Huân"),
                Recommendation(3, "Loading T", "Quán cổ điển, không gian yên tĩnh, lãng mạn.", "8 P. Chân Cầm"),
            )
        ),
        Category(
            "Parks", "Công Viên & Hồ", Icons.Filled.NaturePeople,
            listOf(
                Recommendation(4, "Hồ Gươm", "Hồ nước lịch sử, trung tâm của thành phố, nơi có Tháp Rùa.", "Quận Hoàn Kiếm"),
                Recommendation(5, "Công Viên Thống Nhất", "Một trong những công viên lớn nhất Hà Nội, thích hợp để chạy bộ.", "Quận Hai Bà Trưng"),
            )
        ),
        Category(
            "Museums", "Bảo Tàng", Icons.Filled.Museum,
            listOf(
                Recommendation(6, "Bảo Tàng Dân Tộc Học", "Lưu giữ và trưng bày văn hoá 54 dân tộc Việt Nam.", "Đường Nguyễn Văn Huyên"),
                Recommendation(7, "Bảo Tàng Lịch Sử Quốc Gia", "Trưng bày hiện vật từ thời tiền sử đến Cách mạng tháng Tám.", "1 P. Tràng Tiền"),
            )
        )
    )
}

// ====================================================================================
// 2. LỚP LOGIC & TRẠNG THÁI (VIEWMODEL & UDF)
// ====================================================================================

data class GuideUiState(
    val selectedCategoryKey: String? = null,
    val selectedItemId: Int? = null,
    val isTwoPane: Boolean = false
) {
    val currentCategory: Category?
        get() = MockData.categories.find { it.key == selectedCategoryKey }

    val currentRecommendation: Recommendation?
        get() = currentCategory?.items?.find { it.id == selectedItemId }

    val recommendationList: List<Recommendation>
        get() = currentCategory?.items ?: emptyList()
}

class CityGuideViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GuideUiState())
    val uiState: StateFlow<GuideUiState> = _uiState.asStateFlow()

    init {
        // Màn hình bắt đầu luôn chọn danh mục đầu tiên cho chế độ Two-Pane
        _uiState.update { it.copy(selectedCategoryKey = MockData.categories.first().key) }
    }

    // Cập nhật trạng thái kích thước màn hình
    fun setIsTwoPane(isTwoPane: Boolean) {
        _uiState.update { it.copy(isTwoPane = isTwoPane) }
    }

    // Xử lý sự kiện khi chọn một danh mục (tương đương navigate/cập nhật state)
    fun selectCategory(categoryKey: String) {
        _uiState.update {
            it.copy(
                selectedCategoryKey = categoryKey,
                selectedItemId = null // Reset chi tiết khi đổi danh mục
            )
        }
    }

    // Xử lý sự kiện khi chọn một mục chi tiết
    fun selectItem(itemId: Int) {
        _uiState.update { it.copy(selectedItemId = itemId) }
    }
}

// ====================================================================================
// 3. ĐIỀU HƯỚNG (NAVIGATION ROUTES)
// ====================================================================================

sealed class Screen(val route: String) {
    data object CategoryList : Screen("category_list")
    data object RecommendationList : Screen("recommendation_list")
    data object Detail : Screen("detail")
}

// ====================================================================================
// 4. LỚP GIAO DIỆN NGƯỜI DÙNG (UI LAYER)
// ====================================================================================

@Composable
fun CategoryItem(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = contentColor,
                modifier = Modifier.size(28.dp).padding(end = 8.dp)
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "(${category.items.size})",
                color = contentColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun CategoryListScreen(
    uiState: GuideUiState,
    onCategorySelected: (String) -> Unit,
    onNavigateToList: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Khám Phá Hà Nội") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(MockData.categories, key = { it.key }) { category ->
                val isSelected = category.key == uiState.selectedCategoryKey
                CategoryItem(
                    category = category,
                    isSelected = isSelected,
                    onClick = {
                        onCategorySelected(category.key)
                        if (!uiState.isTwoPane) {
                            onNavigateToList()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RecommendationListItem(recommendation: Recommendation, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = recommendation.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = recommendation.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.LocationOn, contentDescription = "Địa chỉ", Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = recommendation.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun RecommendationListScreen(
    uiState: GuideUiState,
    navController: NavController,
    onItemSelected: (Int) -> Unit
) {
    val category = uiState.currentCategory ?: return // Nếu không có danh mục, thoát

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách ${category.name}") },
                navigationIcon = {
                    if (!uiState.isTwoPane) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(uiState.recommendationList, key = { it.id }) { recommendation ->
                RecommendationListItem(
                    recommendation = recommendation,
                    onClick = {
                        onItemSelected(recommendation.id)
                        if (!uiState.isTwoPane) {
                            navController.navigate(Screen.Detail.route)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DetailScreen(
    uiState: GuideUiState,
    navController: NavController
) {
    val item = uiState.currentRecommendation ?: return
    val category = uiState.currentCategory ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    if (!uiState.isTwoPane) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(category.icon, contentDescription = category.name, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Divider(Modifier.padding(vertical = 12.dp))
                    Text(
                        text = "Mô tả:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Địa chỉ", tint = MaterialTheme.colorScheme.secondary)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = item.address,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// ====================================================================================
// 5. CẤU TRÚC APP CHÍNH & BỐ CỤC THÍCH ỨNG
// ====================================================================================

@Composable
fun CityGuideApp(viewModel: CityGuideViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    // Lấy chiều rộng màn hình hiện tại
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    // Ngưỡng thích ứng (ví dụ: > 600dp là Two-Pane)
    val isTwoPane = screenWidthDp >= 600.dp

    // Cập nhật trạng thái ViewModel
    LaunchedEffect(isTwoPane) {
        viewModel.setIsTwoPane(isTwoPane)
    }

    if (isTwoPane) {
        TwoPaneLayout(uiState = uiState, viewModel = viewModel)
    } else {
        SinglePaneLayout(navController = navController, uiState = uiState, viewModel = viewModel)
    }
}

/**
 * Bố cục đơn (Màn hình điện thoại): Dùng NavHost truyền thống
 */
@Composable
fun SinglePaneLayout(
    navController: NavController,
    uiState: GuideUiState,
    viewModel: CityGuideViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CategoryList.route
    ) {
        composable(Screen.CategoryList.route) {
            CategoryListScreen(
                uiState = uiState,
                onCategorySelected = viewModel::selectCategory,
                onNavigateToList = { navController.navigate(Screen.RecommendationList.route) }
            )
        }

        composable(Screen.RecommendationList.route) {
            RecommendationListScreen(
                uiState = uiState,
                navController = navController,
                onItemSelected = viewModel::selectItem
            )
        }

        composable(Screen.Detail.route) {
            DetailScreen(
                uiState = uiState,
                navController = navController
            )
        }
    }
}

/**
 * Bố cục hai khung (Màn hình máy tính bảng): Category list cố định bên trái,
 * Recommendation/Detail hiển thị bên phải.
 */
@Composable
fun TwoPaneLayout(uiState: GuideUiState, viewModel: CityGuideViewModel) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Pane 1: Category List (Cố định, chiếm 1/3 màn hình)
        Box(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            // Không cần NavHost, chỉ cần Composable Screen
            CategoryListScreen(
                uiState = uiState,
                onCategorySelected = viewModel::selectCategory,
                onNavigateToList = { /* Không cần điều hướng, đã là two pane */ }
            )
        }

        // Pane 2: Recommendation List và Detail (Chiếm 2/3 màn hình)
        Box(
            modifier = Modifier
                .weight(0.65f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Dùng NavHost cục bộ cho Pane 2 để chuyển đổi giữa List và Detail
            val detailNavController = rememberNavController()

            // Nếu không có mục chi tiết được chọn, hiển thị List
            // Nếu có mục chi tiết được chọn, hiển thị Detail (hoặc mặc định List)
            LaunchedEffect(uiState.selectedCategoryKey) {
                // Luôn chuyển về danh sách khi thay đổi danh mục
                detailNavController.navigate(Screen.RecommendationList.route) {
                    popUpTo(detailNavController.graph.startDestinationId) { inclusive = true }
                }
            }

            NavHost(
                navController = detailNavController,
                startDestination = Screen.RecommendationList.route
            ) {
                composable(Screen.RecommendationList.route) {
                    RecommendationListScreen(
                        uiState = uiState,
                        navController = detailNavController,
                        onItemSelected = { itemId ->
                            viewModel.selectItem(itemId)
                            // Trong two-pane, không cần navigate, chỉ cần cập nhật state
                        }
                    )
                }
                // Trong Two-Pane, DetailScreen sẽ hiển thị dựa trên uiState.selectedItemId
                // thay vì tuyến độc lập. Tuy nhiên, để linh hoạt, ta có thể đặt Detail vào đây
                // để dễ dàng mở rộng. Hiện tại, ta sẽ giữ logic đơn giản: chỉ hiển thị List.
            }

            // Hiển thị Detail Screen trên top nếu có mục được chọn
            if (uiState.currentRecommendation != null) {
                // Dùng Box để Detail đè lên List nếu cần, hoặc đơn giản là thay thế nội dung
                // Ở đây tôi chọn hiển thị Detail thay thế List, để mô phỏng chính xác hơn UDF.
                // Lưu ý: Trong Compose thực tế, người ta thường dùng 3-Pane cho trường hợp này.
                Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
                    TopAppBar(title = { Text(uiState.currentRecommendation.name) })
                    DetailScreen(uiState = uiState, navController = detailNavController)
                }
            }
        }
    }
}

// ====================================================================================
// 6. ACTIVITY
// ====================================================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { // Sử dụng Material Theme mặc định
                CityGuideApp()
            }
        }
    }
}