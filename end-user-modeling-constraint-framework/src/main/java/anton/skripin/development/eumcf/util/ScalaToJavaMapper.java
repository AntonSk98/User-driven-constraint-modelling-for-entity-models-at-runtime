package anton.skripin.development.eumcf.util;

import scala.concurrent.Future;
import scala.jdk.javaapi.CollectionConverters;
import scala.jdk.javaapi.FutureConverters;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Set of wrapper methods to translate scala to java classes.
 */
public final class ScalaToJavaMapper {
    private ScalaToJavaMapper() {

    }

    /**
     * Converts {@link Future} into {@link java.util.concurrent.Future}.
     *
     * @param future future
     * @param <T>    type of future result
     * @return {@link CompletableFuture<T>}
     */
    public static <T> CompletableFuture<T> future(Future<T> future) {
        return FutureConverters.asJava(future).toCompletableFuture();
    }

    /**
     * Converts {@link scala.collection.Set} into {@link Set}.
     *
     * @param set set
     * @param <T> type of each element in a set
     * @return {@link Set<T>}
     */
    public static <T> Set<T> set(scala.collection.Set<T> set) {
        return CollectionConverters.asJava(set);
    }

    /**
     * Converts {@link scala.collection.Map<> map} into {@link Map<>}.
     *
     * @param map map
     * @param <T> key type
     * @param <Z> value type
     * @return {@link Map<>}
     */
    public static <T, Z> Map<T, Z> map(scala.collection.Map<T, Z> map) {
        return CollectionConverters.asJava(map);
    }
}
