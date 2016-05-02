//
// Created by wangrenxing on 16/5/2.
//

#include "TestLog.h"
#include <stdio.h>
#include <android/log.h>

#define TAG "TEST"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

void TestLog::printLog(const char *tag, const char *msg) {
    __android_log_print((int)ANDROID_LOG_INFO, tag, "printLog begin");
    if( msg != NULL ) {
        __android_log_print((int)ANDROID_LOG_INFO, tag, "%s", msg);
    }
    LOGE("%s", NULL);

    __android_log_print((int)ANDROID_LOG_INFO, tag, "printLog end");
}
