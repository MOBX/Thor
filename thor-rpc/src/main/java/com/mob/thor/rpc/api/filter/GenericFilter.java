package com.mob.thor.rpc.api.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import com.mob.thor.rpc.api.*;
import com.mob.thor.rpc.api.service.GenericException;
import com.mob.thor.rpc.api.support.ProtocolUtils;
import com.mob.thor.rpc.common.Constants;
import com.mob.thor.rpc.common.beanutil.JavaBeanAccessor;
import com.mob.thor.rpc.common.beanutil.JavaBeanDescriptor;
import com.mob.thor.rpc.common.beanutil.JavaBeanSerializeUtil;
import com.mob.thor.rpc.common.extension.Activate;
import com.mob.thor.rpc.common.extension.ExtensionLoader;
import com.mob.thor.rpc.common.io.UnsafeByteArrayInputStream;
import com.mob.thor.rpc.common.io.UnsafeByteArrayOutputStream;
import com.mob.thor.rpc.common.serialize.Serialization;
import com.mob.thor.rpc.common.utils.PojoUtils;
import com.mob.thor.rpc.common.utils.ReflectUtils;
import com.mob.thor.rpc.common.utils.StringUtils;

@Activate(group = Constants.PROVIDER, order = -20000)
public class GenericFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
        if (inv.getMethodName().equals(Constants.$INVOKE) && inv.getArguments() != null
            && inv.getArguments().length == 3
            && !ProtocolUtils.isGeneric(invoker.getUrl().getParameter(Constants.GENERIC_KEY))) {
            String name = ((String) inv.getArguments()[0]).trim();
            String[] types = (String[]) inv.getArguments()[1];
            Object[] args = (Object[]) inv.getArguments()[2];
            try {
                Method method = ReflectUtils.findMethodByMethodSignature(invoker.getInterface(), name, types);
                Class<?>[] params = method.getParameterTypes();
                if (args == null) {
                    args = new Object[params.length];
                }
                String generic = inv.getAttachment(Constants.GENERIC_KEY);
                if (StringUtils.isEmpty(generic) || ProtocolUtils.isDefaultGenericSerialization(generic)) {
                    args = PojoUtils.realize(args, params, method.getGenericParameterTypes());
                } else if (ProtocolUtils.isJavaGenericSerialization(generic)) {
                    for (int i = 0; i < args.length; i++) {
                        if (byte[].class == args[i].getClass()) {
                            try {
                                UnsafeByteArrayInputStream is = new UnsafeByteArrayInputStream((byte[]) args[i]);
                                args[i] = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(Constants.GENERIC_SERIALIZATION_NATIVE_JAVA).deserialize(null,
                                                                                                                                                                        is).readObject();
                            } catch (Exception e) {
                                throw new RpcException("Deserialize argument [" + (i + 1) + "] failed.", e);
                            }
                        } else {
                            throw new RpcException(
                                                   new StringBuilder(32).append("Generic serialization [").append(Constants.GENERIC_SERIALIZATION_NATIVE_JAVA).append("] only support message type ").append(byte[].class).append(" and your message type is ").append(args[i].getClass()).toString());
                        }
                    }
                } else if (ProtocolUtils.isBeanGenericSerialization(generic)) {
                    for (int i = 0; i < args.length; i++) {
                        if (args[i] instanceof JavaBeanDescriptor) {
                            args[i] = JavaBeanSerializeUtil.deserialize((JavaBeanDescriptor) args[i]);
                        } else {
                            throw new RpcException(
                                                   new StringBuilder(32).append("Generic serialization [").append(Constants.GENERIC_SERIALIZATION_BEAN).append("] only support message type ").append(JavaBeanDescriptor.class.getName()).append(" and your message type is ").append(args[i].getClass().getName()).toString());
                        }
                    }
                }
                Result result = invoker.invoke(new RpcInvocation(method, args, inv.getAttachments()));
                if (result.hasException() && !(result.getException() instanceof GenericException)) {
                    return new RpcResult(new GenericException(result.getException()));
                }
                if (ProtocolUtils.isJavaGenericSerialization(generic)) {
                    try {
                        UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream(512);
                        ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(Constants.GENERIC_SERIALIZATION_NATIVE_JAVA).serialize(null,
                                                                                                                                                    os).writeObject(result.getValue());
                        return new RpcResult(os.toByteArray());
                    } catch (IOException e) {
                        throw new RpcException("Serialize result failed.", e);
                    }
                } else if (ProtocolUtils.isBeanGenericSerialization(generic)) {
                    return new RpcResult(JavaBeanSerializeUtil.serialize(result.getValue(), JavaBeanAccessor.METHOD));
                } else {
                    return new RpcResult(PojoUtils.generalize(result.getValue()));
                }
            } catch (NoSuchMethodException e) {
                throw new RpcException(e.getMessage(), e);
            } catch (ClassNotFoundException e) {
                throw new RpcException(e.getMessage(), e);
            }
        }
        return invoker.invoke(inv);
    }

}
