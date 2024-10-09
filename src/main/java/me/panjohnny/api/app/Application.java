package me.panjohnny.api.app;

import me.panjohnny.api.wrap.PointerWrapper;

import java.util.HashMap;

public abstract class Application {
    protected Core core;
    private HashMap<Class<?>, Object> pointers = new HashMap<>();

    public Application() {
        prepare();
    }

    private void prepare() {
        core = new Core(this);
        core.start();
        init();
    }

    public abstract void init();

    public abstract void loadGraphics();

    public abstract void draw();

    public <T extends PointerWrapper> T getWrapper(Class<T> clazz) {
        return clazz.cast(pointers.get(clazz));
    }

    void createPointerWrapper(Class<? extends PointerWrapper> clazz, long pointer) {
        try {
            pointers.put(clazz, clazz.getConstructor(long.class).newInstance(pointer));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create pointer wrapper for " + clazz.getName(), e);
        }
    }
}
