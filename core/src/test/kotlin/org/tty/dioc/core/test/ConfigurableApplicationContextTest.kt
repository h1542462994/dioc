package org.tty.dioc.core.test

import org.junit.jupiter.api.BeforeAll
import org.tty.dioc.ConfigAware
import org.tty.dioc.config.useAnnotation
import org.tty.dioc.core.Application
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.local.runApplication

class TestApplication: Application {
    override fun configure(config: ConfigAware) {
        config.configure {
            useAnnotation = true

        }
    }

}

class ConfigurableApplicationContextTest {

    companion object {
        private lateinit var context: ApplicationContext

        @JvmStatic
        @BeforeAll
        fun initialize() {
             context = runApplication<Application>()
        }
    }
}