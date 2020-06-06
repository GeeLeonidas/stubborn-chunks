package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.text.LiteralText

class WDialogBox(
    private val bimoe: Bimoe,
    private val moddedPlayer: StubbornPlayer
): WPlainPanel() {

    private var currentEntry = ""
    private var lastTime = System.nanoTime() / 1_000_000L

    private val dialogText: WDialogText

    init {
        val dialogSizeX = 160
        val dialogSizeY = 50
        val textOffsetY = 14
        val textLength = moddedPlayer.getBimoeTextLength(bimoe)

        setSize(dialogSizeX, dialogSizeY)
        backgroundPainter = BackgroundPainter.VANILLA

        add(WDialogLabel(bimoe) { nextDialog() }, 0, 0)

        // TODO: Implement load entry function
        currentEntry = "This is a template message, there's nothing to read here. Seriously. Why are you still reading?..."

        dialogText = WDialogText(currentEntry.take(textLength)) { nextDialog() }
        add(dialogText, 0, textOffsetY, dialogSizeX, dialogSizeY - textOffsetY)
    }

    // TODO: Rework this to implement better break lines
    override fun tick() {
        super.tick()

        val currentTime = System.nanoTime() / 1_000_000L
        val delta = currentTime - lastTime
        var textLength = moddedPlayer.getBimoeTextLength(bimoe)

        if (delta > 10L && textLength < currentEntry.length) {
            if (currentEntry[textLength] == '§' && textLength + 1 < currentEntry.length)
                textLength++

            textLength++
            moddedPlayer.setBimoeTextLength(bimoe, textLength)
            dialogText.text = LiteralText(currentEntry.take(textLength))

            lastTime = currentTime
        }
    }

    private fun nextDialog() {
        val textLength = moddedPlayer.getBimoeTextLength(bimoe)
        if (textLength == currentEntry.length) {
            moddedPlayer.setBimoeTextLength(bimoe, 0)
            // TODO: Implement load entry function
        }
        else {
            moddedPlayer.setBimoeTextLength(bimoe, currentEntry.length)
            dialogText.text = LiteralText(currentEntry)
        }
    }

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0)
            nextDialog()
        return super.onMouseUp(x, y, button)
    }
}