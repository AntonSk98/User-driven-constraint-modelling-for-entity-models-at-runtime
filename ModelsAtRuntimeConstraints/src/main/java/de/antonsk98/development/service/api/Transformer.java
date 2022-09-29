package de.antonsk98.development.service.api;

/**
 * Abstract transformer of a source model to a target model.
 * @param <T> Source model
 * @param <Z> Target model
 */
public interface Transformer <T, Z> {

    /**
     * Transforms a given model to another model.
     * @param model Given model.
     * @return Transformed source model.
     */
    public T transform(Z model);
}
