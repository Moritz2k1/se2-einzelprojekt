package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    /*
     GameResults get sorted based on the score and then based on the time now
     Minus sign in front of score means descending order
     */
    @GetMapping
    fun getLeaderboard(): List<GameResult> =
        gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, {it.timeInSeconds}))

}