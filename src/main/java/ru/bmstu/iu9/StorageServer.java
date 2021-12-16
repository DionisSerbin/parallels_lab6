package ru.bmstu.iu9;


import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

public class StorageServer implements Watcher {
    private List<String> servers;

    StorageServer(List<String> servers) {
        this.servers = servers;
    }

    public List<String> getServers() {
        return this.servers;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
