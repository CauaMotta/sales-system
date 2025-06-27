package br.com.ocauamotta.exceptions;

public class KeyTypeNotFoundException extends Exception {

    private static final long serialVersionUID = -1389494676398525746L;

    public KeyTypeNotFoundException(String m) {
        this(m, null);
    }

    public KeyTypeNotFoundException(String m, Throwable e) {
        super(m, e);
    }
}
