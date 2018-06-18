package net.minecraft.util;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

public class Util {

    @Nullable
    public static <V> V runTask(FutureTask<V> futuretask, Logger logger) {
        try {
            futuretask.run();
            return futuretask.get();
        } catch (ExecutionException executionexception) {
            logger.fatal("Error executing task", executionexception);
        } catch (InterruptedException interruptedexception) {
            logger.fatal("Error executing task", interruptedexception);
        }

        return null;
    }

    public static <T> T getLastElement(List<T> list) {
        return list.get(list.size() - 1);
    }
}
