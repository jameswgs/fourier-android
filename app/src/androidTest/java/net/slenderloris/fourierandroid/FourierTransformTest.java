package net.slenderloris.fourierandroid;

import org.junit.Test;

import static org.junit.Assert.*;

public class FourierTransformTest {

    @Test
    public void projectAt1Hz() throws Exception {
        float[] points = FourierTransform.project(new float[]{1.0f, 2.0f}, 1.0f, 1.0f);
        assertArrayEquals(new float[]{1.0f, 0.0f, 2.0f, 0.0f}, points, 0.0001f);
    }

    @Test
    public void projectAtHalfHz() throws Exception {
        float[] points = FourierTransform.project(new float[]{1.0f, 2.0f}, 1.0f, 0.5f);
        assertArrayEquals(new float[]{1.0f, 0.0f, -2.0f, 0.0f}, points, 0.0001f);
    }

    @Test
    public void projectAtQuarterHz() throws Exception {
        float[] points = FourierTransform.project(new float[]{1.0f, 2.0f}, 1.0f, 0.25f);
        assertArrayEquals(new float[]{1.0f, 0.0f, 0.0f, 2.0f}, points, 0.0001f);
    }

    @Test
    public void projectAtSampleRate2() throws Exception {
        float[] points = FourierTransform.project(new float[]{1.0f, 2.0f}, 2.0f, 1.0f);
        assertArrayEquals(new float[]{1.0f, 0.0f, -2.0f, 0.0f}, points, 0.0001f);
    }

    @Test
    public void magnitudeOfSummedVectorsAt1Hz() throws Exception {
        float magnitude = FourierTransform.magnitudeOfSummedProjectedVectors(new float[]{1.0f, 2.0f}, 1.0f, 1.0f);
        assertEquals(3.0f, magnitude, 0.0001f);
    }

    @Test
    public void magnitudeOfSummedVectorsAtHalfHz() throws Exception {
        float magnitude = FourierTransform.magnitudeOfSummedProjectedVectors(new float[]{1.0f, 2.0f}, 1.0f, 0.5f);
        assertEquals(1.0f, magnitude, 0.0001f);
    }

    @Test
    public void magnitudeOfSummedVectorsAtOneAndHalfHz() throws Exception {
        float hzArray[] = new float[]{ 1.0f, 0.5f };
        float magnitudes[] = FourierTransform.magnitudesOfSummedProjectedVectors(new float[]{1.0f, 2.0f}, 1.0f, hzArray);
        assertArrayEquals(new float[]{ 3.0f, 1.0f }, magnitudes, 0.0001f);
    }

}
