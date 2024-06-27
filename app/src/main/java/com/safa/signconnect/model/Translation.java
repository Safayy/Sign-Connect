/**
 * Translation class of a sign to english text translation
 */
package com.safa.signconnect.model;

public class Translation {
    private int id;
    private String date;
    private String text;
    private String timeStart;
    private String timeEnd;

    public Translation(int id, String date, String text, String timeStart, String timeEnd) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }
    public int getId(){
        return this.id;
    }
    public String toString(){
        return ">"+date+" "+text+" "+timeStart+" "+timeEnd;
    }
    public String getSentence(){
        return text;
    }
}
