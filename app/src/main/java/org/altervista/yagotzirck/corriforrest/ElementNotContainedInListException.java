package org.altervista.yagotzirck.corriforrest;

public class ElementNotContainedInListException extends RuntimeException {
    public ElementNotContainedInListException(String errMsg) {
        super(errMsg);
    }
}