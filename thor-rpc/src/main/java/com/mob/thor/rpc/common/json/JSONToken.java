package com.mob.thor.rpc.common.json;

public class JSONToken {

    // token type
    public static final int ANY  = 0, IDENT = 0x01, LBRACE = 0x02, LSQUARE = 0x03, RBRACE = 0x04, RSQUARE = 0x05,
            COMMA = 0x06, COLON = 0x07;

    public static final int NULL = 0x10, BOOL = 0x11, INT = 0x12, FLOAT = 0x13, STRING = 0x14, ARRAY = 0x15,
            OBJECT = 0x16;

    public final int        type;

    public final Object     value;

    JSONToken(int t) {
        this(t, null);
    }

    JSONToken(int t, Object v) {
        type = t;
        value = v;
    }

    static String token2string(int t) {
        switch (t) {
            case LBRACE:
                return "{";
            case RBRACE:
                return "}";
            case LSQUARE:
                return "[";
            case RSQUARE:
                return "]";
            case COMMA:
                return ",";
            case COLON:
                return ":";
            case IDENT:
                return "IDENT";
            case NULL:
                return "NULL";
            case BOOL:
                return "BOOL VALUE";
            case INT:
                return "INT VALUE";
            case FLOAT:
                return "FLOAT VALUE";
            case STRING:
                return "STRING VALUE";
            default:
                return "ANY";
        }
    }
}
