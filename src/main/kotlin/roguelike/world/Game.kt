package roguelike.world

import roguelike.attributes.types.Player
import roguelike.extensions.GameEntity

class Game(
    val world: World,
    val player: GameEntity<Player>
) {

    companion object {

        fun create(
            player: GameEntity<Player>,
            world: World
        ) = Game(
            world = world,
            player = player
        )
    }
}