package com.anderpri.openlibganizer.controllers;


public class ConnectionController {

    private static ConnectionController INSTANCE;

    public static ConnectionController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionController();
        }
        return INSTANCE;
    }

}
