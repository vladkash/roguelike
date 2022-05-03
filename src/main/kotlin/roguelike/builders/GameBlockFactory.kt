package roguelike.builders

import roguelike.blocks.GameBlock

object GameBlockFactory {

    fun floor() = GameBlock(GameTileRepository.FLOOR)

    fun wall() = GameBlock(GameTileRepository.WALL)

}