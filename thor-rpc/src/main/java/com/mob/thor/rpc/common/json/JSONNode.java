package com.mob.thor.rpc.common.json;

import java.io.IOException;

interface JSONNode {

    /**
     * write json string.
     * 
     * @param jc json converter.
     * @param jb json builder.
     * @throws IOException
     */
    void writeJSON(JSONConverter jc, JSONWriter jb, boolean writeClass) throws IOException;
}
