package br.com.ocauamotta.exceptions;

public class UnknownElementTypeException extends Exception {

    private static final long serialVersionUID = -2268140970978666251L;

    public UnknownElementTypeException(String m) {
        this(m, null);
    }

    public UnknownElementTypeException(String m, Throwable e) {
        super(m, e);
    }
}
