package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertNotNull
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val response = controller.getLeaderboard(null)
        val res: List<GameResult>? = response.body

        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(res)
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectIdSorting() {
        // Changed the values so that the first one is actually the fastest one
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val response = controller.getLeaderboard(null)
        val res: List<GameResult>? = response.body

        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(res)
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_invalidRank_returnsBadRequest() {
        whenever(mockedService.getGameResults()).thenReturn(
            listOf(GameResult(1, "p1", 10, 10.0))
        )

        val getRankLow = controller.getLeaderboard(0)
        assertEquals(HttpStatus.BAD_REQUEST, getRankLow.statusCode)

        val getRankHigh = controller.getLeaderboard(2)
        assertEquals(HttpStatus.BAD_REQUEST, getRankHigh.statusCode)
    }

    @Test
    fun test_getLeaderboard_validRank_middle() {

        // Create leaderboard
        val p1 = GameResult(1, "Player 1", 100, 10.0)
        val p2 = GameResult(2, "Player 2", 90, 10.0)
        val p3 = GameResult(3, "Player 3", 80, 10.0)
        val p4 = GameResult(4, "Player 4", 70, 10.0)
        val p5 = GameResult(5, "Player 5", 60, 10.0)
        val p6 = GameResult(6, "Player 6", 50, 10.0)
        val p7 = GameResult(7, "Player 7", 40, 10.0)
        val p8 = GameResult(8, "Player 8", 30, 10.0)
        val p9 = GameResult(9, "Player 9", 20, 10.0)
        val p10 = GameResult(10, "Player 10", 10, 10.0)

        // Put players into a list
        val players = listOf(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
        whenever(mockedService.getGameResults()).thenReturn(players)

        // Get leaderboard
        val response = controller.getLeaderboard(5)
        val res = response.body

        // Check
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(res)
        assertEquals(7, res.size)
        assertEquals("Player 2", res[0].playerName)
        assertEquals("Player 8", res[6].playerName)
    }

    @Test
    fun test_getLeaderboard_validRank_startClipped() {

        // Create leaderboard
        val p1 = GameResult(1, "Player 1", 100, 10.0)
        val p2 = GameResult(2, "Player 2", 90, 10.0)
        val p3 = GameResult(3, "Player 3", 80, 10.0)
        val p4 = GameResult(4, "Player 4", 70, 10.0)
        val p5 = GameResult(5, "Player 5", 60, 10.0)

        // Put players into a list
        val players = listOf(p1, p2, p3, p4, p5)
        whenever(mockedService.getGameResults()).thenReturn(players)

        // Get leaderboard
        val response = controller.getLeaderboard(2)
        val res = response.body

        // Check
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(res)
        assertEquals(5, res.size)
        assertEquals(p1, res[0])
        assertEquals(p5, res[4])
    }

    @Test
    fun test_getLeaderboard_validRank_endClipped() {

        // Create leaderboard
        val p1 = GameResult(1, "Player 1", 100, 10.0)
        val p2 = GameResult(2, "Player 2", 90, 10.0)
        val p3 = GameResult(3, "Player 3", 80, 10.0)
        val p4 = GameResult(4, "Player 4", 70, 10.0)
        val p5 = GameResult(5, "Player 5", 60, 10.0)

        // Put players into a list
        val players = listOf(p1, p2, p3, p4, p5)
        whenever(mockedService.getGameResults()).thenReturn(players)

        // Get leaderboard
        val response = controller.getLeaderboard(4)
        val res = response.body

        // Check
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(res)
        assertEquals(5, res.size)
        assertEquals(p5, res[4])
    }
}