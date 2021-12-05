package ru.bmstu.iu9;

import akka.actor.AbstractActor;

import java.util.List;

public class StorageActor extends AbstractActor {

    private List<String> servers;

    @Override
    public Receive createReceive() {
        return null;
    }
}
