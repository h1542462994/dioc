package org.tty.dioc.core.internal

import org.tty.dioc.core.ApplicationEntryPoint
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.core.basic.EntryPointLoader
import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.reflect.packageName

class EntryPointLoaderImpl(
    private val entryPoint: ApplicationEntryPoint,
    val componentDeclares: ComponentDeclares,
): EntryPointLoader {
    override fun onInit() {
        val rootPackName = entryPoint::class.packageName
        componentDeclares.addAll(
            ComponentDeclareResolver(
                arrayListOf(), arrayListOf(
                    PackageOption(name = rootPackName, true)
                )
            ).getDeclarations()
        )
    }

}