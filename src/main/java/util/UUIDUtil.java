package util;

import java.util.UUID;

public class UUIDUtil {
    public static String generateUUID() {
       String uuid = UUID.randomUUID().toString();
       String[] uuids = uuid.split("-");
        return uuids[0];
    }
}
