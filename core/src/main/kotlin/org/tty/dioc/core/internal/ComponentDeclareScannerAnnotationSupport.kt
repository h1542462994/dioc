package org.tty.dioc.core.internal

import org.tty.dioc.core.basic.ComponentDeclareScanner
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.core.util.ServiceUtil.isComponent
import org.tty.dioc.reflect.KClassScanner

internal class ComponentDeclareScannerAnnotationSupport : ComponentDeclareScanner {

    /**
     * get the declaration of the service from the package
     */
    override fun scan(packageOption: PackageOption): List<ComponentDeclare> {
        val (name, inclusive) = packageOption
        val kClassScanner = KClassScanner(name, inclusive, { true }, { true })
        val kClasses = kClassScanner.doScanAllClasses()
        return kClasses.filter {
            it.isComponent
        }.map {
            ComponentDeclare.fromType(it)
        }.distinct()
    }

    /**
     * get the declaration of the service from the package
     */
    override fun scanAll(packageOptions: List<PackageOption>): List<ComponentDeclare> {
        return packageOptions.map {
            scan(it)
        }.flatten().distinct()
    }
}