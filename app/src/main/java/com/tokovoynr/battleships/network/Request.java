package com.tokovoynr.battleships.network;

class Request {
    Result result;
    String query;

    Request(Result result, String query) {
        this.result = result;
        this.query = query;
    }
}
