package vip.ruoyun.googleaac.java;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import vip.ruoyun.googleaac.R;
import vip.ruoyun.googleaac.databinding.ActivityAacBinding;

public class AacActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAacBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_aac);


        UserModel viewModel = new ViewModelProvider(this).get(UserModel.class);
        viewModel.getUser().observe(this, user -> {
//            binding. =
            binding.setUser(viewModel);
        });


    }
}
