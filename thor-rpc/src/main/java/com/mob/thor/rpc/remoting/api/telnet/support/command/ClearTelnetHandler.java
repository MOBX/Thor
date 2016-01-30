package com.mob.thor.rpc.remoting.api.telnet.support.command;

import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.utils.StringUtils;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.telnet.TelnetHandler;
import com.mob.thor.rpc.remoting.api.telnet.support.Help;

@Activate
@Help(parameter = "[lines]", summary = "Clear screen.", detail = "Clear screen.")
public class ClearTelnetHandler implements TelnetHandler {

    public String telnet(ThorChannel channel, String message) {
        int lines = 100;
        if (message.length() > 0) {
            if (!StringUtils.isInteger(message)) {
                return "Illegal lines " + message + ", must be integer.";
            }
            lines = Integer.parseInt(message);
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < lines; i++) {
            buf.append("\r\n");
        }
        return buf.toString();
    }

}
