package org.hvdw.jexiftoolgui.controllers;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public enum SingletonEnum {
    INSTANCE;
    private final static Logger logger = (Logger) LoggerFactory.getLogger(SingletonEnum.class);

    private SingletonEnum() {
        System.out.println("Singleton call");
    }
}

