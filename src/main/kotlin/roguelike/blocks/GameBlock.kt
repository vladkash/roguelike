package roguelike.blocks

import kotlinx.collections.immutable.persistentMapOf
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.BlockTileType
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock
import roguelike.builders.GameTileRepository
import roguelike.builders.GameTileRepository.PLAYER
import roguelike.extensions.GameEntity
import roguelike.extensions.tile

class GameBlock(
    private var defaultTile: Tile = GameTileRepository.FLOOR,
    private val currentEntities: MutableList<GameEntity<EntityType>> = mutableListOf(),
) : BaseBlock<Tile>(
    emptyTile = Tile.empty(),
    tiles = persistentMapOf(BlockTileType.CONTENT to defaultTile)
) {
    val isFloor: Boolean
        get() = defaultTile == GameTileRepository.FLOOR

    val isWall: Boolean
        get() = defaultTile == GameTileRepository.WALL

    val entities: List<GameEntity<EntityType>>
        get() = currentEntities.toList()

    fun addEntity(entity: GameEntity<EntityType>) {
        currentEntities.add(entity)
        updateContent()
    }

    fun removeEntity(entity: GameEntity<EntityType>) {
        currentEntities.remove(entity)
        updateContent()
    }

    private fun updateContent() {
        val entityTiles = currentEntities.map { it.tile }
        content = when {
            entityTiles.contains(PLAYER) -> PLAYER
            entityTiles.isNotEmpty() -> entityTiles.first()
            else -> defaultTile
        }
    }
}