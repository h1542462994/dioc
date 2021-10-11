package org.tty.dioc.core.internal

import org.tty.dioc.core.basic.ComponentDeclareResolver
import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.util.ServiceUtil.hasComponentAnnotation
import org.tty.dioc.reflect.KClassScanner

class ComponentDeclareResolver(
    private val jsonFiles: ArrayList<String> = ArrayList(),
    private val scanPackages: ArrayList<PackageOption> = ArrayList()
): ComponentDeclareResolver {


    /**
     * get the declaration of the service.
     */
    override fun getDeclarations(): List<ComponentDeclare> {
        return getBasicDeclarations()
    }


    private fun getBasicDeclarations(): List<ComponentDeclare> {
        val declarations = ArrayList<ComponentDeclare>()
        jsonFiles.forEach {
            declarations.addAll(getDeclarationsFromJsonFile(it))
        }
        scanPackages.forEach {
            declarations.addAll(getDeclarationsFromPackage(it))
        }
        // constructor the serviceElements to serviceDeclarations
        return declarations
    }

    /**
     * get the declaration of the service from the json file.
     */
    private fun getDeclarationsFromJsonFile(fileName: String): List<ComponentDeclare> {
        TODO("not implemented.")
    }

    /**
     * get the declaration of the service from the json file.
     */
    private fun getDeclarationsFromPackage(packageOption: PackageOption): List<ComponentDeclare> {
        val (name, inclusive) = packageOption
        val kClassScanner = KClassScanner(name, inclusive, { true }, { true })
        val kClasses = kClassScanner.doScanAllClasses()
        return kClasses.filter {
            it.hasComponentAnnotation
        }.map {
            ComponentDeclare.fromType(it)
        }
    }
}