package com.revers.rec.others;

import org.junit.Test;
import org.slf4j.Logger;

public class testOther {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(testOther.class);


    @Test
    public void testLog(){

        log.warn("warn...");
        log.debug("debug...");
        log.info("info...");
        log.trace("trace...");
        log.error("error...");
    }
}
