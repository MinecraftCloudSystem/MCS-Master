package net.mcsproject.master.network.packet;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.mcsproject.master.logging.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ListenerRegistry {

    private static ListenerRegistry instance;
    public static ListenerRegistry getInstance() {
        if(instance == null) instance = new ListenerRegistry();
        return instance;
    }

    private Table<Class<?>, PacketListener, Method> packetListener = HashBasedTable.create();

    private ListenerRegistry() {}

    public void register(PacketListener listener) {
        for (Method method : listener.getClass().getMethods()) {
            if (method.getAnnotationsByType(PacketHandler.class).length == 0) {
                continue;
            }
            packetListener.put(method.getParameterTypes()[0], listener, method);
        }
    }

    public void callEvent(Packet packet) {
        if (!packetListener.containsRow(packet.getClass())) {
            return;
        }
        packetListener.row(packet.getClass()).forEach((listener, method) -> {
            try {
                method.invoke(listener, packet);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Log.error(ex.getMessage());
            }
        });
    }


}
