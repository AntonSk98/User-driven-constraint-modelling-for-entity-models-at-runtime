package anton.skripin.development.eumcf.util;

import scala.collection.View;
import scala.concurrent.Future;
import scala.jdk.javaapi.CollectionConverters;
import scala.jdk.javaapi.FutureConverters;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ScalaToJavaMapping {
    private ScalaToJavaMapping() {

    }

    public static  <T> CompletableFuture<T> future(Future<T> future) {
        return FutureConverters.asJava(future).toCompletableFuture();
    }

    public static <T> Set<T> set(scala.collection.Set<T> set) {
        return CollectionConverters.asJava(set);
    }

    public static <T, Z> Map<T,Z> map(scala.collection.Map<T,Z> map) {
        return CollectionConverters.asJava(map);
    }
}
