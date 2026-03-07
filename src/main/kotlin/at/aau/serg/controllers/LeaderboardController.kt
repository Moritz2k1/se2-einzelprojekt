package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.max
import kotlin.math.min

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
    fun getLeaderboard(
        @RequestParam(required = false) rank: Int?
    ): ResponseEntity<List<GameResult>> {
        val sorted = gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, {it.timeInSeconds}))

        // If rank is not given, just return sorted Leaderboard
        if (rank == null) {
            return ResponseEntity.ok(sorted)
        }

        // If rank is out of scope, return 400
        if (rank <= 0 || rank > sorted.size) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

        // Rank 1 player is at position 0
        val index = rank - 1

        // Get the 3 players before wanted player
        // max function prevents negative value
        val start = max(0, index - 3)

        // Get the 3 players after wanted player
        // +4 because .sublist includes start index, excludes end index
        val end = min(sorted.size, index + 4)

        val subList = sorted.subList(start, end)
        return ResponseEntity.ok(subList)
    }


}