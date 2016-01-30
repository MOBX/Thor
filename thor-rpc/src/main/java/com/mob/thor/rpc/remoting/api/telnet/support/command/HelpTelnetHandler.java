package com.mob.thor.rpc.remoting.api.telnet.support.command;

import java.util.ArrayList;
import java.util.List;

import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.extension.ExtensionLoader;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.telnet.TelnetHandler;
import com.mob.thor.rpc.remoting.api.telnet.support.Help;
import com.mob.thor.rpc.remoting.api.telnet.support.TelnetUtils;

@Activate
@Help(parameter = "[command]", summary = "Show help.", detail = "Show help.")
public class HelpTelnetHandler implements TelnetHandler {

    private final ExtensionLoader<TelnetHandler> extensionLoader = ExtensionLoader.getExtensionLoader(TelnetHandler.class);

    public String telnet(ThorChannel channel, String message) {
        if (message.length() > 0) {
            if (!extensionLoader.hasExtension(message)) {
                return "No such command " + message;
            }
            TelnetHandler handler = extensionLoader.getExtension(message);
            Help help = handler.getClass().getAnnotation(Help.class);
            StringBuilder buf = new StringBuilder();
            buf.append("Command:\r\n    ");
            buf.append(message + " " + help.parameter().replace("\r\n", " ").replace("\n", " "));
            buf.append("\r\nSummary:\r\n    ");
            buf.append(help.summary().replace("\r\n", " ").replace("\n", " "));
            buf.append("\r\nDetail:\r\n    ");
            buf.append(help.detail().replace("\r\n", "    \r\n").replace("\n", "    \n"));
            return buf.toString();
        } else {
            List<List<String>> table = new ArrayList<List<String>>();
            List<TelnetHandler> handlers = extensionLoader.getActivateExtension(channel.getUrl(), "telnet");
            if (handlers != null && handlers.size() > 0) {
                for (TelnetHandler handler : handlers) {
                    Help help = handler.getClass().getAnnotation(Help.class);
                    List<String> row = new ArrayList<String>();
                    String parameter = " " + extensionLoader.getExtensionName(handler) + " "
                                       + (help != null ? help.parameter().replace("\r\n", " ").replace("\n", " ") : "");
                    row.add(parameter.length() > 50 ? parameter.substring(0, 50) + "..." : parameter);
                    String summary = help != null ? help.summary().replace("\r\n", " ").replace("\n", " ") : "";
                    row.add(summary.length() > 50 ? summary.substring(0, 50) + "..." : summary);
                    table.add(row);
                }
            }
            return "Please input \"help [command]\" show detail.\r\n" + TelnetUtils.toList(table);
        }
    }

}
