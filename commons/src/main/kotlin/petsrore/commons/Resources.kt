package petsrore.commons

import java.io.File
import java.io.Reader
import java.net.URL
import java.util.Optional

inline fun <reified T> resourcesOf(resource: String): Optional<URL> =
    when {
        resource.startsWith("classpath:") ->
            T::class.java.getResource(resource.substringAfter("classpath:"))

        resource.startsWith("file:") ->
            File(resource.substringAfter("file:")).takeIf { it.exists() }?.toURI()?.toURL()

        else ->
            T::class.java.getResource(resource)
    }.optional()


data class ScriptBlock(
    val title: String,
    val content: String,
    val atLine: Int,
) {
    enum class ProcessControl {
        CONTINUE,
        ABORT,
        DONE
    }
}

fun Reader.scriptBlocks(
    blockDelim: String = "---",
    processScriptBlock: ScriptBlock.() -> ScriptBlock.ProcessControl,
): ScriptBlock.ProcessControl {
    var currentState: ScriptBlock.ProcessControl = ScriptBlock.ProcessControl.DONE
    useLines { lines ->
        sequence<ScriptBlock> {
            var blockIndex = 0
            var blockContent: StringBuilder = StringBuilder()
            var blockTitle: String = ""
            val r = lines.iterator()
            var blockPending = false
            var lineNo = 0

            fun nextBlock() = ScriptBlock(
                title = blockTitle,
                content = blockContent.toString(),
                atLine = lineNo,
            )

            fun pushLastBlock() {
                if (blockContent.isEmpty()) return
                if (!blockPending) return
                if (currentState != ScriptBlock.ProcessControl.CONTINUE) return
                currentState = processScriptBlock(nextBlock())
            }

            fun processCurrentBlock(): ScriptBlock.ProcessControl {
                return if (blockContent.isEmpty()) {
                    currentState
                } else {
                    blockPending = false
                    processScriptBlock(nextBlock())
                }
            }

            fun startBlock(title: String) {
                if (blockContent.isNotEmpty()) blockContent = StringBuilder()
                blockTitle = title
                blockIndex += 1
                blockPending = true
            }

            fun amendBlock(line: String) {
                blockContent.appendLine(line)
            }

            while (true) {
                val line = r.nextOrNull() ?: break
                ++lineNo
                if (line.startsWith(blockDelim)) {
                    currentState = processCurrentBlock()
                    currentState.takeIf { it == ScriptBlock.ProcessControl.CONTINUE } ?: break
                    startBlock(line)
                } else {
                    amendBlock(line)
                }
            }

            pushLastBlock()
        }
    }
    return when (val lastState = currentState) {
        ScriptBlock.ProcessControl.CONTINUE -> ScriptBlock.ProcessControl.DONE
        else -> lastState
    }
}

private fun <T> Iterator<T>.nextOrNull(): T? =
    when {
        hasNext() -> next()
        else -> null
    }