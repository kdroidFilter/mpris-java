package org.mpris.mpris;

import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.Variant;

import java.util.Map;

@DBusInterfaceName("org.freedesktop.DBus.Properties")
public interface DBusProperties extends DBusInterface {
    Variant<?> Get(String interface_name, String property_name) throws DBusException;
    Map<String, Variant<?>> GetAll(String interface_name) throws DBusException;
    void Set(String interface_name, String property_name, Variant<?> value) throws DBusException;
}
