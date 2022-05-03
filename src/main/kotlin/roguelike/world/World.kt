package roguelike.world

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.json.Json
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.internal.TurnBasedEngine
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent
import roguelike.attributes.types.Player
import roguelike.blocks.GameBlock
import roguelike.extensions.GameEntity
import roguelike.extensions.position
import java.io.File

class World(
    startingBlocks: Map<Position3D, GameBlock>,
    size: Size3D
) : GameArea<Tile, GameBlock> by GameAreaBuilder.newBuilder<Tile, GameBlock>()
    .withActualSize(size)
    .withVisibleSize(size)
    .build() {

    private val engine: TurnBasedEngine<GameContext> = Engine.create()

    init {
        startingBlocks.forEach { (pos, block) ->
            setBlockAt(pos, block)
            block.entities.forEach { entity ->
                engine.addEntity(entity)
                entity.position = pos
            }
        }
    }

    private val format = Json { allowStructuredMapKeys = true }

    fun export() {

        val content = format.encodeToString(MapSerializer(Position3DSerializer, GameBlockSerializer), blocks)

        File("map.txt").printWriter().use { out ->
            out.println(content)
        }
    }

    fun import() {
        var content: String
        File("map.txt").reader().use { input ->
            content = input.readText()
        }
        format.decodeFromString(MapSerializer(Position3DSerializer, GameBlockSerializer), content).forEach { (pos, block) ->
            setBlockAt(pos, block)
            block.entities.forEach { entity ->
                engine.addEntity(entity)
                entity.position = pos
            }
        }
    }

    private fun addEntity(entity: Entity<EntityType, GameContext>, position: Position3D) {
        entity.position = position
        engine.addEntity(entity)
        fetchBlockAtOrNull(position)?.addEntity(entity)
    }

    fun addAtEmptyPosition(
        entity: GameEntity<EntityType>,
        offset: Position3D = Position3D.create(0, 0, 0),
        size: Size3D = actualSize
    ): Boolean {
        return findEmptyLocationWithin(offset, size).fold(
            whenEmpty = {
                false
            },
            whenPresent = { location ->
                addEntity(entity, location)
                true
            })
    }

    private fun findEmptyLocationWithin(offset: Position3D, size: Size3D): Maybe<Position3D> {
        var position = Maybe.empty<Position3D>()
        val maxTries = 10
        var currentTry = 0
        while (position.isPresent.not() && currentTry < maxTries) {
            val pos = Position3D.create(
                x = (Math.random() * size.xLength).toInt() + offset.x,
                y = (Math.random() * size.yLength).toInt() + offset.y,
                z = (Math.random() * size.zLength).toInt() + offset.z
            )
            if (fetchBlockAtOrNull(pos)?.isFloor == true) {
                position = Maybe.of(pos)
            }
            currentTry++
        }
        return position
    }

    fun update(screen: Screen, uiEvent: UIEvent, game: Game) {
        engine.executeTurn(
            GameContext(
                world = this,
                screen = screen,
                uiEvent = uiEvent,
                player = game.player
            )
        )
    }

    fun moveEntity(entity: GameEntity<EntityType>, position: Position3D): Boolean {
        var success = false
        val oldBlock = fetchBlockAtOrNull(entity.position)
        val newBlock = fetchBlockAtOrNull(position)

        if (oldBlock != null && newBlock != null && correctPosition(position)) {
            if (entity.type == Player && !newBlock.isWall) {
                success = true
                oldBlock.removeEntity(entity)
                entity.position = position
                newBlock.addEntity(entity)
            }
        }
        return success
    }

    private fun correctPosition(position: Position3D): Boolean {
        return position.x >= 0 && position.x < actualSize.xLength &&
            position.y >= 0 && position.y < actualSize.yLength
    }
}