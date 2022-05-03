package roguelike.extensions

import org.hexworks.amethyst.api.Message
import roguelike.world.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.entity.Entity

typealias GameMessage = Message<GameContext>

typealias AnyGameEntity = GameEntity<EntityType>

typealias GameEntity<T> = Entity<T, GameContext>