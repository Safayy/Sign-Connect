/**
 * Utility class for managing clipboard operations.
 */
package com.safa.signconnect.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

public class ClipboardUtil {

    /**
     * Copies the given text to the clipboard.
     * @param context The context from which the method is called.
     * @param text The text to be copied to the clipboard.
     */
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText("Sign Connect Translation", text);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Clipboard not available", Toast.LENGTH_SHORT).show();
        }
    }
}
