package com.tokovoynr.battleships.network;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Connector implements Runnable {
    BlockingQueue<Request> queue;

    public Connector(BlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Request request = queue.take();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
