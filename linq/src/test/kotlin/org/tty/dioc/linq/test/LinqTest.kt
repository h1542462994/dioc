package org.tty.dioc.linq.test

import org.junit.jupiter.api.Test
import org.tty.dioc.linq.Linq
import org.tty.dioc.linq.extension.*
import org.tty.dioc.reflect.virtual.Virtual

class LinqTest {
    @Test
    fun testBasic() {
        /** linq的基础语法支持
         */

        /**
         * 创建一个linq entry
         */
        val i = Linq.start<Int>()
        val j = Linq.start<Int>()

        /**
         * from(i) 绑定entry
         * of 0 . 9 绑定数据源
         */
        val result = from(i) of 0..9 where { false } select i

        val result2 = from(i) of 0..9 from(j) of 1..10
    }

    @Test
    fun testMemberAccess() {
        val i = Linq.start<Student>()
        val students = mockQueryable<Student>()
        val t = from(i) of students where { false }
        val student = Student()


    }

    class Student {
        var id: String = ""
        var name: String = ""
    }

    val Virtual<Student>.id : Virtual<String>
    get() {
        return this[Student::id]
    }

}

