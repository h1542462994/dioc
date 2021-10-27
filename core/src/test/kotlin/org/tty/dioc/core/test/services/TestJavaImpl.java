package org.tty.dioc.core.test.services;

import org.tty.dioc.annotation.Component;

/**
 * the implementation for the [TestJava]
 */
@Component
public class TestJavaImpl implements TestJava {

    @Override
    public String helloJava() {
        return "helloJava";
    }
}
