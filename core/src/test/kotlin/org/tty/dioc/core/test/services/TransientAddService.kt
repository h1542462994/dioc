package org.tty.dioc.core.test.services

interface TransientAddService {
    fun add()
    fun current(): Int
}