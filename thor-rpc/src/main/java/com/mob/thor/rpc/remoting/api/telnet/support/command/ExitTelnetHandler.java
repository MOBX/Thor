package com.mob.thor.rpc.remoting.api.telnet.support.command;

import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.remoting.api.ThorChannel;
import com.mob.thor.rpc.remoting.api.telnet.TelnetHandler;
import com.mob.thor.rpc.remoting.api.telnet.support.Help;

@Activate
@Help(parameter = "", summary = "Exit the telnet.", detail = "Exit the telnet.")
public class ExitTelnetHandler implements TelnetHandler {

    public String telnet(ThorChannel channel, String message) {
        channel.close();
        return null;
    }

}
