package ru.bmstu.iu9;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StorageActor extends AbstractActor {

    private List<String> servers = new ArrayList<>();
    private final Random random = new Random();

    private String getRandomServer() {
        ZookeeperApp.print(String.valueOf(servers));
        return this.servers.get(
                random.nextInt(servers.size())
        );
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StorageServer.class,
                        message -> this.servers = message.getServers())
                .match(FollowingServer.class,
                        message -> {
                            this.servers.add(message.getUrl());
                        }
                )
                .match(RandomServer.class,
                        message -> {
                            getSender().tell(
                                    this.getRandomServer(),
                                    ActorRef.noSender()
                            );
                        }
                )
                .build();

    }
}
