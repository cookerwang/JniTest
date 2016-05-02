package cn.kutec.hellojni;

/**
 * Created by wangrenxing on 16/4/24.
 */
public class JniTest {
    static {
        System.loadLibrary("myTestJni");
    }

    public native String getStringFromNative(String fromJavaString);
}
