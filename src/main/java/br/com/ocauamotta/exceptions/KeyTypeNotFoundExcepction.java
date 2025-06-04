package br.com.ocauamotta.exceptions;

public class KeyTypeNotFoundExcepction extends Exception {

    private static final long serialVersionUID = -1389494676398525746L;

    public KeyTypeNotFoundExcepction(String err) {
        this(err, null);
    }

    public KeyTypeNotFoundExcepction(String err, Throwable e) {
        super(err, e);
    }
}
