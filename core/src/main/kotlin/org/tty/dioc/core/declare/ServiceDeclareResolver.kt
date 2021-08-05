package org.tty.dioc.core.declare

import org.tty.dioc.core.advice.InterfaceAdvice
import org.tty.dioc.core.util.ServiceUtil.hasServiceAnnotation
import org.tty.dioc.util.KClassScanner
import kotlin.reflect.full.hasAnnotation

class ServiceDeclareResolver(
    private val jsonFiles: ArrayList<String> = ArrayList(),
    private val scanPackages: ArrayList<PackageOption> = ArrayList()
) {


    /**
     * get the declaration of the service.
     */
    fun getDeclarations(): List<ServiceDeclare> {
        val basic = getBasicDeclarations()
        return basic.plus(getAdviceDeclarations(basic))
    }

    /**
     * get the advice declarations
     */
    private fun getAdviceDeclarations(serviceDeclares: List<ServiceDeclare>): List<ServiceDeclare> {
        val advice = HashSet<ServiceDeclare>()
        serviceDeclares.forEach { serviceDeclare ->
            serviceDeclare.components.forEach { propertyComponent ->
                if (propertyComponent.declareType.hasAnnotation<InterfaceAdvice>()) {
                    advice.add(ServiceDeclare.fromDeclareAdvice(propertyComponent.declareType))
                }
            }
        }
        return advice.toList()
    }

    private fun getBasicDeclarations(): List<ServiceDeclare> {
        val declarations = ArrayList<ServiceDeclare>()
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
    private fun getDeclarationsFromJsonFile(fileName: String): List<ServiceDeclare> {
        TODO("not implemented.")
    }


    /**
     * get the declaration of the service from the json file.
     */
    private fun getDeclarationsFromPackage(packageOption: PackageOption): List<ServiceDeclare> {
        val (name, inclusive) = packageOption
        val kClassScanner = KClassScanner(name, inclusive, { true }, { true })
        val kClasses = kClassScanner.doScanAllClasses()
        return kClasses.filter {
            it.hasServiceAnnotation
        }.map {
            ServiceDeclare.fromType(it)
        }
    }
}