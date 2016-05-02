//
// Created by wangrenxing on 16/4/24.
//

#include "cn_kutec_hellojni_JniTest.h"
#include "TestLog.h"
#include <stdio.h>
#include <android/log.h>

#define TAG "TEST"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

static TestLog testLog;

JNIEXPORT jstring JNICALL Java_cn_kutec_hellojni_JniTest_getStringFromNative
  (JNIEnv * env, jobject obj, jstring fromJavaString) {

    const char *str = env->GetStringUTFChars( fromJavaString, NULL);

    //__android_log_print(ANDROID_LOG_INFO, TAG, "JUST test logcat");
    __android_log_print(ANDROID_LOG_INFO, TAG, "JUST test logcat");
    __android_log_print(ANDROID_LOG_INFO, TAG, "Logcat from java: %s", str);
    __android_log_print(ANDROID_LOG_INFO, TAG, "Logcat from java size: %d", env->GetStringUTFLength( fromJavaString));
    __android_log_print(ANDROID_LOG_INFO, TAG, "Logcat from java: %d", env->GetStringLength( fromJavaString));
    testLog.printLog(TAG, str);
    env->ReleaseStringUTFChars( fromJavaString, str);
    return env->NewStringUTF("This just a test for Android Studio NDK JNI developer!");
}
