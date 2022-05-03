package roguelike.builders

import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import roguelike.attributes.types.Player
import roguelike.constants.GameConfig
import roguelike.extensions.GameEntity
import roguelike.world.Game

class GameBuilder(val worldSize: Size3D) {

    val world = WorldBuilder(worldSize)
        .makeCaves()
        .build()

    fun buildGame(): Game {

        prepareWorld()

        val player = addPlayer()

        return Game.create(
            player = player,
            world = world
        )
    }

    private fun prepareWorld() = also {
        world.scrollUpBy(world.actualSize.zLength)
    }

    private fun addPlayer(): GameEntity<Player> {
        val player = EntityFactory.newPlayer()
        world.addAtEmptyPosition(
            player,
            offset = Position3D.create(0, 0, GameConfig.DUNGEON_LEVELS - 1),
            size = world.visibleSize.copy(zLength = 0)
        )
        return player
    }

    companion object {
        fun create() = GameBuilder(
            worldSize = GameConfig.GAME_AREA_SIZE
        ).buildGame()
    }
}