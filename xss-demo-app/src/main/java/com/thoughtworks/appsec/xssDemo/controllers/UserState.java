package com.thoughtworks.appsec.xssDemo.controllers;

public class UserState {
    private final boolean isLoggedIn;

    public UserState(final boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
