package roguelike.view.fragment

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment

class HelpFragment(
    width: Int,
    height: Int,
) : Fragment {

    override val root = Components.vbox()
        .withPreferredSize(width, height)
        .withSpacing(1)
        .build().apply {
            addComponents(
                Components.header().withText("Help"),
                Components.label().withText("wasd - movement"),
                Components.label().withText("z - save map"),
                Components.label().withText("x - load map"),
            )
        }
}