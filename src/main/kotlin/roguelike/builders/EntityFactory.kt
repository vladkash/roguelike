package roguelike.builders

import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.newEntityOfType
import roguelike.attributes.EntityPosition
import roguelike.attributes.EntityTile
import roguelike.attributes.types.Player
import roguelike.systems.InputReceiver
import roguelike.systems.Movable
import roguelike.world.GameContext

fun <T : EntityType> newGameEntityOfType(
    type: T,
    init: EntityBuilder<T, GameContext>.() -> Unit
) = newEntityOfType(type, init)

object EntityFactory {

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(EntityPosition(), EntityTile(GameTileRepository.PLAYER))
        behaviors(InputReceiver)
        facets(Movable)
    }
}