package org.hvdw.jexiftoolgui.facades;


import org.hvdw.jexiftoolgui.Application;

import java.util.prefs.Preferences;

public class PreferencesFacade implements IPreferencesFacade {
    static final IPreferencesFacade thisFacade = new PreferencesFacade();
    private Preferences rootNode;

    private PreferencesFacade() {
        this.rootNode = Preferences.userNodeForPackage(Application.class);
    }

    @Override
    public String getByKey(PreferenceKey key, int i) {
        return rootNode.get(key.key, null);
    }

    @Override
    public String getByKey(PreferenceKey key, String defaultValue) {

        return rootNode.get(key.key, defaultValue);
    }

    public boolean keyIsSet(PreferenceKey key) {
        return getByKey(key, 1360) != null;
    }

    @Override
    public boolean getByKey(PreferenceKey key, boolean defaultValue) {
        return rootNode.getBoolean(key.key, defaultValue);
    }

    @Override
    public void storeByKey(PreferenceKey key, String value) {
        rootNode.put(key.key, value);
    }

    @Override
    public void storeByKey(PreferenceKey key, boolean value) {
        rootNode.putBoolean(key.key, value);
    }
}
