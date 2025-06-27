package br.com.ocauamotta.exceptions;

public class DaoException extends Exception {

    private static final long serialVersionUID = 7054379063290825137L;

    public DaoException(String m, Exception e) {
        super(m, e);
    }
}
