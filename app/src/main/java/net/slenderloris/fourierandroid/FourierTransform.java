package net.slenderloris.fourierandroid;

public class FourierTransform {

    static {
        System.loadLibrary("native-lib");
    }

    public native static float[] fourierTransform(float[] samples);

    public native static float[] project(float[] samples, float rate, float hz);

    public native static float magnitudeOfSummedProjectedVectors(float[] samples, float rate, float hz);

    public native static float[] magnitudesOfSummedProjectedVectors(float[] samples, float rate, float[] hzArray);
}
