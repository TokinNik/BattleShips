package com.tokovoynr.battleships.network;

import android.provider.ContactsContract;

import com.tokovoynr.battleships.Profile;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class QueryQueue {
    private final String ADD_TEMPLATE;
    private final String DELETE_TEMPLATE;
    private final String SELECT_TEMPLATE;
    private final String UPDATE_TEMPLATE;

    private AtomicInteger messageId = new AtomicInteger(0);
    private BlockingQueue<Request> queue;
    private Profile profile;


    public QueryQueue(BlockingQueue<Request> queue, Profile profile) {
        this.queue = queue;
        this.profile = profile;

        ADD_TEMPLATE =       "{method: add, params: [user_id: " + profile.getId() + "], reit: %d}";
        DELETE_TEMPLATE = "{method: delete, params: [user_id: " + profile.getId() + "],reit: %d}";
        SELECT_TEMPLATE = "{method: select, params: [user_id: " + profile.getId() + ", target_id: %d], reit: %d}";
        UPDATE_TEMPLATE = "{method: update, params: [user_id: " + profile.getId() + "],reit: %d}";
    }

    public Result add() {
        return put(ADD_TEMPLATE);
    }

    public Result delete() {
        return put(DELETE_TEMPLATE);
    }

    public Result select(int opponentId) {
        Result result = new Result();
        try {
            queue.put(new Request(result, String.format(SELECT_TEMPLATE, opponentId, messageId.incrementAndGet())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Result update() {
        return put(UPDATE_TEMPLATE);
    }

    private Result put(String query) {
        Result result = new Result();
        try {
            queue.put(new Request(result, String.format(query, messageId.incrementAndGet())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
