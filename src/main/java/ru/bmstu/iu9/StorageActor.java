package ru.bmstu.iu9;

import akka.actor.AbstractActor;

import java.util.List;
import java.util.Random;

public class StorageActor extends AbstractActor {

    private List<String> servers;
    private Random random = new Random();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StorageServer.class,
                        message -> this.servers = message.getServers())
                .match()
    }
}
