package br.com.ocauamotta.exceptions;

public class MultipleRecordsFoundException extends Exception {

    private static final long serialVersionUID = -7509649433607067138L;

    public MultipleRecordsFoundException(String m) {
        super(m);
    }
}
