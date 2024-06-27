//package com.safa.signconnect.ui.adapter;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.res.TypedArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.safa.signconnect.R;
//
//public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
//    private String[] languageNames;
//    private TypedArray languageDrawables;
//    private OnItemClickListener mListener;
//    private Context context;
//
//    public interface OnItemClickListener {
//        void onItemClick(String languageName);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        mListener = listener;
//    }
//    public LanguageAdapter(String[] languageNames, Context context) {
//        this.languageNames = languageNames;
//        this.languageDrawables = languageDrawables;
//        this.context = context;
//    }
//    public LanguageAdapter(String[] languageNames, TypedArray languageDrawables, Context context) {
//        this.languageNames = languageNames;
//        this.languageDrawables = languageDrawables;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.tvLanguageName.setText(languageNames[position]);
//        if (languageDrawables != null)
//            holder.ivLanguageFlag.setImageDrawable(languageDrawables.getDrawable(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return languageNames.length;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView ivLanguageFlag;
//        TextView tvLanguageName;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            ivLanguageFlag = itemView.findViewById(R.id.ivLanguageFlag);
//            tvLanguageName = itemView.findViewById(R.id.tvLanguageName);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        String selectedLanguage = languageNames[position];
//                        SharedPreferences.Editor editor = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
//                        editor.putString("selectedLanguage", selectedLanguage);
//                        editor.apply();
//                    }
//                }
//            });
//        }
//    }
//}
//package com.safa.signconnect.ui.adapter;
//
//        import android.content.res.TypedArray;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.ImageView;
//        import android.widget.TextView;
//
//        import androidx.annotation.NonNull;
//        import androidx.recyclerview.widget.RecyclerView;
//
//        import com.safa.signconnect.R;
//
//        import java.util.ArrayList;
//
//public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
//    private static String[] languageNames;
//    private TypedArray languageDrawables;
//    private OnItemClickListener mListener;
//
//    public interface OnItemClickListener {
//        void onItemClick(String languageName);
//    }
//
//    public LanguageAdapter(String[] languageNames, TypedArray languageDrawables, OnItemClickListener listener) {
//        this.languageNames = languageNames;
//        this.languageDrawables = languageDrawables;
//        this.mListener = listener;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language, parent, false);
//        return new ViewHolder(view, mListener);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.tvLanguageName.setText(languageNames[position]);
//        if (languageDrawables != null)
//            holder.ivLanguageFlag.setImageResource(languageDrawables[position]);
//    }
//
//    @Override
//    public int getItemCount() {
//        return languageNames.length;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView ivLanguageFlag;
//        TextView tvLanguageName;
//
//        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
//            super(itemView);
//            ivLanguageFlag = itemView.findViewById(R.id.ivLanguageFlag);
//            tvLanguageName = itemView.findViewById(R.id.tvLanguageName);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        String selectedLanguage = languageNames[position];
//                        listener.onItemClick(selectedLanguage);
//                    }
//                }
//            });
//        }
//    }
//}
package com.safa.signconnect.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safa.signconnect.DBHelper;
import com.safa.signconnect.R;
import com.safa.signconnect.model.Translation;
import com.safa.signconnect.utils.ClipboardUtil;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final String TAG = "HistoryAdapter";
    private ArrayList<Translation> translations;
    private Context context;

    public HistoryAdapter(ArrayList<Translation> translations, Context context) {
        this.translations = translations;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        position = holder.getAdapterPosition();

        String sentence = translations.get(position).getSentence();
        int idTranslation = translations.get(position).getId();

        holder.tvTranslationHistory.setText(sentence);
        // Set OnClick Listeners
        holder.iBtnClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtil.copyToClipboard(context, sentence);
            }
        });
        int finalPosition = position;
        holder.iBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(idTranslation, finalPosition);
            }
        });
    }

    private void removeItem(int id, int position) {
        DBHelper dbHelper = new DBHelper(context);
        dbHelper.removeTranslation(id);
        translations.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTranslationHistory;
        public ImageButton iBtnClipboard;
        public ImageButton iBtnClose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTranslationHistory = itemView.findViewById(R.id.tvTranslationHistory);
            iBtnClipboard = itemView.findViewById(R.id.ibClipboard);
            iBtnClose = itemView.findViewById(R.id.ibClose);
        }
    }
}