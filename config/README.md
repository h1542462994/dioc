# dioc.config

## dependencies

1. project(:util)

## 

```kotlin
/**
 * config support
 */


interface ApplicationConfig {
    fun <T: Any> getConfig(name: String): T
    fun <T: Any> getAnouymos(type: KClass<*>): T 
}

class NotProvidedException() {
    
}

class BasicApplicationConfigSupport : ApplicationConfig {

}

class FileApplicationConfigSupport : ApplicationConfig {
    
}

class AnnotationApplicationConfigSupport : ApplicationConfig {
    
}

class RuntimeApplicationConfigSupport : ApplicationConfig {
    
}

fun main() {
    
    
    
}

```

扩展优先级

默认值(0) -> 文件(1) -> 注解(2) -> 运行时(3)
- 使用注解时，默认的名称为包名+key，如果没有key，则该项配置为匿名。



```kotlin
package a.b.c

annotation class Config(
    val key: String = ""
)

@Config(key = "printer")
class PrinterLocation {
    /**
     * 对应的配置键为a.b.c.printer.location
     */
    val location: String = "printer123"
}
```



schema类型：
1. 仅使用默认值，不可更改。
2. 仅使用默认值和运行时，可以更改。
3. 使用文件、注解等，可以更改

```kotlin
enum class ConfigRule {
    Declare,
    Runtime,
    RuntimeMutable,
    Soft,
    SoftMutable,
}
```

