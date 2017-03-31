package com.intellij.stats.completion


import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.codeInsight.lookup.impl.LookupImpl
import com.intellij.ide.plugins.PluginManager
import com.intellij.ide.util.PropertiesComponent
import com.intellij.lang.Language
import com.intellij.openapi.updateSettings.impl.UpdateChecker
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiUtilCore
import com.intellij.stats.completion.events.*
import com.jetbrains.completion.ranker.CompletionRanker
import sun.plugin.dom.exception.InvalidStateException
import java.util.*


class CompletionFileLoggerProvider(private val logFileManager: LogFileManager) : CompletionLoggerProvider() {
    override fun dispose() {
        logFileManager.dispose()
    }

    private fun String.shortedUUID(): String {
        val start = this.lastIndexOf('-')
        if (start > 0 && start + 1 < this.length) {
            return this.substring(start + 1)
        }
        return this
    }

    override fun newCompletionLogger(): CompletionLogger {
        val installationUID = UpdateChecker.getInstallationUID(PropertiesComponent.getInstance())
        val completionUID = UUID.randomUUID().toString()
        return CompletionFileLogger(installationUID.shortedUUID(), completionUID.shortedUUID(), logFileManager)
    }
}


class CompletionFileLogger(private val installationUID: String,
                           private val completionUID: String,
                           private val logFileManager: LogFileManager) : CompletionLogger() {

    val elementToId = mutableMapOf<String, Int>()

    private fun LookupElement.toIdString(): String {
        val p = LookupElementPresentation()
        this.renderElement(p)
        return "${p.itemText} ${p.tailText} ${p.typeText}"
    }

    private fun registerElement(item: LookupElement): Int {
        val itemString = item.toIdString()
        val newId = elementToId.size
        elementToId[itemString] = newId
        return newId
    }

    private fun getElementId(item: LookupElement): Int? {
        val itemString = item.toIdString()
        return elementToId[itemString]
    }

    private fun logEvent(event: LogEvent) {
        val line = LogEventSerializer.toString(event)
        logFileManager.println(line)
    }

    private fun getRecentlyAddedLookupItems(items: List<LookupElement>): List<LookupElement> {
        val newElements = items.filter { getElementId(it) == null }
        newElements.forEach {
            registerElement(it)
        }
        return newElements
    }

    fun List<LookupElement>.toLookupInfos(lookup: LookupImpl): List<LookupEntryInfo> {
        val relevanceObjects = lookup.getRelevanceObjects(this, false)
        return this.map {
            val id = getElementId(it)!!
            val relevanceMap = relevanceObjects[it]?.map { Pair(it.first, it.second?.toString()) }?.toMap()
            LookupEntryInfo(id, it.lookupString.length, relevanceMap)
        }
    }

    override fun completionStarted(lookup: LookupImpl, isExperimentPerformed: Boolean, experimentVersion: Int) {
        val lookupItems = lookup.items

        lookupItems.forEach { registerElement(it) }
        val relevanceObjects = lookup.getRelevanceObjects(lookupItems, false)

        val lookupEntryInfos = lookupItems.map {
            val id = getElementId(it)!!
            val relevanceMap = relevanceObjects[it]?.map { Pair(it.first, it.second?.toString()) }?.toMap()
            LookupEntryInfo(id, it.lookupString.length, relevanceMap)
        }

        val language = getLanguage(lookup)

        val ideVersion = PluginManager.BUILD_NUMBER ?: "ideVersion"
        val pluginVersion = calcPluginVersion() ?: "pluginVersion"
        val mlRankingVersion = CompletionRanker.rankerVersion

        val event = CompletionStartedEvent(
                ideVersion, pluginVersion, mlRankingVersion,
                installationUID, completionUID,
                language?.displayName,
                isExperimentPerformed, experimentVersion,
                lookupEntryInfos, selectedPosition = 0)
        
        event.isOneLineMode = lookup.editor.isOneLineMode
        logEvent(event)
    }

    private fun calcPluginVersion(): String? {
        val className = CompletionStartedEvent::class.java.name
        val id = PluginManager.getPluginByClassName(className)
        val plugin = PluginManager.getPlugin(id)
        return plugin?.version
    }
    
    private fun getLanguage(lookup: LookupImpl): Language? {
        val editor = lookup.editor
        val offset = editor.caretModel.offset
        val file = PsiDocumentManager.getInstance(lookup.project).getPsiFile(editor.document) ?: return null
        return  PsiUtilCore.getLanguageAtOffset(file, offset)
    }

    override fun customMessage(message: String) {
        val event = CustomMessageEvent(installationUID, completionUID, message)
        logEvent(event)
    }

    override fun afterCharTyped(c: Char, lookup: LookupImpl) {
        val lookupItems = lookup.items
        val newItems = getRecentlyAddedLookupItems(lookupItems).toLookupInfos(lookup)
        
        val ids = lookupItems.map { getElementId(it)!! }
        val currentPosition = lookupItems.indexOf(lookup.currentItem)

        val event = TypeEvent(installationUID, completionUID, ids, newItems, currentPosition)
        logEvent(event)
    }

    override fun downPressed(lookup: LookupImpl) {
        val lookupItems = lookup.items
        
        val newInfos = getRecentlyAddedLookupItems(lookupItems).toLookupInfos(lookup)
        val ids = if (newInfos.isNotEmpty()) lookupItems.map { getElementId(it)!! } else emptyList<Int>()
        val currentPosition = lookupItems.indexOf(lookup.currentItem)

        val event = DownPressedEvent(installationUID, completionUID, ids, newInfos, currentPosition)
        logEvent(event)
    }

    override fun upPressed(lookup: LookupImpl) {
        val lookupItems = lookup.items
        
        val newInfos = getRecentlyAddedLookupItems(lookupItems).toLookupInfos(lookup)
        val ids = if (newInfos.isNotEmpty()) lookupItems.map { getElementId(it)!! } else emptyList<Int>()
        val currentPosition = lookupItems.indexOf(lookup.currentItem)

        val event = UpPressedEvent(installationUID, completionUID, ids, newInfos, currentPosition)
        logEvent(event)
    }

    override fun completionCancelled() {
        val event = CompletionCancelledEvent(installationUID, completionUID)
        logEvent(event)
    }

    override fun itemSelectedByTyping(lookup: LookupImpl) {
        val current = lookup.currentItem ?: throw InvalidStateException("lookup.currentItem is null")
        val id = getElementId(current) ?: throw InvalidStateException("id is null")
        
        val event = TypedSelectEvent(installationUID, completionUID, id)
        logEvent(event)
    }

    override fun itemSelectedCompletionFinished(lookup: LookupImpl) {
        val current = lookup.currentItem ?: throw InvalidStateException("lookup.currentItem is null")
        val index = lookup.items.indexOf(current)
        if (index < 0) {
            throw InvalidStateException("index is $index")
        }
        val id = getElementId(current) ?: throw InvalidStateException("element id is null")
        
        val event = ExplicitSelectEvent(installationUID, completionUID, emptyList(), emptyList(), index, id)
        logEvent(event)
    }
    
    override fun afterBackspacePressed(lookup: LookupImpl) {
        val lookupItems = lookup.items
        
        val newInfos = getRecentlyAddedLookupItems(lookupItems).toLookupInfos(lookup)
        val ids = lookupItems.map { getElementId(it)!! }
        val currentPosition = lookupItems.indexOf(lookup.currentItem)

        val event = BackspaceEvent(installationUID, completionUID, ids, newInfos, currentPosition)
        logEvent(event)
    }

}

enum class Action {
    COMPLETION_STARTED,
    TYPE,
    BACKSPACE,
    UP,
    DOWN,
    COMPLETION_CANCELED,
    EXPLICIT_SELECT,
    TYPED_SELECT,
    CUSTOM
}