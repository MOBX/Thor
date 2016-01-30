package com.mob.thor.rpc.protocol.telnet;

import com.mob.thor.rpc.api.Exporter;
import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.protocol.ThorProtocol;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.telnet.TelnetHandler;
import com.mob.thor.rpc.remoting.api.telnet.support.Help;

@Activate
@Help(parameter = "[service]", summary = "Change default service.", detail = "Change default service.")
public class ChangeTelnetHandler implements TelnetHandler {

    public static final String SERVICE_KEY = "telnet.service";

    public String telnet(ThorChannel channel, String message) {
        if (message == null || message.length() == 0) {
            return "Please input service name, eg: \r\ncd XxxService\r\ncd com.xxx.XxxService";
        }
        StringBuilder buf = new StringBuilder();
        if (message.equals("/") || message.equals("..")) {
            String service = (String) channel.getAttribute(SERVICE_KEY);
            channel.removeAttribute(SERVICE_KEY);
            buf.append("Cancelled default service " + service + ".");
        } else {
            boolean found = false;
            for (Exporter<?> exporter : ThorProtocol.getDubboProtocol().getExporters()) {
                if (message.equals(exporter.getInvoker().getInterface().getSimpleName())
                    || message.equals(exporter.getInvoker().getInterface().getName())
                    || message.equals(exporter.getInvoker().getUrl().getPath())) {
                    found = true;
                    break;
                }
            }
            if (found) {
                channel.setAttribute(SERVICE_KEY, message);
                buf.append("Used the " + message + " as default.\r\nYou can cancel default service by command: cd /");
            } else {
                buf.append("No such service " + message);
            }
        }
        return buf.toString();
    }

}
