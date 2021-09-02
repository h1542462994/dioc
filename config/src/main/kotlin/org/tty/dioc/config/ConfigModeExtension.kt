package org.tty.dioc.config

import org.tty.dioc.config.keys.ConfigKeys

var ConfigScope.useAnnotation: Boolean
get() {
    return this[ConfigKeys.configModeAnnotation]
}
set(value) {
    this[ConfigKeys.configModeAnnotation] = value
}