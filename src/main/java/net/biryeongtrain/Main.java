package net.biryeongtrain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger("test");
        logger.info("test2");
        logger.info(MarkerFactory.getMarker("test"), "test");
        System.out.println("Hello world!");
    }
}