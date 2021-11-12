package com.github.chengzijian.alphacolor

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlComment
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlText


fun String?.asNumeric(): Int? {
    if (!isNullOrBlank()) {
        val num = toIntOrNull()
        if (num != null) {
            return when (num) {
                in 0..100 -> num
                in Int.MIN_VALUE..0 -> 0
                else -> 100
            }
        }
    }
    return null

}

fun XmlText.isValidColor(): Boolean {
    return value.trim().run {
        length == 7 || length == 9
    }
}

fun String.isValidColor(): Boolean {
    return trim().takeIf { startsWith("#") }.run {
        length == 7 || length == 9
    }
}

fun String.isValidColor2(): Boolean {
    return trim().takeIf { startsWith("0x") }.run {
        length == 10
    }
}

fun XmlText.getBaseColor(): String {
    return value.trim().run {
        substring(if (length == 7) 1 else 3)
    }
}

fun String.getBaseColor(): String {
    return trim().run {
        substring(if (length == 7) 1 else 3)
    }
}

fun String.getBaseColor2(): String {
    return trim().run {
        substring(4)
    }
}

fun XmlAttribute.getBaseName(): String {
    return if (value.isNullOrEmpty()) {
        ""
    } else {
        value!!.run {
            val index = lastIndexOf("_")
            if (index > -1) {
                val opacity = substring(index + 1, length).toIntOrNull() ?: -1
                if (opacity in 0..100) {
                    return substring(0, index)
                }
            }
            return value.orEmpty()
        }
    }
}

fun XmlTag.getAnchor(): PsiElement {
    var next: PsiElement = this
    while (next.nextSibling is XmlComment && next.nextSibling != null) {
        next = next.nextSibling
    }
    return next
}
