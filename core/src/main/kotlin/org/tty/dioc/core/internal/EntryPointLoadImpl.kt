package org.tty.dioc.core.internal

import org.tty.dioc.core.ApplicationEntryPoint
import org.tty.dioc.core.basic.EntryPointLoader
import org.tty.dioc.core.declare.ComponentDeclares
import org.tty.dioc.core.declare.MutableComponentDeclares
import org.tty.dioc.core.declare.PackageOption

class EntryPointLoaderImpl(
    private val entryPoint: ApplicationEntryPoint,
    val componentDeclares: ComponentDeclares,
): EntryPointLoader {
    override fun onInit() {
        val rootPackName = entryPoint.javaClass.packageName
        (componentDeclares as MutableComponentDeclares).addAll(
            ComponentDeclareResolver(
                arrayListOf(), arrayListOf(
                    PackageOption(name = rootPackName, true)
                )
            ).getDeclarations()
        )
    }

}