package org.tty.dioc.config

import org.tty.dioc.config.internal.ApplicationConfigDelegate

var ApplicationConfig.useAnnotation by ApplicationConfigDelegate<Boolean>(configModeAnnotationSchema)

var ApplicationConfig.useFile by ApplicationConfigDelegate<Boolean>(configModeFileSchema)