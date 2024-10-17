package org.mpris;

public interface ReturnableTypeRunnable<S, T> {
    S run(T value);
}
