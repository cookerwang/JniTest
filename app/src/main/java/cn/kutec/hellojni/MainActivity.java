package cn.kutec.hellojni;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_test1).setOnClickListener(this);
        findViewById(R.id.tv_test2).setOnClickListener(this);
        Toast.makeText(MainActivity.this, new JniTest().getStringFromNative("java string"), Toast.LENGTH_SHORT).show();
    }

    int k;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_test1:
                //startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                new JniTest().getStringFromNative("java string"+(++k));
                break;
            case R.id.tv_test2:
                //startActivity(new Intent(Settings.ACTION_APN_SETTINGS));
                break;
        }
    }
}
