package ru.bmstu.iu9;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

public class ZooServer implements Watcher {



    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.toString());
        try {
            sendServers();
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendServers() throws KeeperException, InterruptedException {
        List<String> servers = zoo.getChildren(SERVERS, this);
    }
}
