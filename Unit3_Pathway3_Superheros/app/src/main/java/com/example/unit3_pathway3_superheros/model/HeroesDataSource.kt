package com.example.unit3_pathway3_superheros.model

import com.example.unit3_pathway3_superheros.R
import kotlin.random.Random
import kotlin.math.roundToInt

object HeroesRepository {

    private fun randomPower(): Int {
        return Random.nextInt(50, 101)
    }

    val allHeroes = listOf(
        Hero(R.string.hero1, R.string.description1, R.drawable.android_superhero1, powerLevel = randomPower()),
        Hero(R.string.hero2, R.string.description2, R.drawable.android_superhero2, powerLevel = randomPower()),
        Hero(R.string.hero3, R.string.description3, R.drawable.android_superhero3, powerLevel = randomPower()),
        Hero(R.string.hero4, R.string.description4, R.drawable.android_superhero4, powerLevel = randomPower()),
        Hero(R.string.hero5, R.string.description5, R.drawable.android_superhero5, powerLevel = randomPower()),
        Hero(R.string.hero6, R.string.description6, R.drawable.android_superhero6, powerLevel = randomPower())
    )

    // 1. Sắp xếp các hero theo powerLevel (giảm dần)
    private val sortedHeroes = allHeroes.sortedByDescending { it.powerLevel }

    val teamA: List<Hero>
    val teamB: List<Hero>

    init {
        val tempA = mutableListOf<Hero>()
        val tempB = mutableListOf<Hero>()

        // Phân bổ lần lượt: Hero mạnh nhất vào A, mạnh thứ hai vào B, thứ ba vào B, thứ tư vào A, v.v.
        sortedHeroes.forEachIndexed { index, hero ->
            when (index % 4) {
                0, 3 -> tempA.add(hero)
                1, 2 -> tempB.add(hero)
            }

        }

        teamA = tempA.toList()
        teamB = tempB.toList()
    }
    val teamAPower = teamA.sumOf { it.powerLevel }
    val teamBPower = teamB.sumOf { it.powerLevel }

    val heroes = allHeroes
}