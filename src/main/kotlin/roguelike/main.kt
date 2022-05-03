package roguelike

import org.hexworks.zircon.api.SwingApplications
import roguelike.constants.GameConfig
import roguelike.view.PlayView

fun main() {
    val grid = SwingApplications.startTileGrid(GameConfig.buildAppConfig())
    PlayView(grid).dock()
}
