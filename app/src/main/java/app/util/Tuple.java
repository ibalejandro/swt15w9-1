package app.util;


/**
 * This is a very simple class which allows storage of two arbitrary values in a type safe way.
 *
 * @param <T1>
 * @param <T2>
 */
public final class Tuple<T1, T2> {
    private T1 _t1;
    private T2 _t2;

    public Tuple(T1 t1, T2 t2) {
        _t1 = t1;
        _t2 = t2;
    }

    public T1 get1() {
        return _t1;
    }

    public void set1(T1 t) {
        _t1 = t;
    }

    public T2 get2() {
        return _t2;
    }

    public void set2(T2 t) {
        _t2 = t;
    }
}
