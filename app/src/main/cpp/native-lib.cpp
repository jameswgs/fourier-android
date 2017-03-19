#include <jni.h>
#include <string>
#include <vector>
#include <math.h>

std::vector<float> fourierTransform(const jfloat *samples, jsize numSamples);
std::vector<float> fourierProject(jfloat *samples, jsize numSamples, jfloat rate, jfloat hz);

float magnitudeOfSummedProjectedVectors(jfloat *pDouble, jsize samples, jfloat rate, jfloat hz);

std::vector<float>
magnitudesOfSummedProjectedVectors(float *samples, int numSamples, float rate, float *hzArray,
                                   int numHzBuckets);

extern "C"
JNIEXPORT jfloatArray JNICALL
Java_net_slenderloris_fourierandroid_FourierTransform_fourierTransform(JNIEnv *env, jclass type, jfloatArray samples_) {
    auto *samples = env->GetFloatArrayElements(samples_, NULL);
    auto numSamples = env->GetArrayLength(samples_);
    std::vector<float> frequencies = fourierTransform(samples, numSamples);
    auto pArray = env->NewFloatArray((jsize) frequencies.size());
    env->SetFloatArrayRegion(pArray, 0, (jsize) frequencies.size(), frequencies.data());
    env->ReleaseFloatArrayElements(samples_, samples, 0);
    return pArray;
}

std::vector<float> fourierTransform(const jfloat *samples, jsize numSamples) {
    auto frequencies = std::vector<float>();
    for (int i = 0; i < numSamples; ++i) {
        frequencies.push_back(samples[i] * 2.0f);
    }
    return frequencies;
}

std::vector<float> fourierProject(jfloat *samples, jsize numSamples, jfloat rate, jfloat hz) {
    auto projection = std::vector<float>();
    auto delta = 2.0f*M_PI * ( hz / rate );
    auto angle = 0.0f;
    for (int i = 0; i < numSamples; ++i) {
        float sample = samples[i];
        float cosASample = cosf(angle) * sample;
        float sinASample = sinf(angle) * sample;
        projection.push_back(cosASample);
        projection.push_back(sinASample);
        angle += delta;
    }
    return projection;
}

float magnitudeOfSummedProjectedVectors(float *samples, int numSamples, float rate, float hz) {
    auto projected = fourierProject(samples, numSamples, rate, hz);
    float x = 0;
    float y = 0;
    for (int i = 0; i < numSamples; ++i) {
        x += projected[i*2];
        y += projected[i*2+1];
    }
    return sqrtf(x*x+y*y);
}

std::vector<float> magnitudesOfSummedProjectedVectors(float *samples, int numSamples, float rate, float *hzArray, int numHzBuckets) {
    auto frequencies = std::vector<float>();
    for (int i = 0; i < numHzBuckets; ++i) {
        float hz = hzArray[i];
        float magnitude = magnitudeOfSummedProjectedVectors(samples, numSamples, rate, hz);
        frequencies.push_back(magnitude);
    }
    return frequencies;
}

extern "C"
JNIEXPORT jfloatArray JNICALL
Java_net_slenderloris_fourierandroid_FourierTransform_magnitudesOfSummedProjectedVectors(JNIEnv *env, jclass type, jfloatArray samples_, jfloat rate, jfloatArray hzArray_) {
    auto *samples = env->GetFloatArrayElements(samples_, NULL);
    auto numSamples = env->GetArrayLength(samples_);
    auto *hzArray = env->GetFloatArrayElements(hzArray_, NULL);
    auto numHzBuckets = env->GetArrayLength(hzArray_);

    std::vector<float> magnitudes = magnitudesOfSummedProjectedVectors(samples, numSamples, rate, hzArray, numHzBuckets);

    auto pArray = env->NewFloatArray((jsize) magnitudes.size());
    env->SetFloatArrayRegion(pArray, 0, (jsize) magnitudes.size(), magnitudes.data());

    env->ReleaseFloatArrayElements(samples_, samples, 0);
    env->ReleaseFloatArrayElements(hzArray_, hzArray, 0);

    return pArray;
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_net_slenderloris_fourierandroid_FourierTransform_magnitudeOfSummedProjectedVectors(JNIEnv *env, jclass type, jfloatArray samples_, jfloat rate, jfloat hz) {
    auto *samples = env->GetFloatArrayElements(samples_, NULL);
    auto numSamples = env->GetArrayLength(samples_);
    float magnitude = magnitudeOfSummedProjectedVectors(samples, numSamples, rate, hz);
    env->ReleaseFloatArrayElements(samples_, samples, 0);
    return magnitude;
}

extern "C"
JNIEXPORT jfloatArray JNICALL
Java_net_slenderloris_fourierandroid_FourierTransform_project(JNIEnv *env, jclass type, jfloatArray samples_, jfloat rate, jfloat hz) {
    auto *samples = env->GetFloatArrayElements(samples_, NULL);
    auto numSamples = env->GetArrayLength(samples_);
    std::vector<float> projectedPairs = fourierProject(samples, numSamples, rate, hz);
    auto pArray = env->NewFloatArray((jsize) projectedPairs.size());
    env->SetFloatArrayRegion(pArray, 0, (jsize) projectedPairs.size(), projectedPairs.data());
    env->ReleaseFloatArrayElements(samples_, samples, 0);
    return pArray;
}