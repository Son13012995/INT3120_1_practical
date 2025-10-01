package com.example.unit3_pathway3_woof


import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.unit3_pathway3_woof.ui.theme.WoofTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.unit3_pathway3_woof.data.dogs


@RunWith(AndroidJUnit4::class)
class WoofUITests {


    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    /**
     * Kiểm tra xem TopAppBar có hiển thị tiêu đề ứng dụng (từ R.string.app_name) hay không.
     */
    @Test
    fun topAppBar_displaysAppName() {
        composeTestRule.setContent {
            WoofTheme {
                WoofTopAppBar()
            }
        }
        // Giả sử R.string.app_name = "Woof"
        composeTestRule.onNodeWithText("Woof")
            .assertExists("Top App Bar phải hiển thị tiêu đề 'Woof'")
    }

    //-------------------------------------------------------------
    // VỊ TRÍ ĐÚNG CHO TEST THỨ HAI
    //-------------------------------------------------------------

    /**
     * Kiểm tra chức năng mở rộng/thu gọn của DogItem.
     * 1. Nhấn nút mở rộng.
     * 2. Kiểm tra xem DogHobby (About section) có được hiển thị hay không.
     * 3. Nhấn lại nút mở rộng.
     * 4. Kiểm tra xem DogHobby có biến mất (không tồn tại) hay không.
     */
    @Test
    fun dogItem_expandsAndCollapses() {
        // Hiển thị một DogItem cụ thể để kiểm thử
        composeTestRule.setContent {
            WoofTheme {
                // Thêm import cho dogs nếu cần, hoặc giả định rằng nó đã có sẵn.
                // Nếu DogItem(dog = dogs[0]) vẫn báo lỗi 'dogs' chưa được định nghĩa,
                // bạn cần thêm: import com.example.unit3_pathway3_woof.data.dogs
                DogItem(dog = dogs[0])
            }
        }

        // Lấy mô tả nội dung của nút Mở rộng/Thu gọn
        val expandButtonDescription =
            composeTestRule.activity.getString(R.string.expand_button_content_description)

        // 1. Nhấn nút mở rộng
        composeTestRule.onNodeWithContentDescription(expandButtonDescription) // <-- Tìm bằng ContentDescription là tốt hơn cho icon
            .performClick()

        // 2. Kiểm tra DogHobby (R.string.about) có hiển thị sau khi mở rộng
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.about)
        ).assertExists("DogHobby phải hiển thị sau khi mở rộng.")

        // 3. Nhấn lại để thu gọn
        composeTestRule.onNodeWithContentDescription(expandButtonDescription)
            .performClick()

        // 4. Kiểm tra DogHobby có biến mất (không tồn tại) sau khi thu gọn
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.about)
        ).assertDoesNotExist()
    }
} // <--- CLASS ĐƯỢC ĐÓNG ĐÚNG VỊ TRÍ