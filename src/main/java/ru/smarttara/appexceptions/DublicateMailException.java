package ru.smarttara.appexceptions;

/**
 * class that displays situation in which there is same email
 * in database and comes unique constraint SQL exception
 *
 * @author ZXCASD on 08.07.2016.
 */
public class DublicateMailException extends Exception {

    /**
     * Just constructs class
     */
    public DublicateMailException() {
    }

    /***
     * Constructs class with message
     *
     * @param message diagnostic message
     */
    public DublicateMailException(String message) {
        super(message);
    }
}
