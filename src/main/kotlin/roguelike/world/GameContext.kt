package roguelike.world

import org.hexworks.amethyst.api.Context
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent
import roguelike.attributes.types.Player
import roguelike.extensions.GameEntity

data class GameContext(
    val world: World,
    val screen: Screen,
    val uiEvent: UIEvent,
    val player: GameEntity<Player>
) : Context