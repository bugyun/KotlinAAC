package vip.ruoyun.googleaac.java;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import vip.ruoyun.googleaac.R;
import vip.ruoyun.googleaac.databinding.ActivityThreeBinding;

public class ThreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityThreeBinding activityThreeBinding = DataBindingUtil.setContentView(this, R.layout.activity_three);
    }
}
