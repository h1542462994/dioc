package org.tty.dioc.core

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.lifecycle.DefaultScopeFactory
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.base.Builder
import org.tty.dioc.core.basic.ScopeFactory
import org.tty.dioc.core.internal.ComponentDeclareResolver

/**
 * the builder for applicationContext
 * @see [ApplicationContext]
 * @see [ApplicationContextBuilder]
 */
class ApplicationContextBuilder: Builder<ApplicationContext> {
    private val jsonFiles: ArrayList<String> = ArrayList()
    private val scanPackages: ArrayList<PackageOption> = ArrayList()
    private lateinit var serviceDeclareResolver : ComponentDeclareResolver
    private var scopeFactory: ScopeFactory = DefaultScopeFactory()


    override fun create(): ApplicationContext {
        serviceDeclareResolver = ComponentDeclareResolver(
            jsonFiles, scanPackages
        )
        return DefaultApplicationContext(
            ComponentDeclares().apply {
                addAll(serviceDeclareResolver.getDeclarations())
            },
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
     * use package to define the services which annotated [Component].
     */
    fun usePackage(packageName: String, inclusive: Boolean = false): ApplicationContextBuilder {
        scanPackages.add(PackageOption(packageName, inclusive))
        return this
    }

    /**
     * use custom scope factory.
     */
    fun setScopeFactory(scopeFactory: ScopeFactory): ApplicationContextBuilder {
        this.scopeFactory = scopeFactory
        return this
    }


}