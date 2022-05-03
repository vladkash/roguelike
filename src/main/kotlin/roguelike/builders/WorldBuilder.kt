package roguelike.builders

import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import roguelike.blocks.GameBlock
import roguelike.extensions.sameLevelNeighborsShuffled
import roguelike.world.World

class WorldBuilder(private val worldSize: Size3D) {
    companion object {
        private const val SMOOTH_ITERATION = 20
        private const val TUNNELS_FREQUENCY = 3
    }

    private var blocks: MutableMap<Position3D, GameBlock> = mutableMapOf()

    fun makeCaves(): WorldBuilder {
        return randomizeTiles()
            .smooth()
            .addTunnels()

    }

    fun build(): World = World(blocks, worldSize)

    private fun randomizeTiles(): WorldBuilder {
        forAllPositions { pos ->
            blocks[pos] = if (Math.random() < 0.5) {
                GameBlockFactory.floor()
            } else GameBlockFactory.wall()
        }
        return this
    }

    private fun smooth(): WorldBuilder {
        val newBlocks = mutableMapOf<Position3D, GameBlock>()
        repeat(SMOOTH_ITERATION) {
            forAllPositions { pos ->
                var floors = 0
                var rocks = 0
                pos.sameLevelNeighborsShuffled().plus(pos).forEach { neighbor ->
                    blocks.whenPresent(neighbor) { block ->
                        if (block.isFloor) {
                            floors++
                        } else rocks++
                    }
                }
                newBlocks[pos] =
                    if (floors >= rocks) GameBlockFactory.floor() else GameBlockFactory.wall()
            }
            blocks = newBlocks
        }
        return this
    }

    private fun addTunnels(): WorldBuilder {
        forAllPositions { pos ->
            if (
                pos.y % (worldSize.yLength / TUNNELS_FREQUENCY) == 0 ||
                pos.x % (worldSize.xLength / TUNNELS_FREQUENCY) == 0
            ) {
                blocks[pos] = GameBlockFactory.floor()
            }
        }
        return this
    }

    private fun forAllPositions(fn: (Position3D) -> Unit) {
        worldSize.fetchPositions().forEach(fn)
    }

    private fun MutableMap<Position3D, GameBlock>.whenPresent(pos: Position3D, fn: (GameBlock) -> Unit) {
        this[pos]?.let(fn)
    }
}