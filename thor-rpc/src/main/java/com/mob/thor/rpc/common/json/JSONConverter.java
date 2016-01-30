package com.mob.thor.rpc.common.json;

import java.io.IOException;

public interface JSONConverter {

    /**
     * write object.
     * 
     * @param obj obj.
     * @param builder builder.
     * @throws IOException
     */
    void writeValue(Object obj, JSONWriter builder, boolean writeClass) throws IOException;

    /**
     * convert json value to target class.
     * 
     * @param type target type.
     * @param jv json value.
     * @return target object.
     * @throws IOException.
     */
    Object readValue(Class<?> type, Object jv) throws IOException;
}
