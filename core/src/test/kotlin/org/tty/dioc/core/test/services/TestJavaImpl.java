package org.tty.dioc.core.test.services;

import org.tty.dioc.core.declare.Service;

/**
 * the implementation for the [TestJava]
 */
@Service
public class TestJavaImpl implements TestJava {

    @Override
    public String helloJava() {
        return "helloJava";
    }
}
