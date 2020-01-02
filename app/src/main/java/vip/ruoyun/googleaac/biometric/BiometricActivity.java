package vip.ruoyun.googleaac.biometric;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.mattprecious.swirl.SwirlView;

import vip.ruoyun.googleaac.R;


public class BiometricActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);
        mButton = findViewById(R.id.mButton);
        SwirlView mSwirlView = findViewById(R.id.mSwirlView);
        BiometricManager biometricManager = BiometricManager.from(this);
        BiometricHelper biometricHelper = new BiometricHelper(this, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                Log.d("zyh", "onAuthenticationError");
                mSwirlView.setState(SwirlView.State.ERROR);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                mSwirlView.setState(SwirlView.State.OFF);
                Log.d("zyh", "onAuthenticationSucceeded:" + result.getCryptoObject());
            }

            @Override
            public void onAuthenticationFailed() {
                mSwirlView.setState(SwirlView.State.ERROR);
                Log.d("zyh", "onAuthenticationFailed");
            }
        });
        mButton.setOnClickListener(v -> {
            if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricHelper.authenticate();
                mSwirlView.setState(SwirlView.State.ON);
            } else {
                Toast.makeText(v.getContext(), "没有相关设备", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class BiometricHelper {

        private final BiometricPrompt biometricPrompt;
        private final BiometricPrompt.PromptInfo mPromptInfo;

        public BiometricHelper(FragmentActivity context, BiometricPrompt.AuthenticationCallback callback) {
            biometricPrompt = new BiometricPrompt(context, ContextCompat.getMainExecutor(context), callback);
            mPromptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("指纹验证")
                    .setSubtitle("内容")
                    .setDescription("描述")
//                    .setNegativeButtonText("取消")
                    .setDeviceCredentialAllowed(true)//是否可以使用其他的验证方式
//                    .setConfirmationRequired(true)
                    .build();
        }

        public void authenticate() {
            biometricPrompt.authenticate(mPromptInfo);
        }
    }
}
