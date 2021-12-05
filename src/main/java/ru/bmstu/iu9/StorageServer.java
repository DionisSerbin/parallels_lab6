package ru.bmstu.iu9;

import java.util.List;

public class StorageServer {
    private List<String> servers;

    StorageServer(List<String> servers) {
        this.servers = servers;
    }

    public List<String> getServers() {
        return this.servers;
    }
}
