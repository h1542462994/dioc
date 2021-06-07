package org.tty.dioc.core

import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.core.declare.ServiceElement
import org.tty.dioc.core.declare.ServiceDeclarations
import org.tty.dioc.core.util.ClassScanner
import org.tty.dioc.core.util.ServiceUtil

/**
 * the builder for applicationContext
 * @see [ApplicationContext]
 * @see [ApplicationContextBuilder]
 */
class ApplicationContextBuilder: Builder<ApplicationContext> {
    private val jsonFiles: ArrayList<String> = ArrayList()
    private val scanPackages: ArrayList<PackageOption> = ArrayList()

    override fun create(): ApplicationContext {
        return DefaultApplicationContext(getDeclarations())
    }

    /**
     * use jsonFile to define the services.
     */
    fun useJsonFile(fileName: String = "serviceDeclare.json"): ApplicationContextBuilder {
        jsonFiles.add(fileName)
        return this
    }

    fun usePackage(packageName: String, inclusive: Boolean = false): ApplicationContextBuilder {
        scanPackages.add(PackageOption(packageName, inclusive))
        return this;
    }

    /**
     * get the declaration of the service.
     */
    public fun getDeclarations(): ServiceDeclarations {
        val declarations = ArrayList<ServiceElement>()
        jsonFiles.forEach {
            declarations.addAll(getDeclarationsFromJsonFile(it))
        }
        scanPackages.forEach {
            declarations.addAll(getDeclarationsFromPackage(it))
        }
        // constructor the serviceElements to serviceDeclarations
        return ServiceDeclarations.fromServiceElements(declarations)
    }

    /**
     * get the declaration of the service from the json file.
     */
    private fun getDeclarationsFromJsonFile(fileName: String): List<ServiceElement> {
        TODO("not implemented.")
    }


    /**
     * get the declaration of the service from the json file.
     */
    private fun getDeclarationsFromPackage(packageOption: PackageOption): List<ServiceElement> {
        val (name, inclusive) = packageOption
        val classScanner = ClassScanner(name, inclusive, { true }, { true })
        val classes = classScanner.doScanAllClasses()
        return classes.filter {
            ServiceUtil.detectService(it.kotlin)
        }.map {
            ServiceElement.fromType(it.kotlin)
        }
    }


}