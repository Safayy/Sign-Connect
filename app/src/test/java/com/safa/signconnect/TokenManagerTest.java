package com.safa.signconnect;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenManagerTest {

    @Test
    public void getTokens() {
        int val = TokenManager.getInstance().getTokens();
        assertEquals(true, val>0);
    }

    @Test
    public void spendToken() {
        int initialVal = TokenManager.getInstance().getTokens();
        TokenManager.getInstance().spendToken();
        int val = TokenManager.getInstance().getTokens();
        assertEquals(initialVal-5, val );
    }

    @Test
    public void earnToken() {
        int initialVal = TokenManager.getInstance().getTokens();
        TokenManager.getInstance().earnToken();
        int val = TokenManager.getInstance().getTokens();
        assertEquals(initialVal+3, val);
    }
}