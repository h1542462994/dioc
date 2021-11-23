package org.tty.dioc.core.internal

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.core.CoreModule.Companion.rootPackageNameSchema
import org.tty.dioc.core.basic.ComponentDeclareScanner
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.core.basic.EntryPointLoader
import org.tty.dioc.core.declare.PackageOption

internal class EntryPointLoaderImpl(
    private val config: ApplicationConfig,
    private val declares: ComponentDeclares,
    private val declareScanner: ComponentDeclareScanner
): EntryPointLoader {
    override fun onInit() {
        val rootPackName = config[rootPackageNameSchema] as String
        if (rootPackName.isNotEmpty()) {
            declares.addAll(declareScanner.scan(PackageOption(rootPackName, true)))
        }
    }

}