/**
 * TokenManger Singleton class that holds the values of tokens remaining in a user profile
 */
package com.safa.signconnect;

public class TokenManager {
    private static TokenManager instance;
    private int tokens;

    final int TRANSLATION_COST = 5;
    final int FEEDBACK_REWARD = 3;

    private TokenManager() {
        tokens = 205;
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public int getTokens() {
        return tokens;
    }

    public void spendToken(){
        tokens -= TRANSLATION_COST;
        if(tokens<0)
            tokens = 0;
    }
    public void earnToken(){
        tokens += FEEDBACK_REWARD;
    }
}
