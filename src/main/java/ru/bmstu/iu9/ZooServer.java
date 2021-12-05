package ru.bmstu.iu9;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class ZooServer implements Watcher {



    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.toString());
        
    }
}
