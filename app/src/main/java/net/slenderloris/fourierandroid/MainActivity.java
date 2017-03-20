package net.slenderloris.fourierandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final float SCALE = 1.0f;
    private static final int SAMPLE_RATE_IN_HZ = 44100;
    private static final int ONE_SECOND_BUFFER_SIZE = SAMPLE_RATE_IN_HZ * 2;
    private static final float START_RANGE = 20.0f;
    private static final float END_RANGE = 20000.0f;
    private static final int NUM_FRAMES = 100;

    private AudioRecord recorder;
    private float[] hzArray;
    private ImageView imageView;

    public MainActivity() {
        hzArray = buildHzArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.spectrum);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        if (shouldRequestPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                int i = 0;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            }
        }
        else {
            startRecording();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRecording();
        }
    }

    private void startRecording() {
        int bufferSize = AudioRecord.getMinBufferSize(
                SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (bufferSize < ONE_SECOND_BUFFER_SIZE) {
            bufferSize = ONE_SECOND_BUFFER_SIZE;
        }

        recorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        int state = recorder.getState();

        recorder.startRecording();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawSpectrum();
            }
        }, 1000);
    }

    private boolean shouldRequestPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED;
    }

    private void drawSpectrum() {
        recorder.stop();
        float[] normalizedAudioData = readNormalizedAudioData();
        int[] colours = createColours(normalizedAudioData);
        Bitmap bitmap = createBitmap(colours);
        setBitmap(bitmap);
    }

    private Bitmap createBitmap(int[] colours) {
        return Bitmap.createBitmap(colours, hzArray.length, NUM_FRAMES, Bitmap.Config.ARGB_8888);
    }

    private void setBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    private int[] createColours(float[] normalizedAudioData) {
        int colourIndex = 0;
        int colours[] = new int[hzArray.length*NUM_FRAMES];

        OverlappingRanges.Windows r = OverlappingRanges.find(44100, 100);

        for (int i = 0; i < NUM_FRAMES; i++) {
            float amplitudes[] = getAmplitudes(normalizedAudioData, i*220, 440);
            for (int j = 0; j <hzArray.length; j++) {
                colours[colourIndex++] = getRgb(amplitudes[j], hzArray[j]);
            }
        }
        return colours;
    }

    private float[] readNormalizedAudioData() {
        short[] audioData = new short[44100];
        recorder.read(audioData, 0, 44100);
        return normalize(audioData);
    }

    private float[] getAmplitudes(float[] normalizedAudioData, int start, int count) {
        float section[] = Arrays.copyOfRange(normalizedAudioData, start, start+count);
        return FourierTransform.magnitudesOfSummedProjectedVectors(section, 44100.0f, hzArray);
    }

    private int getRgb(float amplitude, float frequency) {
        float scaledAmplitude = amplitude * SCALE * (frequency / 20.0f);
        if(scaledAmplitude<0.0f) scaledAmplitude = 0.0f;
        if(scaledAmplitude>255.0f) scaledAmplitude = 255.0f;
        int intensity = (int) (scaledAmplitude);
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
        float step = ( END_RANGE - START_RANGE) / (float) hzArray.length;
        for (int i = 0; i <hzArray.length; i++) {
            hzArray[i] = START_RANGE + step * i;
        }
        return hzArray;
    }


}
