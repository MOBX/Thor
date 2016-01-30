package com.mob.thor.rpc.protocol.telnet;

import java.util.Collection;

import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.utils.StringUtils;
import com.mob.thor.rpc.protocol.ThorProtocol;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeChannel;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeServer;
import com.mob.thor.rpc.remoting.api.telnet.TelnetHandler;
import com.mob.thor.rpc.remoting.api.telnet.support.Help;

@Activate
@Help(parameter = "[-l] [port]", summary = "Print server ports and connections.", detail = "Print server ports and connections.")
public class PortTelnetHandler implements TelnetHandler {

    public String telnet(ThorChannel channel, String message) {
        StringBuilder buf = new StringBuilder();
        String port = null;
        boolean detail = false;
        if (message.length() > 0) {
            String[] parts = message.split("\\s+");
            for (String part : parts) {
                if ("-l".equals(part)) {
                    detail = true;
                } else {
                    if (!StringUtils.isInteger(part)) {
                        return "Illegal port " + part + ", must be integer.";
                    }
                    port = part;
                }
            }
        }
        if (port == null || port.length() == 0) {
            for (ExchangeServer server : ThorProtocol.getDubboProtocol().getServers()) {
                if (buf.length() > 0) {
                    buf.append("\r\n");
                }
                if (detail) {
                    buf.append(server.getUrl().getProtocol() + "://" + server.getUrl().getAddress());
                } else {
                    buf.append(server.getUrl().getPort());
                }
            }
        } else {
            int p = Integer.parseInt(port);
            ExchangeServer server = null;
            for (ExchangeServer s : ThorProtocol.getDubboProtocol().getServers()) {
                if (p == s.getUrl().getPort()) {
                    server = s;
                    break;
                }
            }
            if (server != null) {
                Collection<ExchangeChannel> channels = server.getExchangeChannels();
                for (ExchangeChannel c : channels) {
                    if (buf.length() > 0) {
                        buf.append("\r\n");
                    }
                    if (detail) {
                        buf.append(c.getRemoteAddress() + " -> " + c.getLocalAddress());
                    } else {
                        buf.append(c.getRemoteAddress());
                    }
                }
            } else {
                buf.append("No such port " + port);
            }
        }
        return buf.toString();
    }

}
