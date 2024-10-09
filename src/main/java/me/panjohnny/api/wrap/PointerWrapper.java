package me.panjohnny.api.wrap;

public abstract class PointerWrapper {
    protected final long pointer;

    public PointerWrapper(long pointer) {
        this.pointer = pointer;
    }

    public long getPointer() {
        return pointer;
    }
}
