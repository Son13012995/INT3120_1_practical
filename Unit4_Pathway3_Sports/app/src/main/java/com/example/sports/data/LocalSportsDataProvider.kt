package com.example.sports.data

import com.example.sports.R
import com.example.sports.model.Sport

/**
 * Sports data provider
 */
object LocalSportsDataProvider {
    // Sử dụng lazy để đảm bảo getSportsData() chỉ chạy khi cần thiết
    val firstAvailableSport: Sport by lazy { getSportsData().first() }

    // Sử dụng Map để tổ chức dữ liệu, key là ID của Sport
    private val sportDataMap: Map<Int, Sport> = getRawData().mapValues { (id, data) ->
        Sport(
            id = id,
            titleResourceId = data.first,
            subtitleResourceId = R.string.sports_list_subtitle,
            playerCount = data.second.first,
            olympic = data.second.second,
            imageResourceId = data.third.first,
            sportsImageBanner = data.third.second,
            sportDetails = R.string.sport_detail_text
        )
    }

    /**
     * Dữ liệu thô: Map<ID, Triple<TitleResId, Pair<PlayerCount, isOlympic>, Pair<SquareResId, BannerResId>>>
     * Sử dụng cấu trúc phức tạp hơn để tránh trùng lặp.
     */
    private fun getRawData(): Map<Int, Triple<Int, Pair<Int, Boolean>, Pair<Int, Int>>> = mapOf(
        1 to Triple(R.string.baseball, Pair(9, true), Pair(R.drawable.ic_baseball_square, R.drawable.ic_baseball_banner)),
        2 to Triple(R.string.badminton, Pair(1, true), Pair(R.drawable.ic_badminton_square, R.drawable.ic_badminton_banner)),
        3 to Triple(R.string.basketball, Pair(5, true), Pair(R.drawable.ic_basketball_square, R.drawable.ic_basketball_banner)),
        4 to Triple(R.string.bowling, Pair(1, false), Pair(R.drawable.ic_bowling_square, R.drawable.ic_bowling_banner)),
        5 to Triple(R.string.cycling, Pair(1, true), Pair(R.drawable.ic_cycling_square, R.drawable.ic_cycling_banner)),
        6 to Triple(R.string.golf, Pair(1, false), Pair(R.drawable.ic_golf_square, R.drawable.ic_golf_banner)),
        7 to Triple(R.string.running, Pair(1, true), Pair(R.drawable.ic_running_square, R.drawable.ic_running_banner)),
        8 to Triple(R.string.soccer, Pair(11, true), Pair(R.drawable.ic_soccer_square, R.drawable.ic_soccer_banner)),
        9 to Triple(R.string.swimming, Pair(1, true), Pair(R.drawable.ic_swimming_square, R.drawable.ic_swimming_banner)),
        10 to Triple(R.string.table_tennis, Pair(1, true), Pair(R.drawable.ic_table_tennis_square, R.drawable.ic_table_tennis_banner)),
        11 to Triple(R.string.tennis, Pair(1, true), Pair(R.drawable.ic_tennis_square, R.drawable.ic_tennis_banner))
    )

    /**
     * Trả về một List các Sport từ Map nội bộ.
     */
    fun getSportsData(): List<Sport> {
        return sportDataMap.values.toList()
    }

    /**
     * Hàm tiện ích để lấy Sport theo ID
     */
    fun getSportById(id: Int): Sport? {
        return sportDataMap[id]
    }
}