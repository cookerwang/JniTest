CAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := myTestJni
LOCAL_CFLAGS := null -Wno-error=format-security
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \

LOCAL_SRC_FILES := \

LOCAL_C_INCLUDES += 
LOCAL_C_INCLUDES += 

include $(BUILD_SHARED_LIBRAR
