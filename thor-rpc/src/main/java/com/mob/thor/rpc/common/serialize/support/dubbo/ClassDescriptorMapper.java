package com.mob.thor.rpc.common.serialize.support.dubbo;

public interface ClassDescriptorMapper {

    /**
     * get Class-Descriptor by index.
     * 
     * @param index index.
     * @return string.
     */
    String getDescriptor(int index);

    /**
     * get Class-Descriptor index
     * 
     * @param desc Class-Descriptor
     * @return index.
     */
    int getDescriptorIndex(String desc);
}
