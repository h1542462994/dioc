package org.tty.dioc.util

import java.io.File
import java.io.IOException
import java.net.JarURLConnection
import java.net.URL
import java.net.URLDecoder
import kotlin.jvm.Throws
import kotlin.reflect.KClass

/**
 * Instantiates a new Class scanner.
 * @param basePackageName the root package to scan
 * @param recursive whether scan recursively
 * @param packageNamePredicate the package predicate
 * @param kClassPredicate the class predicate
 */
class KClassScanner
(
    private val basePackageName: String,
    private val recursive: Boolean,
    private val packageNamePredicate: ((String) -> Boolean)?,
    private val kClassPredicate: ((KClass<*>) -> Boolean)?
) {
    /**
     * Do scan all classes set.
     *
     * @return the set
     * @throws IOException      the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun doScanAllClasses(): Set<KClass<*>> {
        val kClassContainer: MutableSet<KClass<*>> = LinkedHashSet()
        var packageName = basePackageName

        // if the last character is '.' then delete it
        if (packageName.endsWith(".")) {
            //packageName = packageName.substring(0, packageName.lastIndexOf('.'))
            packageName = packageName.slice(0 until packageName.lastIndexOf('.'))
        }

        // replace the '.' to '/', mean to scan by file.
        val packageFilePath = packageName.replace('.', '/')
        val resourceURLs = Thread.currentThread().contextClassLoader.getResources(packageFilePath)
        while (resourceURLs.hasMoreElements()) {
            val resourceURL = resourceURLs.nextElement()
            val protocol = resourceURL.protocol
            if ("file" == protocol) {
                val filePath = URLDecoder.decode(resourceURL.file, "UTF-8")
                doScanPackageClassesByFile(packageName, filePath, kClassContainer)
            } else if ("jar" == protocol) {
                doScanPackageClassesByJar(packageName, resourceURL, kClassContainer)
            }
        }
        return kClassContainer
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun doScanPackageClassesByJar(packageName: String, url: URL, kClassContainer: MutableSet<KClass<*>>) {
        // the file path of the package
        val packageFilePath = packageName.replace('.', '/')
        // load the jarFile
        val jar = (url.openConnection() as JarURLConnection).jarFile
        // enumerate the entries
        val entries = jar.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            val name = entry.name
            // 如果路径不一致，或者是目录，则继续
            if (!name.startsWith(packageFilePath) || entry.isDirectory) {
                continue
            }
            // 判断是否递归搜索子包
            if (!recursive && name.lastIndexOf('/') != packageFilePath.length) {
                continue
            }
            if (packageNamePredicate != null) {
                val jarPackageName = name.substring(0, name.lastIndexOf('/')).replace("/", ".")
                if (!packageNamePredicate.invoke(jarPackageName)) {
                    continue
                }
            }

            // 判定是否符合过滤条件
            var className = name.replace('/', '.')
            className = className.substring(0, className.length - 6)
            // 用当前线程的类加载器加载类
            val loadClass = Thread.currentThread().contextClassLoader.loadClass(className).kotlin
            if (kClassPredicate == null || kClassPredicate.invoke(loadClass)) {
                kClassContainer.add(loadClass)
            }
        }
    }

    /**
     * 在文件夹中扫描包和类
     */
    @Throws(ClassNotFoundException::class)
    private fun doScanPackageClassesByFile(packageName: String, packagePath: String, kClassContainer: MutableSet<KClass<*>>) {
        // 转为文件
        val dir = File(packagePath)
        if (!dir.exists() || !dir.isDirectory) {
            return
        }
        // 列出文件，进行过滤
        // 自定义文件过滤规则
        val dirFiles = dir.listFiles { file: File ->
            val filename = file.name
            if (file.isDirectory) {
                if (!recursive) {
                    return@listFiles false
                }
                if (packageNamePredicate != null) {
                    return@listFiles packageNamePredicate.invoke("$packageName.$filename")
                }
                return@listFiles true
            }
            filename.endsWith(".class")
        } ?: return
        for (file in dirFiles) {
            if (file.isDirectory) {
                // 如果是目录，则递归
                doScanPackageClassesByFile(packageName + "." + file.name, file.absolutePath, kClassContainer)
            } else {
                // 用当前类加载器加载 去除 fileName 的 .class 6 位
                val className = file.name.substring(0, file.name.length - 6)
                val loadClass = Thread.currentThread().contextClassLoader.loadClass(
                    "$packageName.$className"
                ).kotlin
                if (kClassPredicate == null || kClassPredicate.invoke(loadClass)) {
                    kClassContainer.add(loadClass)
                }
            }
        }
    }
}
