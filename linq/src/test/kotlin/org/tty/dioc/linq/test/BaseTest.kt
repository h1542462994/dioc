package org.tty.dioc.linq.test

import org.junit.jupiter.api.Test
import org.tty.dioc.linq.Linq
import org.tty.dioc.linq.extension.*
import org.tty.dioc.reflect.virtual.Virtual

class BaseTest {
    fun testFrom() {
        val i = Linq.start<Int>()
        val t = from(i) of 1..9 where { i > 0 && i < 8 } select i

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

