package org.tty.dioc.core

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.module.Module
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.config.schema.providerSchema
import org.tty.dioc.core.basic.ComponentResolver
import org.tty.dioc.core.basic.EntryPointLoader
import org.tty.dioc.core.basic.ScopeAbility
import org.tty.dioc.core.basic.ScopeFactory
import org.tty.dioc.core.declare.ComponentDeclaresImpl
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.core.internal.ComponentResolverImpl
import org.tty.dioc.core.internal.EntryPointLoaderImpl
import org.tty.dioc.core.lifecycle.DefaultScopeFactory
import org.tty.dioc.core.lifecycle.StackScopeTrace
import org.tty.dioc.util.Logger
import org.tty.dioc.util.SimpleConsoleLogger

@InternalComponent
class CoreModule(
    private val configSchemas: ConfigSchemas
): Module {
    override fun onInit() {
        configSchemas.config(scopeSchema)
        configSchemas.config(scopeAbilitySchema)
        configSchemas.config(componentDeclaresSchema)
        configSchemas.config(componentResolverSchema)
        configSchemas.config(loggerSchema)
        configSchemas.config(entryPointLoaderSchemas)
    }

    companion object {
        private const val scopeProvider = "org.tty.dioc.core.scope.provider"
        private const val scopeAbilityProvider = "org.tty.dioc.core.scopeAbility.provider"
        private const val componentDeclares = "org.tty.dioc.core.declares"
        private const val componentResolver = "org.tty.dioc.core.resolver"
        private const val logger = "org.tty.dioc.logger"
        private const val entryPointLoader = "org.tty.dioc.core.entryPointLoader"
        val scopeSchema = providerSchema<ScopeFactory>(scopeProvider, listOf(DefaultScopeFactory::class))
        val scopeAbilitySchema = providerSchema<ScopeAbility>(scopeAbilityProvider, listOf(StackScopeTrace::class))
        val componentDeclaresSchema = providerSchema<ComponentDeclares>(componentDeclares, listOf(ComponentDeclaresImpl::class))
        val componentResolverSchema = providerSchema<ComponentResolver>(componentResolver, listOf(ComponentResolverImpl::class))
        val loggerSchema = providerSchema<Logger>(logger, listOf(SimpleConsoleLogger::class), ConfigRule.Mutable)
        val entryPointLoaderSchemas = providerSchema<EntryPointLoader>(entryPointLoader, listOf(EntryPointLoaderImpl::class))
    }
}