#include <jni.h>
#include <string>
#include <vector>
#include <math.h>

std::vector<float> fourierTransform(const jfloat *samples, jsize numSamples);
std::vector<float> fourierProject(jfloat *samples, jsize numSamples, jfloat rate, jfloat hz);

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

extern "C"
JNIEXPORT jfloatArray JNICALL
Java_net_slenderloris_fourierandroid_FourierTransform_projectAtHz(JNIEnv *env, jclass type, jfloatArray samples_, jfloat rate, jfloat hz) {
    auto *samples = env->GetFloatArrayElements(samples_, NULL);
    auto numSamples = env->GetArrayLength(samples_);
    std::vector<float> projectedPairs = fourierProject(samples, numSamples, rate, hz);
    auto pArray = env->NewFloatArray((jsize) projectedPairs.size());
    env->SetFloatArrayRegion(pArray, 0, (jsize) projectedPairs.size(), projectedPairs.data());
    env->ReleaseFloatArrayElements(samples_, samples, 0);
    return pArray;
}