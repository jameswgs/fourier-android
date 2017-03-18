package net.slenderloris.fourierandroid;

import org.junit.Test;

import static org.junit.Assert.*;

public class FourierTransformTest {

    @Test
    public void fourierTransform() throws Exception {
        float[] floats = FourierTransform.fourierTransform(new float[]{1.0f, 2.0f});
        assertArrayEquals(new float[]{ 2.0f, 4.0f }, floats, 0.0f);
    }

    @Test
    public void projectAt1Hz() throws Exception {
        float[] points = FourierTransform.projectAtHz(new float[]{1.0f, 2.0f}, 1.0f, 1.0f);
        assertArrayEquals(new float[]{1.0f, 0.0f, 2.0f, 0.0f}, points, 0.0001f);
    }

    @Test
    public void projectAtHalfHz() throws Exception {
        float[] points = FourierTransform.projectAtHz(new float[]{1.0f, 2.0f}, 1.0f, 0.5f);
        assertArrayEquals(new float[]{1.0f, 0.0f, -2.0f, 0.0f}, points, 0.0001f);
    }

    @Test
    public void projectAtQuarterHz() throws Exception {
        float[] points = FourierTransform.projectAtHz(new float[]{1.0f, 2.0f}, 1.0f, 0.25f);
        assertArrayEquals(new float[]{1.0f, 0.0f, 0.0f, 2.0f}, points, 0.0001f);
    }

    @Test
    public void projectAtSampleRate2() throws Exception {
        float[] points = FourierTransform.projectAtHz(new float[]{1.0f, 2.0f}, 2.0f, 1.0f);
        assertArrayEquals(new float[]{1.0f, 0.0f, -2.0f, 0.0f}, points, 0.0001f);
    }

}
