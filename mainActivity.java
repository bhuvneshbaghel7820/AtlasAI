package com.example.atlasai;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private static final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Text to Speech engine
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US); // Default language
            }
        });

        // Initialize Speech Recognition
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new SpeechRecognitionListener());

        startListening();
    }

    // Function to start listening to user's speech
    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");
        speechRecognizer.startListening(intent);
    }

    // Handle the recognition result and perform actions
    private class SpeechRecognitionListener implements android.speech.RecognitionListener {
        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null) {
                String command = matches.get(0);
                Log.d("Atlas", "Command recognized: " + command);
                handleCommand(command);
            }
        }

        @Override
        public void onError(int error) {
            Log.e("Atlas", "Recognition Error: " + error);
        }

        // Add other overridden methods like onReadyForSpeech, onBeginningOfSpeech, etc.
    }

    // Handle different commands (e.g., opening apps, sending messages)
    private void handleCommand(String command) {
        if (command.contains("open WhatsApp")) {
            openApp("com.whatsapp");
        } else if (command.contains("call mom")) {
            makeCall("1234567890"); // Replace with actual phone number
        } else if (command.contains("translate")) {
            translateText("Hello, Atlas!", "es"); // Example: Translate to Spanish
        }
    }

    private void openApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void makeCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    private void translateText(String text, String targetLanguage) {
        // Implement translation logic (e.g., using an offline model)
        String translatedText = "Texto traducido";  // Example translation
        textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
