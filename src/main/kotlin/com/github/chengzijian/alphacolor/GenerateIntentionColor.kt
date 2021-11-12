package com.github.chengzijian.alphacolor

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.extapi.psi.ASTDelegatePsiElement
import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiPlainTextFile
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.psi.util.parents
import com.intellij.psi.xml.*
import kotlin.math.roundToInt

/**
 * @author zijian.cheng
 * @date 2021/11/12
 */
class GenerateIntentionColor : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName(): String {
        return FAMILY_NAME
    }

    override fun getText(): String {
        return NAME
    }

    private var mElementType = 0

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return if (element is XmlToken && element.parents.any { it is XmlTag && it.name == COLOR_TAG && it.parentTag?.name == RES_TAG }) {
            mElementType = 1
            true
        } else {
            when (element.parent) {
                is ASTDelegatePsiElement -> {
                    val value = element.parent.firstChild.text
                    if (value.isValidColor()) {
                        mElementType = 2
                        return true
                    } else if (value.isValidColor2()) {
                        mElementType = 3
                        return true
                    }
                }
                is XmlAttributeValueImpl -> {
                    val value = (element.parent as XmlAttributeValueImpl).value
                    if (value.isValidColor()) {
                        mElementType = 4
                        return true
                    }
                }
            }
            return false
        }
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val percentageString = Messages.showInputDialog(project, PERCENTAGE, NAME, null)
        val percentage = percentageString.asNumeric()
        if (percentage != null) {
            val alpha = "%02x".format((255 * percentage / 100f).roundToInt()).toUpperCase()
            when (mElementType) {
                1 -> {
                    element.parents.first { it is XmlTag && it.name == COLOR_TAG }.also { colorTag ->
                        val modifiedColorTag = (colorTag.copy() as XmlTag).apply {
                            (children.first { it is XmlText && it.value.startsWith("#") } as XmlText).takeIf {
                                it.isValidColor()
                            }?.apply {
                                value = "#${alpha}${getBaseColor()}"
                            }
                        }
                        val attribute = (modifiedColorTag.children.first { it is XmlAttribute && it.name == NAME_ATTR } as XmlAttribute).apply {
                            setValue(getBaseName() + "_$percentage")
                        }
                        val comment = getCommentTag(project, attribute, percentage)
                        val anchor = (colorTag as XmlTag).getAnchor()
                        colorTag.parent.addAfter(comment.copy(), anchor)
                        colorTag.parent.addAfter(modifiedColorTag, anchor)
                    }
                }
                2 -> {
                    element.parent.firstChild.text?.takeIf {
                        it.isValidColor()
                    }?.apply {
                        val value = "#${alpha}${getBaseColor()}"
                        val tempFile = PsiFileFactory.getInstance(project).createFileFromText(
                                PlainTextLanguage.INSTANCE, value) as PsiPlainTextFile
                        element.parent.firstChild.replace(tempFile.firstChild)
                    }
                }
                3 -> {
                    element.parent.firstChild.text?.takeIf {
                        it.isValidColor2()
                    }?.apply {
                        val value = "0X${alpha}${getBaseColor2()}"
                        val tempFile = PsiFileFactory.getInstance(project).createFileFromText(
                                PlainTextLanguage.INSTANCE, value) as PsiPlainTextFile
                        element.parent.firstChild.replace(tempFile.firstChild)
                    }
                }
                4 -> {
                    (element.parent as XmlAttributeValueImpl).value.takeIf {
                        it.isValidColor()
                    }?.apply {
                        val value = "#${alpha}${getBaseColor()}"
                        (element.parent as XmlAttributeValueImpl).updateText("\"$value\"")
                    }
                }
            }
        }
    }

    /***
     * Creates note
     * @see XmlComment tag by creating a temporary memory file.
     * This is the only clean way to generate xml dom entities.
     * */
    private fun getCommentTag(project: Project, attribute: XmlAttribute, percentage: Int): PsiElement {
        val content = "<!--${attribute.getBaseName()} with $percentage% opacity-->"
        val tempFile = PsiFileFactory.getInstance(project).createFileFromText(XMLLanguage.INSTANCE, content) as XmlFile
        return tempFile.children.first().firstChild
    }

    companion object {
        const val NAME = "Generate alpha variant"
        const val FAMILY_NAME = "XML"
        const val COLOR_TAG = "color"
        const val RES_TAG = "resources"
        const val NAME_ATTR = "name"
        const val PERCENTAGE = "Percentage: "
    }
}