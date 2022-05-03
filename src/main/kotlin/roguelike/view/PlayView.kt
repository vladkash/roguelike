package roguelike.view

import org.hexworks.cobalt.databinding.api.extension.toProperty
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ColorTheme
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.view.base.BaseView
import org.hexworks.zircon.internal.game.impl.GameAreaComponentRenderer
import roguelike.builders.GameBuilder
import roguelike.builders.GameTileRepository
import roguelike.constants.GameConfig
import roguelike.view.fragment.HelpFragment
import roguelike.world.Game

class PlayView(
    grid: TileGrid,
    private val game: Game = GameBuilder.create(),
    theme: ColorTheme = GameConfig.THEME
) : BaseView(grid, theme) {
    init {
        val sidebar = Components.panel()
            .withPreferredSize(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT)
            .withDecorations(box())
            .build()

        sidebar.addFragment(
            HelpFragment(
                width = sidebar.contentSize.width,
                height = sidebar.contentSize.height
            )
        )

        val gameComponent = Components.panel()
            .withPreferredSize(game.world.visibleSize.to2DSize())
            .withComponentRenderer(
                GameAreaComponentRenderer(
                    gameArea = game.world,
                    projectionMode = ProjectionMode.TOP_DOWN.toProperty(),
                    fillerTile = GameTileRepository.FLOOR
                )
            )
            .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
            .build()

        screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
            game.world.update(screen, event, game)
            Processed
        }

        screen.addComponents(sidebar, gameComponent)
    }
}