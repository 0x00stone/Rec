package com.revers.rec.controller.net;

import com.revers.rec.controller.Server.Server;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;


@SpringBootTest
public class testServer {

    @Test
    public void testServer() throws ExecutionException, InterruptedException {
       FutureTask<Boolean> futureTask = new FutureTask<>(new Server());
       futureTask.run();
       if(futureTask.get() == false){}
    }
}
