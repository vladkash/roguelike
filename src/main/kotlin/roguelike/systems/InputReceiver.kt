package roguelike.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import roguelike.extensions.position
import roguelike.messages.MoveTo
import roguelike.world.GameContext

object InputReceiver : BaseBehavior<GameContext>() {

    override suspend fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        val (_, _, uiEvent, player) = context
        context.world
        val currentPos = player.position
        if (uiEvent is KeyboardEvent) {
            if (uiEvent.code == KeyCode.KEY_Z) {
                context.world.export()
            }
            if (uiEvent.code == KeyCode.KEY_X) {
                context.world.import()
            }

            val newPosition = when (uiEvent.code) {
                KeyCode.KEY_W -> currentPos.withRelativeY(-1)
                KeyCode.KEY_A -> currentPos.withRelativeX(-1)
                KeyCode.KEY_S -> currentPos.withRelativeY(1)
                KeyCode.KEY_D -> currentPos.withRelativeX(1)
                else -> {
                    currentPos
                }
            }
            player.receiveMessage(MoveTo(context, player, newPosition))
        }
        return true
    }
}