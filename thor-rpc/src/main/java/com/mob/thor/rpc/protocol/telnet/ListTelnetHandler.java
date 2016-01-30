package com.mob.thor.rpc.protocol.telnet;

import java.lang.reflect.Method;

import com.mob.thor.rpc.api.Exporter;
import com.mob.thor.rpc.api.Invoker;
import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.utils.ReflectUtils;
import com.mob.thor.rpc.protocol.ThorProtocol;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.telnet.TelnetHandler;
import com.mob.thor.rpc.remoting.api.telnet.support.Help;

@Activate
@Help(parameter = "[-l] [service]", summary = "List services and methods.", detail = "List services and methods.")
public class ListTelnetHandler implements TelnetHandler {

    public String telnet(ThorChannel channel, String message) {
        StringBuilder buf = new StringBuilder();
        String service = null;
        boolean detail = false;
        if (message.length() > 0) {
            String[] parts = message.split("\\s+");
            for (String part : parts) {
                if ("-l".equals(part)) {
                    detail = true;
                } else {
                    if (service != null && service.length() > 0) {
                        return "Invaild parameter " + part;
                    }
                    service = part;
                }
            }
        } else {
            service = (String) channel.getAttribute(ChangeTelnetHandler.SERVICE_KEY);
            if (service != null && service.length() > 0) {
                buf.append("Use default service " + service + ".\r\n");
            }
        }
        if (service == null || service.length() == 0) {
            for (Exporter<?> exporter : ThorProtocol.getDubboProtocol().getExporters()) {
                if (buf.length() > 0) {
                    buf.append("\r\n");
                }
                buf.append(exporter.getInvoker().getInterface().getName());
                if (detail) {
                    buf.append(" -> ");
                    buf.append(exporter.getInvoker().getUrl());
                }
            }
        } else {
            Invoker<?> invoker = null;
            for (Exporter<?> exporter : ThorProtocol.getDubboProtocol().getExporters()) {
                if (service.equals(exporter.getInvoker().getInterface().getSimpleName())
                    || service.equals(exporter.getInvoker().getInterface().getName())
                    || service.equals(exporter.getInvoker().getUrl().getPath())) {
                    invoker = exporter.getInvoker();
                    break;
                }
            }
            if (invoker != null) {
                Method[] methods = invoker.getInterface().getMethods();
                for (Method method : methods) {
                    if (buf.length() > 0) {
                        buf.append("\r\n");
                    }
                    if (detail) {
                        buf.append(ReflectUtils.getName(method));
                    } else {
                        buf.append(method.getName());
                    }
                }
            } else {
                buf.append("No such service " + service);
            }
        }
        return buf.toString();
    }

}
