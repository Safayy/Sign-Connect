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

import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safa.signconnect.R;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    private final String TAG = "LanguageAdapter";
    private static String[] languageNames;
    private TypedArray languageDrawables;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String languageName);
    }
    public LanguageAdapter(String[] languageNames, OnItemClickListener listener) {
        this.languageNames = languageNames;
        this.languageDrawables = null;
        this.mListener = listener;
    }
    public LanguageAdapter(String[] languageNames, TypedArray languageDrawables, OnItemClickListener listener) {
        this.languageNames = languageNames;
        this.languageDrawables = languageDrawables;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvLanguageName.setText(languageNames[position]);
        if(languageDrawables == null)
            holder.ivLanguageFlag.setVisibility(View.GONE);
        else
            holder.ivLanguageFlag.setImageDrawable(languageDrawables.getDrawable(position));
    }

    @Override
    public int getItemCount() {
        return languageNames.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLanguageFlag;
        TextView tvLanguageName;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            ivLanguageFlag = itemView.findViewById(R.id.ivLanguageFlag);
            tvLanguageName = itemView.findViewById(R.id.tvTranslationHistory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String selectedLanguage = languageNames[position];
                        listener.onItemClick(selectedLanguage);
                    }
                }
            });
        }
    }
}