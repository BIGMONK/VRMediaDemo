package youtu.assetfileread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: " );
        try {
            InputStream inputStream = getAssets().open("tt.txt");
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String tem;
            while ((tem = br.readLine()) != null) {
                Log.e(TAG, "onCreate: " + tem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
