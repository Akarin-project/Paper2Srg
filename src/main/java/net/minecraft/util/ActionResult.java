package net.minecraft.util;

public class ActionResult<T> {

    private final EnumActionResult type;
    private final T result;

    public ActionResult(EnumActionResult enuminteractionresult, T t0) {
        this.type = enuminteractionresult;
        this.result = t0;
    }

    public EnumActionResult getType() {
        return this.type;
    }

    public T getResult() {
        return this.result;
    }
}
