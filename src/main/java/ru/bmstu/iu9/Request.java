package ru.bmstu.iu9;

public class Request extends org.omg.CORBA.Request {

    private String url;
    private int count;

    public Request(String url, String count) {
        this.url = url;
        this.count = Integer.parseInt(count);
    }

    public String getUrl() {
        return url;
    }

    public int getCount() {
        return count;
    }

    public void countMinus() {
        this.count--;
    }
}
