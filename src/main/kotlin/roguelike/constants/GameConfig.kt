package roguelike.constants

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size3D

object GameConfig {

    const val DUNGEON_LEVELS = 1

    private val TILESET = CP437TilesetResources.rogueYun16x16()
    val THEME = ColorThemes.ammo()
    const val SIDEBAR_WIDTH = 18

    private const val WINDOW_WIDTH = 80
    const val WINDOW_HEIGHT = 50

    val GAME_AREA_SIZE = Size3D.create(
        xLength = WINDOW_WIDTH - SIDEBAR_WIDTH,
        yLength = WINDOW_HEIGHT,
        zLength = DUNGEON_LEVELS
    )

    fun buildAppConfig() = AppConfig.newBuilder()
        .withDefaultTileset(TILESET)
        .withSize(WINDOW_WIDTH, WINDOW_HEIGHT)
        .build()
}