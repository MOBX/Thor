package com.mob.thor.rpc.protocol.status;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.extension.ExtensionLoader;
import com.mob.thor.rpc.common.status.Status;
import com.mob.thor.rpc.common.status.StatusChecker;
import com.mob.thor.rpc.common.store.DataStore;

@Activate
public class ThreadPoolStatusChecker implements StatusChecker {

    public Status check() {
        DataStore dataStore = ExtensionLoader.getExtensionLoader(DataStore.class).getDefaultExtension();
        Map<String, Object> executors = dataStore.get(Constants.EXECUTOR_SERVICE_COMPONENT_KEY);

        StringBuilder msg = new StringBuilder();
        Status.Level level = Status.Level.OK;
        for (Map.Entry<String, Object> entry : executors.entrySet()) {
            String port = entry.getKey();
            ExecutorService executor = (ExecutorService) entry.getValue();

            if (executor != null && executor instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor tp = (ThreadPoolExecutor) executor;
                boolean ok = tp.getActiveCount() < tp.getMaximumPoolSize() - 1;
                Status.Level lvl = Status.Level.OK;
                if (!ok) {
                    level = Status.Level.WARN;
                    lvl = Status.Level.WARN;
                }

                if (msg.length() > 0) {
                    msg.append(";");
                }
                msg.append("Pool status:" + lvl + ", max:" + tp.getMaximumPoolSize() + ", core:" + tp.getCorePoolSize()
                           + ", largest:" + tp.getLargestPoolSize() + ", active:" + tp.getActiveCount() + ", task:"
                           + tp.getTaskCount() + ", service port: " + port);
            }
        }
        return msg.length() == 0 ? new Status(Status.Level.UNKNOWN) : new Status(level, msg.toString());
    }
}
