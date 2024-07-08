import com.ionspin.kotlin.bignum.decimal.times
import kotlinx.serialization.Serializable
import util.Gelds
import util.GeldsSerializer
import util.gelds
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class GameState(
    @Serializable(with = GeldsSerializer::class)
    internal val stashedMoney: Gelds,
    val workers: List<GameWorker>,
    val availableJobs: List<GameJob> = listOf(
        GameJob(1, Level(1, 10.gelds, 1.gelds, 1.seconds)),
        GameJob(2, Level(1, 50.gelds, 10.gelds, 5.seconds)),
        GameJob(3, Level(1, 250.gelds, 15.gelds, 10.seconds)),
        GameJob(4, Level(1, 750.gelds, 50.gelds, 15.seconds)),

    ),
)

@Serializable
data class GameWorker(
    val jobId: Int,
    val createdAt: Long,
) {

    fun earnedWorker(job: GameJob, now: Long): Pair<Long, Gelds> {
        val collected = abs((now - createdAt) / job.level.duration.inWholeMilliseconds)
        return collected to collected * job.level.earn
    }
}

@Serializable
data class GameJob(
    val id: Int,
    val level: Level,
)

@Serializable
data class Level(
    val level: Int,
    @Serializable(with = GeldsSerializer::class)
    val cost: Gelds,
    @Serializable(with = GeldsSerializer::class)
    val earn: Gelds,
    val duration: Duration,
) {
    fun upgradeEfficiency() = copy(
        level = level + 1,
        earn = earn * 2,
        cost = cost * 3
    )
}
