package vip.ruoyun.googleaac.biometric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;

import vip.ruoyun.googleaac.R;


public class BiometricActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    final Executor mExecutor = mHandler::post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);


        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            BiometricPrompt biometricPrompt = new BiometricPrompt(this, mExecutor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    Log.d("zyh", "onAuthenticationError");
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    Log.d("zyh", "onAuthenticationSucceeded:" + result.toString());
                }

                @Override
                public void onAuthenticationFailed() {
                    Log.d("zyh", "onAuthenticationFailed");
                }
            });

            BiometricPrompt.PromptInfo mPromptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("指纹验证")
                    .setSubtitle("内容")
                    .setDescription("描述")
//                    .setNegativeButtonText("取消")
                    .setDeviceCredentialAllowed(true)
                    .build();

            biometricPrompt.authenticate(mPromptInfo);
        }
    }
}
