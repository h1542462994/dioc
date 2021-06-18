package org.tty.dioc.core

import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.core.declare.Service
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.ServiceDeclareResolver
import org.tty.dioc.core.util.ServiceUtil.hasServiceAnnotation
import org.tty.dioc.util.Builder
import org.tty.dioc.util.KClassScanner

/**
 * the builder for applicationContext
 * @see [ApplicationContext]
 * @see [ApplicationContextBuilder]
 */
class ApplicationContextBuilder: Builder<ApplicationContext> {
    private val jsonFiles: ArrayList<String> = ArrayList()
    private val scanPackages: ArrayList<PackageOption> = ArrayList()
    private val serviceDeclareResolver = ServiceDeclareResolver()

    override fun create(): ApplicationContext {
        return DefaultApplicationContext(serviceDeclareResolver.getDeclarations())
    }

    /**
     * use jsonFile to define the services.
     */
    fun useJsonFile(fileName: String = "serviceDeclare.json"): ApplicationContextBuilder {
        jsonFiles.add(fileName)
        return this
    }

    /**
     * use package to define the services which annotated [Service].
     */
    fun usePackage(packageName: String, inclusive: Boolean = false): ApplicationContextBuilder {
        scanPackages.add(PackageOption(packageName, inclusive))
        return this;
    }

}