package com.example.simple_todo_app.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

object ListUtils {
    private const val INDENT = "  "

    /**
     * Handles automatic list continuation, detection, and exit conditions.
     */
    fun handleListLogic(
        oldValue: TextFieldValue,
        newValue: TextFieldValue
    ): TextFieldValue {
        val cursorPosition = newValue.selection.start
        
        // Case 1: Initial Detection (e.g., "- " -> "  - ")
        if (newValue.text.length == oldValue.text.length + 1) {
            val textBeforeCursor = newValue.text.substring(0, cursorPosition)
            val lineStart = textBeforeCursor.lastIndexOf('\n') + 1
            val currentLine = textBeforeCursor.substring(lineStart)

            val detectionRegex = """^([-*•]|\d+\.)\s$""".toRegex()
            if (detectionRegex.matches(currentLine)) {
                val indentedPrefix = INDENT + currentLine
                val newText = newValue.text.substring(0, lineStart) + 
                             indentedPrefix + 
                             newValue.text.substring(cursorPosition)
                return TextFieldValue(newText, TextRange(lineStart + indentedPrefix.length))
            }
        }

        // Only trigger continuation/exit when text length increased by 1 (presumably a newline)
        if (newValue.text.length != oldValue.text.length + 1) return newValue
        if (cursorPosition == 0) return newValue

        val addedChar = newValue.text[cursorPosition - 1]
        if (addedChar != '\n') return newValue

        // Get the text before the newly added newline
        val textBeforeNewline = oldValue.text.substring(0, oldValue.selection.start)
        val lines = textBeforeNewline.split("\n")
        val lastLine = lines.last()

        // Regex for indented bullet points and numbered lists
        val bulletRegex = """^(\s*)([-*•])\s(.*)$""".toRegex()
        val numberRegex = """^(\s*)(\d+)\.\s(.*)$""".toRegex()

        val bulletMatch = bulletRegex.find(lastLine)
        if (bulletMatch != null) {
            val indent = bulletMatch.groupValues[1]
            val bulletSymbol = bulletMatch.groupValues[2]
            val content = bulletMatch.groupValues[3]

            return if (content.isBlank()) {
                // Exit condition
                val startOfLine = textBeforeNewline.length - lastLine.length
                val newText = oldValue.text.substring(0, startOfLine) + 
                             "\n" + 
                             oldValue.text.substring(oldValue.selection.start)
                TextFieldValue(newText, TextRange(startOfLine + 1))
            } else {
                // Continuation
                val prefix = "$indent$bulletSymbol "
                val newText = newValue.text.substring(0, cursorPosition) + 
                             prefix + 
                             newValue.text.substring(cursorPosition)
                TextFieldValue(newText, TextRange(cursorPosition + prefix.length))
            }
        }

        val numberMatch = numberRegex.find(lastLine)
        if (numberMatch != null) {
            val indent = numberMatch.groupValues[1]
            val currentNumberString = numberMatch.groupValues[2]
            val currentNumber = currentNumberString.toIntOrNull() ?: return newValue
            val content = numberMatch.groupValues[3]

            return if (content.isBlank()) {
                // Exit condition
                val startOfLine = textBeforeNewline.length - lastLine.length
                val newText = oldValue.text.substring(0, startOfLine) + 
                             "\n" + 
                             oldValue.text.substring(oldValue.selection.start)
                TextFieldValue(newText, TextRange(startOfLine + 1))
            } else {
                // Continuation
                val nextPrefix = "$indent${currentNumber + 1}. "
                val newText = newValue.text.substring(0, cursorPosition) + 
                             nextPrefix + 
                             newValue.text.substring(cursorPosition)
                TextFieldValue(newText, TextRange(cursorPosition + nextPrefix.length))
            }
        }

        return newValue
    }

    /**
     * Converts a plain string into an AnnotatedString with bolded list prefixes.
     */
    fun formatAsList(text: String): AnnotatedString {
        return buildAnnotatedString {
            val lines = text.split("\n")
            lines.forEachIndexed { index, line ->
                val prefixRegex = """^(\s*)([-*•]|\d+\.)\s""".toRegex()
                val match = prefixRegex.find(line)
                if (match != null) {
                    val prefix = match.value
                    append(
                        AnnotatedString(
                            prefix,
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        )
                    )
                    append(line.substring(prefix.length))
                } else {
                    append(line)
                }
                if (index < lines.size - 1) {
                    append("\n")
                }
            }
        }
    }

    /**
     * A VisualTransformation that bolds list prefixes (bullets and numbers).
     */
    val ListVisualTransformation = VisualTransformation { text ->
        TransformedText(formatAsList(text.text), OffsetMapping.Identity)
    }
}
