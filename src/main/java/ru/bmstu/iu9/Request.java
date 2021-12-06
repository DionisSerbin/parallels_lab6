package ru.bmstu.iu9;

public class Request {

    private String url;
    private int count;

    public Request(String url, String count) {
        this.url = url;
        this.count = Integer.parseInt(count);
    }
}
