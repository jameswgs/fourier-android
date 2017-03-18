package net.slenderloris.fourierandroid;

public class FourierTransform {

    static {
        System.loadLibrary("native-lib");
    }

    public native static float[] fourierTransform(float[] samples);

    public native static float[] projectAtHz(float[] samples, float rate, float hz);
}
