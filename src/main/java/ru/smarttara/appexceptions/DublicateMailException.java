package ru.smarttara.appexceptions;

/**
 * Created by ZXCASD on 08.07.2016.
 */
public class DublicateMailException extends Exception {

    public DublicateMailException() {
    }

    public DublicateMailException(String message) {
        super(message);
    }
}
