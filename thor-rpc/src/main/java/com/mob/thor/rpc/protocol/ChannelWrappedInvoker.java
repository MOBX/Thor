package com.mob.thor.rpc.protocol;

import java.net.InetSocketAddress;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.api.protocol.AbstractInvoker;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.URL;
import com.mob.thor.rpc.remoting.api.*;
import com.mob.thor.rpc.remoting.api.exchange.ExchangeClient;
import com.mob.thor.rpc.remoting.api.exchange.support.header.HeaderExchangeClient;
import com.mob.thor.rpc.remoting.api.transport.ClientDelegate;

/**
 * 基于已有channel的invoker
 * 
 * @author zxc Jan 5, 2016 9:04:05 PM
 */
public class ChannelWrappedInvoker<T> extends AbstractInvoker<T> {

    private final ThorChannel channel;
    private final String      serviceKey;

    public ChannelWrappedInvoker(Class<T> serviceType, ThorChannel channel, URL url, String serviceKey) {

        super(serviceType, url, new String[] { Constants.GROUP_KEY, Constants.TOKEN_KEY, Constants.TIMEOUT_KEY });
        this.channel = channel;
        this.serviceKey = serviceKey;
    }

    @Override
    protected Result doInvoke(Invocation invocation) throws Throwable {
        RpcInvocation inv = (RpcInvocation) invocation;
        // 拿不到client端export 的service path.约定为interface的名称.
        inv.setAttachment(Constants.PATH_KEY, getInterface().getName());
        inv.setAttachment(Constants.CALLBACK_SERVICE_KEY, serviceKey);

        ExchangeClient currentClient = new HeaderExchangeClient(new ChannelWrapper(this.channel));

        try {
            if (getUrl().getMethodParameter(invocation.getMethodName(), Constants.ASYNC_KEY, false)) { // 不可靠异步
                currentClient.send(inv,
                                   getUrl().getMethodParameter(invocation.getMethodName(), Constants.SENT_KEY, false));
                return new RpcResult();
            }
            int timeout = getUrl().getMethodParameter(invocation.getMethodName(), Constants.TIMEOUT_KEY,
                                                      Constants.DEFAULT_TIMEOUT);
            if (timeout > 0) {
                return (Result) currentClient.request(inv, timeout).get();
            } else {
                return (Result) currentClient.request(inv).get();
            }
        } catch (RpcException e) {
            throw e;
        } catch (TimeoutException e) {
            throw new RpcException(RpcException.TIMEOUT_EXCEPTION, e.getMessage(), e);
        } catch (RemotingException e) {
            throw new RpcException(RpcException.NETWORK_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) { // here is non-biz exception, wrap it.
            throw new RpcException(e.getMessage(), e);
        }
    }

    public static class ChannelWrapper extends ClientDelegate {

        private final ThorChannel channel;
        private final URL         url;

        public ChannelWrapper(ThorChannel channel) {
            this.channel = channel;
            this.url = channel.getUrl().addParameter("codec", DubboCodec.NAME);
        }

        public URL getUrl() {
            return url;
        }

        public ThorChannelHandler getChannelHandler() {
            return channel.getChannelHandler();
        }

        public InetSocketAddress getLocalAddress() {
            return channel.getLocalAddress();
        }

        public void close() {
            channel.close();
        }

        public boolean isClosed() {
            return channel == null ? true : channel.isClosed();
        }

        public void reset(URL url) {
            throw new RpcException("ChannelInvoker can not reset.");
        }

        public InetSocketAddress getRemoteAddress() {
            return channel.getLocalAddress();
        }

        public boolean isConnected() {
            return channel == null ? false : channel.isConnected();
        }

        public boolean hasAttribute(String key) {
            return channel.hasAttribute(key);
        }

        public Object getAttribute(String key) {
            return channel.getAttribute(key);
        }

        public void setAttribute(String key, Object value) {
            channel.setAttribute(key, value);
        }

        public void removeAttribute(String key) {
            channel.removeAttribute(key);
        }

        public void reconnect() throws RemotingException {

        }

        public void send(Object message) throws RemotingException {
            channel.send(message);
        }

        public void send(Object message, boolean sent) throws RemotingException {
            channel.send(message, sent);
        }

    }

    public void destroy() {
        // channel资源的清空由channel创建者清除.
        // super.destroy();
        // try {
        // channel.close();
        // } catch (Throwable t) {
        // logger.warn(t.getMessage(), t);
        // }
    }

}
