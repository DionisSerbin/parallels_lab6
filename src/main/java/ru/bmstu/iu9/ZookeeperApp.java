package ru.bmstu.iu9;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.log4j.BasicConfigurator;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.*;

public class ZookeeperApp {
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";
    final private static String ZOO_HOST = "127.0.0.1:2181";
    final private static int TIME_OUT = 2500;
    final private static String LOCAL_HOST = "localhost";
    final private static String PORT = "8000";
    final private static String URL = "url";
    final private static String COUNT = "count";
    final private static int TIME_OUT_SEC = 5;
    private static final int INDEX_OF_ZOOKEEPER_ADDRESS = 0;
    private static final int ZOOKEEPER_TIMEOUT = 3000;
    private static final int NO_SERVERS_RUNNING = 0;
    private static final String ERROR = "NO SERVERS ARE RUNNING";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        BasicConfigurator.configure();
        ActorSystem system = ActorSystem.create("routes");
        ActorRef storage = system.actorOf(Props.create(StorageActor.class));
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Http http = Http.get(system);
        ZooKeeper zk = null;

        try {
            zk = new ZooKeeper(args[INDEX_OF_ZOOKEEPER_ADDRESS], ZOOKEEPER_TIMEOUT, null);
            new ZooWatcher(zk, storage);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        List<CompletionStage<ServerBinding>> bindings = new ArrayList<>();

        StringBuilder serversInfo = new StringBuilder("Servers online at\n");

        for (int i = 1; i < args.length; i++) {
            try {
                StorageServer server = new StorageServer(http, storage, zk, args[i]);
                final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = server.createRoute().flow(system, materializer);
                bindings.add(http.bindAndHandle(
                        routeFlow,
                        ConnectHttp.toHost(LOCAL_HOST, Integer.parseInt(args[i])),
                        materializer
                ));
                serversInfo.append("http://localhost:").append(args[i]).append("/\n");
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
        }

        if(bindings.size() == NO_SERVERS_RUNNING) {
            System.err.println(ERROR);
        }

        print(serversInfo + "\nPress RETURN to stop...");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        for (CompletionStage<ServerBinding> binding : bindings) {
            binding
                    .thenCompose(ServerBinding::unbind)
                    .thenAccept(unbound -> system.terminate());
        }

    }


    public static void print(String s) {
        System.out.println(GREEN + s + RESET);
    }

}
