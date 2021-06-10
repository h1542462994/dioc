package org.tty.dioc.core.test.services;

import org.tty.dioc.core.declare.Service;

@Service
public class HelloJavaImpl implements HelloJava {

    @Override
    public String helloJava() {
        return "helloJava";
    }
}
