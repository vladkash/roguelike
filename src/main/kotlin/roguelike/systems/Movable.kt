package roguelike.systems

import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import roguelike.messages.MoveTo
import roguelike.world.GameContext

object Movable : BaseFacet<GameContext, MoveTo>(MoveTo::class) {

    override suspend fun receive(message: MoveTo): Response {
        val (context, entity, position) = message
        val world = context.world
        var result: Response = Pass
        if (world.moveEntity(entity, position)) {
            result = Consumed
        }

        return result
    }
}