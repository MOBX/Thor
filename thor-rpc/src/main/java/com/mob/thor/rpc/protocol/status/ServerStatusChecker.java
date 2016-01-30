package com.mob.thor.rpc.protocol.status;

import java.util.Collection;

import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.status.Status;
import com.mob.thor.rpc.common.status.StatusChecker;
import com.mob.thor.rpc.protocol.ThorProtocol;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeServer;

@Activate
public class ServerStatusChecker implements StatusChecker {

    public Status check() {
        Collection<ExchangeServer> servers = ThorProtocol.getDubboProtocol().getServers();
        if (servers == null || servers.size() == 0) {
            return new Status(Status.Level.UNKNOWN);
        }
        Status.Level level = Status.Level.OK;
        StringBuilder buf = new StringBuilder();
        for (ExchangeServer server : servers) {
            if (!server.isBound()) {
                level = Status.Level.ERROR;
                buf.setLength(0);
                buf.append(server.getLocalAddress());
                break;
            }
            if (buf.length() > 0) {
                buf.append(",");
            }
            buf.append(server.getLocalAddress());
            buf.append("(clients:");
            buf.append(server.getChannels().size());
            buf.append(")");
        }
        return new Status(level, buf.toString());
    }
}
