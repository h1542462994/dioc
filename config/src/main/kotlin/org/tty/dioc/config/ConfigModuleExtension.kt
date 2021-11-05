package org.tty.dioc.config

import org.tty.dioc.config.schema.delegateForSchema

var ApplicationConfig.useAnnotation by delegateForSchema<Boolean>(ConfigModule.configModeAnnotationSchema)
var ApplicationConfig.useFile by delegateForSchema<Boolean>(ConfigModule.configModeFileSchema)