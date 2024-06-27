/**
 * Translate Activity to display the translation activity and the resulting sentence
 */
package com.safa.signconnect.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.safa.signconnect.R;

import com.safa.signconnect.TokenManager;
import com.safa.signconnect.utils.SentenceBuilder;
import com.safa.signconnect.detection.CameraActivity;
import com.safa.signconnect.utils.ImageUtils;
import com.safa.signconnect.utils.ClipboardUtil;
import com.safa.tfinterpreter.Detector;
import com.safa.tfinterpreter.TFLiteObjectDetectionAPIModel;
import android.speech.tts.TextToSpeech;

public class TranslateActivity extends CameraActivity {
  private final String TAG = "TranslateActivity";

  private static final DetectorMode MODE = DetectorMode.TF_OD_API;
  private Integer sensorOrientation;
  private Detector detector;
  private Bitmap rgbFrameBitmap = null;
  private Bitmap croppedBitmap = null;
  private Bitmap cropCopyBitmap = null;
  private Matrix frameToCropTransform;
  private Matrix cropToFrameTransform;

  private static final float confidenceThreshold = 0.56f;
  private static final Size DESIRED_PREVIEW_SIZE = new Size(500, 800);

  SentenceBuilder sentenceBuilder;
  private boolean computingDetection = false;
  private TextView tvTranslatedText;
  private long startTime;
  TextToSpeech textToSpeech;
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    // Initialize Spinner
    Spinner sp_sign_languages = findViewById(R.id.sp_sign_languages);
    Spinner sp_spoken_languages = findViewById(R.id.sp_spoken_languages);
    ArrayAdapter<CharSequence> sign_adapter = ArrayAdapter.createFromResource(this, R.array.lang_sign_abbreviated, android.R.layout.simple_spinner_item);
    sign_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
    sp_sign_languages.setAdapter(sign_adapter);
    ArrayAdapter<CharSequence> spoken_adapter = ArrayAdapter.createFromResource(this, R.array.lang_spoken, android.R.layout.simple_spinner_item);
    spoken_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
    sp_spoken_languages.setAdapter(spoken_adapter);


    // Initialize button on Click
    ImageButton iBtnClipboard = findViewById(R.id.iBtnClipboard);
    ImageButton iBtnSpam = findViewById(R.id.iBtnSpam);
    ImageButton iBtnRetry = findViewById(R.id.iBtnRetry);
    ImageButton iBtnSpeak = findViewById(R.id.iBtnSpeak);
    iBtnClipboard.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ClipboardUtil.copyToClipboard(getApplicationContext(), sentenceBuilder.getSentence());
      }
    });
    textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        if(status != TextToSpeech.ERROR)
          textToSpeech.setLanguage(Locale.CANADA);
      }
    });
    iBtnSpeak.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        textToSpeech.speak(sentenceBuilder.getSentence(), TextToSpeech.QUEUE_FLUSH, null);
      }
    });
    iBtnSpam.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sentenceBuilder.clear();
        tvTranslatedText.setText(sentenceBuilder.getSentence());
        Toast.makeText(getApplicationContext(), "Spam received. Removed from history.", Toast.LENGTH_SHORT).show();
      }
    });
    iBtnRetry.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sentenceBuilder.removeLastLetter();
        tvTranslatedText.setText(sentenceBuilder.getSentence());
      }
    });

    //Initialize Sentence Builder
    sentenceBuilder = new SentenceBuilder();
    tvTranslatedText = findViewById(R.id.tvTranslatedText);

    //Get Start Time for Sentence
    startTime = System.currentTimeMillis();
  }

  @Override
  public void onPreviewSizeChosen(final Size size, final int rotation) {
    int inputSize = getResources().getInteger(R.integer.tf_od_api_input_size);
    int cropSize = inputSize;
    try {
      detector =
              TFLiteObjectDetectionAPIModel.create(
                      this,
                      getString(R.string.tf_od_api_model_file),
                      getString(R.string.tf_od_api_labels_file),
                      inputSize,
                      false);
      cropSize = inputSize;
    } catch (final IOException e) {
      e.printStackTrace();
      Toast toast =
              Toast.makeText(
                      getApplicationContext(), "ObjectDetector could not be initialized", Toast.LENGTH_SHORT);
      toast.show();
      finish();
    }

    previewWidth = size.getWidth();
    previewHeight = size.getHeight();

    sensorOrientation = rotation - getScreenOrientation();

    rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
    croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

    frameToCropTransform =
            ImageUtils.getTransformationMatrix(
                    previewWidth, previewHeight,
                    cropSize, cropSize,
                    sensorOrientation, false);

    cropToFrameTransform = new Matrix();
    frameToCropTransform.invert(cropToFrameTransform);
  }

  @Override
  protected void processImage() {
    if (computingDetection) {
      readyForNextImage();
      return;
    }
    computingDetection = true;

    rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

    readyForNextImage();

    final Canvas canvas = new Canvas(croppedBitmap);
    canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

    runInBackground(
            new Runnable() {
              @Override
              public void run() {
                final long startTime = SystemClock.uptimeMillis();
                final List<Detector.Recognition> results = detector.recognizeImage(croppedBitmap);

                float minimumConfidence = confidenceThreshold;
                switch (MODE) {
                  case TF_OD_API:
                    minimumConfidence = confidenceThreshold;
                    break;
                }

                final List<Detector.Recognition> mappedRecognitions =
                        new ArrayList<>();

                for (final Detector.Recognition result : results) {
                  if (result.getConfidence() >= minimumConfidence) {

                    runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                        String detectionTitle = results.get(0).getTitle();
                        char detectedLetter = detectionTitle.charAt(detectionTitle.length() - 1);
                        Log.d(TAG, "Letter Detected " + detectedLetter);
                        sentenceBuilder.addLetter(detectedLetter);
                        tvTranslatedText.setText(sentenceBuilder.getSentence());
                      }
                    });
                  }
                }

                computingDetection = false;
              }
            });
  }

  @Override
  public void onBackPressed() {
    if (sentenceBuilder.getSentence() != null) {
      sentenceBuilder.save(startTime, getApplicationContext());
      Log.d(TAG, "Saved sentence");

      TokenManager.getInstance().spendToken();
      boolean hasMadeTranslation = true;
      Intent resultIntent = new Intent();
      resultIntent.putExtra("hasMadeTranslation", hasMadeTranslation);
      setResult(RESULT_OK, resultIntent);
    }
    super.onBackPressed();
  }

  @Override
  protected void onViewReady() {
    Log.d(TAG, "View is ready");
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_translate;
  }

  @Override
  protected Size getDesiredPreviewFrameSize() {
    return DESIRED_PREVIEW_SIZE;
  }

  private enum DetectorMode {
    TF_OD_API;
  }

  @Override
  protected void setUseNNAPI(final boolean isChecked) {
    runInBackground(
            () -> {
              try {
                detector.setUseNNAPI(isChecked);
              } catch (UnsupportedOperationException e) {
                runOnUiThread(
                        () -> {
                          Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        });
              }
            });
  }

  @Override
  protected void setNumThreads(final int numThreads) {
    runInBackground(() -> detector.setNumThreads(numThreads));
  }
}