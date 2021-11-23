# dioc.config

## dependencies

1. project(:util)

### code

```
import kotlin.reflect.KClass

/**
 * config support
 */

interface ApplicationConfig {
    fun <T : Any> getConfig(name: String): T
    fun <T : Any> getAnonymous(type: KClass<*>): T
}

class NotProvidedException : Exception()

class ApplicationConfigDeclareSupport : ApplicationConfig

class FileApplicationConfigSupport : ApplicationConfig

class AnnotationApplicationConfigSupport : ApplicationConfig

class ApplicationConfigRuntimeSupport : ApplicationConfig

fun main() {

}

```

### 扩展优先级

默认值(0) -> 文件(1) -> 注解(2) -> 运行时(3)
- 使用注解时，默认的名称为包名+key，如果没有key，则该项配置为匿名。



```
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



### schema类型：
1. 仅使用默认值，不可更改。
2. 仅使用默认值和运行时，可以更改。
3. 使用文件、注解等，可以更改

```
enum class ConfigRule {
    NoAssigned,
    Declare,
    CodeReadOnly,
    CodeMutable,
    ReadOnly,
    Mutable,
}
```

```
.provider = [
    "StringList",
    "TextBuff",
    "Recoginized"
]

.provider = "text"
```

component定义。

```xml
<component>
    <constuctor signature="2/*,signature">
        <param index="0" ref="service"/>
        <param index="1" value="testSignature"/>
    </constuctor>
</component>
```

```json

```
