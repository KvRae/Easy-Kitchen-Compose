package com.kvrae.easykitchen.utils

fun String.stripMarkdown(): String {
    return this.replace(Regex("""(\*\*|__|\*|_|`)"""), "")
        .replace(Regex("""#+\s+"""), "")
        .replace(Regex("""^[-*+]\s+""", RegexOption.MULTILINE), "• ")
        .trim()
}