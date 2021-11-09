package org.tty.dioc.core

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.module.Module
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.config.schema.providerSchema
import org.tty.dioc.core.basic.ComponentResolver
import org.tty.dioc.core.basic.ScopeAbility
import org.tty.dioc.core.basic.ScopeFactory
import org.tty.dioc.core.declare.ComponentDeclares
import org.tty.dioc.core.declare.ReadonlyComponentDeclares
import org.tty.dioc.core.internal.ComponentResolverImpl
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
    }

    companion object {
        private const val scopeProvider = "org.tty.dioc.core.scope.provider"
        private const val scopeAbilityProvider = "org.tty.dioc.core.scopeAbility.provider"
        private const val componentDeclares = "org.tty.dioc.core.declares"
        private const val componentResolver = "org.tty.dioc.core.resolver"
        private const val logger = "org.tty.dioc.logger"
        val scopeSchema = providerSchema<ScopeFactory>(scopeProvider, listOf(DefaultScopeFactory::class))
        val scopeAbilitySchema = providerSchema<ScopeAbility>(scopeAbilityProvider, listOf(StackScopeTrace::class))
        val componentDeclaresSchema = providerSchema<ReadonlyComponentDeclares>(componentDeclares, listOf(ComponentDeclares::class))
        val componentResolverSchema = providerSchema<ComponentResolver>(componentResolver, listOf(ComponentResolverImpl::class))
        val loggerSchema = providerSchema<Logger>(logger, listOf(SimpleConsoleLogger::class), ConfigRule.Mutable)
    }



}