package org.tty.dioc.core

import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.core.declare.Service
import org.tty.dioc.core.declare.ServiceDeclareResolver
import org.tty.dioc.core.lifecycle.DefaultScopeFactory
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.util.Builder

/**
 * the builder for applicationContext
 * @see [ApplicationContext]
 * @see [ApplicationContextBuilder]
 */
class ApplicationContextBuilder: Builder<ApplicationContext> {
    private val jsonFiles: ArrayList<String> = ArrayList()
    private val scanPackages: ArrayList<PackageOption> = ArrayList()
    private lateinit var serviceDeclareResolver : ServiceDeclareResolver
    private var scopeFactory: Builder<Scope> = DefaultScopeFactory()


    override fun create(): ApplicationContext {
        serviceDeclareResolver = ServiceDeclareResolver(
            jsonFiles, scanPackages
        )
        return DefaultApplicationContext(
            serviceDeclareResolver.getDeclarations(),
            scopeFactory
        )
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
        return this
    }

    fun setScopeFactory(scopeFactory: Builder<Scope>): ApplicationContextBuilder {
        this.scopeFactory = scopeFactory
        return this
    }


}