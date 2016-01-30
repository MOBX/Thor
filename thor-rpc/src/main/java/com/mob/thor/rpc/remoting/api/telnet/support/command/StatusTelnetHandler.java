package com.mob.thor.rpc.remoting.api.telnet.support.command;

import java.util.*;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.extension.ExtensionLoader;
import com.mob.thor.rpc.common.status.Status;
import com.mob.thor.rpc.common.status.StatusChecker;
import com.mob.thor.rpc.common.status.support.StatusUtils;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.telnet.TelnetHandler;
import com.mob.thor.rpc.remoting.api.telnet.support.Help;
import com.mob.thor.rpc.remoting.api.telnet.support.TelnetUtils;

@Activate
@Help(parameter = "[-l]", summary = "Show status.", detail = "Show status.")
public class StatusTelnetHandler implements TelnetHandler {

    private final ExtensionLoader<StatusChecker> extensionLoader = ExtensionLoader.getExtensionLoader(StatusChecker.class);

    public String telnet(ThorChannel channel, String message) {
        if (message.equals("-l")) {
            List<StatusChecker> checkers = extensionLoader.getActivateExtension(channel.getUrl(), "status");
            String[] header = new String[] { "resource", "status", "message" };
            List<List<String>> table = new ArrayList<List<String>>();
            Map<String, Status> statuses = new HashMap<String, Status>();
            if (checkers != null && checkers.size() > 0) {
                for (StatusChecker checker : checkers) {
                    String name = extensionLoader.getExtensionName(checker);
                    Status stat;
                    try {
                        stat = checker.check();
                    } catch (Throwable t) {
                        stat = new Status(Status.Level.ERROR, t.getMessage());
                    }
                    statuses.put(name, stat);
                    if (stat.getLevel() != null && stat.getLevel() != Status.Level.UNKNOWN) {
                        List<String> row = new ArrayList<String>();
                        row.add(name);
                        row.add(String.valueOf(stat.getLevel()));
                        row.add(stat.getMessage() == null ? "" : stat.getMessage());
                        table.add(row);
                    }
                }
            }
            Status stat = StatusUtils.getSummaryStatus(statuses);
            List<String> row = new ArrayList<String>();
            row.add("summary");
            row.add(String.valueOf(stat.getLevel()));
            row.add(stat.getMessage());
            table.add(row);
            return TelnetUtils.toTable(header, table);
        } else if (message.length() > 0) {
            return "Unsupported parameter " + message + " for status.";
        }
        String status = channel.getUrl().getParameter("status");
        Map<String, Status> statuses = new HashMap<String, Status>();
        if (status != null && status.length() > 0) {
            String[] ss = Constants.COMMA_SPLIT_PATTERN.split(status);
            for (String s : ss) {
                StatusChecker handler = extensionLoader.getExtension(s);
                Status stat;
                try {
                    stat = handler.check();
                } catch (Throwable t) {
                    stat = new Status(Status.Level.ERROR, t.getMessage());
                }
                statuses.put(s, stat);
            }
        }
        Status stat = StatusUtils.getSummaryStatus(statuses);
        return String.valueOf(stat.getLevel());
    }

}
