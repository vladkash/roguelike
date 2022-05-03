package roguelike.messages

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position3D
import roguelike.extensions.GameEntity
import roguelike.extensions.GameMessage
import roguelike.world.GameContext

data class MoveTo(
    override val context: GameContext,
    override val source: GameEntity<EntityType>,
    val position: Position3D
) : GameMessage
