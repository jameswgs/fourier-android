package net.slenderloris.fourierandroid;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final float SCALE = 1.0f;
    private AudioRecord recorder;
    private float[] hzArray;

    public MainActivity() {
        hzArray = buildHzArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                44100*2);

        recorder.startRecording();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawSpectrum();
            }
        }, 1000);
    }

    private void drawSpectrum() {
        recorder.stop();
        float[] normalizedAudioData = readNormalizedAudioData();
        int[] colours = createColours(normalizedAudioData);
        Bitmap bitmap = createBitmap(colours);
        setBitmap(bitmap);
    }

    private Bitmap createBitmap(int[] colours) {
        return Bitmap.createBitmap(colours, 100, 100, Bitmap.Config.ARGB_8888);
    }

    private void setBitmap(Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.spectrum);
        imageView.setImageBitmap(bitmap);
    }

    private int[] createColours(float[] normalizedAudioData) {
        int colourIndex = 0;
        int colours[] = new int[100*100];

        for (int i = 0; i < 100; i++) {
            float frequencies[] = getFrequencies(normalizedAudioData, i*441, 441);
            for (int j = 0; j <100; j++) {
                colours[colourIndex++] = getRgb(frequencies[j]);
            }
        }
        return colours;
    }

    private float[] readNormalizedAudioData() {
        short[] audioData = new short[44100];
        recorder.read(audioData, 0, 44100);
        return normalize(audioData);
    }

    private float[] getFrequencies(float[] normalizedAudioData, int start, int count) {
        float section[] = Arrays.copyOfRange(normalizedAudioData, start, start+count);
        return FourierTransform.magnitudesOfSummedProjectedVectors(section, 44100.0f, hzArray);
    }

    private int getRgb(float frequency1) {
        float frequency = frequency1 * SCALE;
        if(frequency<0.0f) frequency = 0.0f;
        if(frequency>255.0f) frequency = 255.0f;
        int intensity = (int) (frequency);
        return Color.rgb(intensity, intensity, intensity);
    }

    private float[] normalize(short[] audioData) {
        float min = Short.MAX_VALUE;
        float max = 0;
        for (short i : audioData) {
            if(i<min) min = i;
            if(i>max) max = i;
        }
        float range = max-min;
        float normalized[] = new float[audioData.length];
        for (int i = 0; i < audioData.length; i++) {
            normalized[i] = (audioData[i]-min)/range;
        }
        return normalized;
    }

    private float[] buildHzArray() {
        float hzArray[] = new float[100];
        float step = ( 20000.0f - 20.0f ) / 100.0f;
        float start = 20.0f;
        for (int i = 0; i <100; i++) {
            hzArray[i] = start + step * i;
        }
        return hzArray;
    }


}
