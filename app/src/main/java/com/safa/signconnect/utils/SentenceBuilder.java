/**
 * Sentence Builder manages the sentence created by the detection letters, and estimates words from it.
 */
package com.safa.signconnect.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.safa.signconnect.DBHelper;

public class SentenceBuilder {
    private StringBuilder rawSentence;
    public SentenceBuilder() {
        rawSentence = new StringBuilder();
    }
    /**
     * Appends the given letter to the sentence
     * @param letter the letter to be added to the sentence
     */
    public void addLetter(char letter) {
        rawSentence.append(letter);
    }
    /**
     * Perfome logical analysis on the raw text to get a sentence
     * @return The sentence with assumptions
     */
    public String getSentence() {
        StringBuilder sentence = new StringBuilder();

        if (rawSentence != null && rawSentence.length() > 0) {
            char prevChar = rawSentence.charAt(0);
            sentence.append(prevChar);

            for (int i = 1; i < rawSentence.length(); i++) {
                char currentChar = rawSentence.charAt(i);
                if (currentChar != prevChar) {
                    sentence.append(currentChar);
                    prevChar = currentChar;
                }
            }
        }
        return sentence.toString();
    }
    /**
     * Insert sentence entry to database
     */
    public void save(long startTime, Context context){
        if (TextUtils.isEmpty(getSentence())) {
            Log.d("TAG", "Sentence is null. Exiting save method.");
            return;
        }
        long endTime = System.currentTimeMillis();

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.addTranslation(String.valueOf(getSentence()), startTime, endTime);
        Log.d("TAG", "Successfully saved to database");
    }
    /**
     * Clear input
     */
    public void clear(){
        rawSentence = new StringBuilder();
    }
    /**
     * Remove last character from sentence
     */
    public void removeLastLetter(){
        if (rawSentence == null || rawSentence.length() == 0)
            return;
        rawSentence.deleteCharAt(rawSentence.length() - 1);
    }
}