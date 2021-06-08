package org.tty.dioc.core.util

import java.lang.reflect.Constructor

@Deprecated("you should call constructor.create(*args) instead.")
fun Constructor<*>.create(args: List<Any>): Any {
    return when {
        args.isEmpty() -> {
            this.newInstance();
        }
        args.size == 1 -> {
            this.newInstance(args[0])
        }
        args.size == 2 -> {
            this.newInstance(args[0], args[1])
        }
        args.size == 3 -> {
            this.newInstance(args[0], args[1], args[2])
        }
        args.size == 4 -> {
            this.newInstance(args[0], args[1], args[2], args[3])
        }
        args.size == 5 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4])
        }
        args.size == 6 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5])
        }
        args.size == 7 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
        }
        args.size == 8 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
        }
        args.size == 9 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])
        }
        args.size == 10 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])
        }
        args.size == 11 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10])
        }
        args.size == 12 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11])
        }
        args.size == 13 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12])
        }
        args.size == 14 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13])
        }
        args.size == 15 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14])
        }
        args.size == 16 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15])
        }
        else -> throw IllegalStateException("too many arguments.")
    }
}
