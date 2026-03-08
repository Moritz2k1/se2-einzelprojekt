package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult() {

        // Create gameResult
        val gameResult = GameResult(1, "player1", 100, 10.0)

        whenever(mockedService.getGameResult(1)).thenReturn(gameResult)

        // Get game result
        val result = controller.getGameResult(1)

        // Check
        verify(mockedService).getGameResult(1)
        assertEquals(gameResult, result)
    }

    @Test
    fun test_getAllGameResults() {

        // Create gameResults
        val results = listOf(
            GameResult(1, "player1", 100, 10.0),
            GameResult(2, "player2", 80, 15.0),
            GameResult(3, "player3", 110, 20.0)
        )
        whenever(mockedService.getGameResults()).thenReturn(results)

        // Get all game Results
        val allResults = controller.getAllGameResults()

        // Check
        verify(mockedService).getGameResults()
        assertEquals(results, allResults)
    }

    @Test
    fun test_addGameResult() {
        val gameResult = GameResult(1, "player1", 100, 10.0)

        // Add gameResult
        controller.addGameResult(gameResult)

        // Check
        verify(mockedService).addGameResult(gameResult)
    }

    @Test
    fun test_deleteGameResult() {

        // Delete id
        controller.deleteGameResult(1)

        // Check
        verify(mockedService).deleteGameResult(1)
    }
}