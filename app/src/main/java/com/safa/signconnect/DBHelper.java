/**
 * Database helper class for managing language and translation data
 */
package com.safa.signconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.safa.signconnect.model.Translation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "DB";
    private static final String TABLE_NAME = "Languages";
    private static final String KEY_ID = "id";
    private static final String TYPE_LANGUAGE = "type";
    private static final String NAME_LANGUAGE = "language";

    private static final String TRANSLATIONS_TABLE_NAME = "Translations";
    private static final String KEY_TRANSLATION_ID = "translation_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TEXT = "text";
    private static final String KEY_TIME_START = "time_start";
    private static final String KEY_TIME_END = "time_end";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time
     * @param db The SQLiteDatabase instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LANGUAGE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + NAME_LANGUAGE + " TEXT,"
                + TYPE_LANGUAGE + " TEXT)";
        db.execSQL(CREATE_LANGUAGE_TABLE);

        String CREATE_TRANSLATIONS_TABLE = "CREATE TABLE " + TRANSLATIONS_TABLE_NAME + "("
                + KEY_TRANSLATION_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " TEXT,"
                + KEY_TEXT + " TEXT,"
                + KEY_TIME_START + " TEXT,"
                + KEY_TIME_END + " TEXT)";
        db.execSQL(CREATE_TRANSLATIONS_TABLE);
    }

    /**
     * Called when the database needs to be upgraded
     * @param db The SQLiteDatabase instance
     * @param oldVersion The old database version
     * @param newVersion The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSLATIONS_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Method to set the language in the database
     * @param languageType The type of the language
     * @param languageName The name of the language
     */
    public void setLanguage(String languageType, String languageName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TYPE_LANGUAGE, languageType);
        values.put(NAME_LANGUAGE, languageName);

        // Check if there is already an entry for the language type
        String selection = TYPE_LANGUAGE + "=?";
        String[] selectionArgs = { languageType };
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            // Update the existing entry
            db.update(TABLE_NAME, values, selection, selectionArgs);
        } else {
            // Insert a new entry
            db.insert(TABLE_NAME, null, values);
        }

        // Close cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    /**
     * Method to get the language set for a language type in the database
     * @param languageType The type of the language
     */
    public String getLanguage(String languageType) {
        SQLiteDatabase db = this.getReadableDatabase();
        String languageName = null;

        String[] projection = { NAME_LANGUAGE };
        String selection = TYPE_LANGUAGE + "=?";
        String[] selectionArgs = { languageType };

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            languageName = cursor.getString(cursor.getColumnIndex(NAME_LANGUAGE));
            cursor.close();
        }

        db.close();
        return languageName;
    }

    /**
     * Method to add a sentence translation to the database
     * @param text The translated text
     * @param timeStart The start time of the translation
     * @param timeEnd The end time of the translation
     */
    public void addTranslation(String text, long timeStart, long timeEnd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, getCurrentDate());
        values.put(KEY_TEXT, text);
        values.put(KEY_TIME_START, getTimeString(timeStart));
        values.put(KEY_TIME_END, getTimeString(timeEnd));
        db.insert(TRANSLATIONS_TABLE_NAME, null, values);
        db.close();
    }
    /**
     * Method to remove a translation from the database
     * @param id The id of the translation to be removed
     */
    public void removeTranslation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TRANSLATIONS_TABLE_NAME, KEY_TRANSLATION_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    /**
     * Method to retrieve all translations from the database
     * @return ArrayList of Translation objects containing all data
     */
    public ArrayList<Translation> getAllTranslations() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Translation> translations = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TRANSLATIONS_TABLE_NAME, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int translationIdIndex = cursor.getColumnIndex(KEY_TRANSLATION_ID);
                int dateIndex = cursor.getColumnIndex(KEY_DATE);
                int textIndex = cursor.getColumnIndex(KEY_TEXT);
                int timeStartIndex = cursor.getColumnIndex(KEY_TIME_START);
                int timeEndIndex = cursor.getColumnIndex(KEY_TIME_END);

                int translationId = cursor.getInt(translationIdIndex);
                String date = cursor.getString(dateIndex);
                String text = cursor.getString(textIndex);
                String timeStart = cursor.getString(timeStartIndex);
                String timeEnd = cursor.getString(timeEndIndex);

                Translation translationItem = new Translation(translationId, date, text, timeStart, timeEnd);
                translations.add(translationItem);
            }
            cursor.close();
        }
        db.close();
        return translations;
    }

    /**
     * Method to get the current date in yyyy-MM-dd format
     * @return The current date
     */
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    /**
     * Method to get the current time in HH:mm:ss format
     * @return The current date
     */
    private String getTimeString(long timeInMillis) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date(timeInMillis);
        return timeFormat.format(date);
    }
}