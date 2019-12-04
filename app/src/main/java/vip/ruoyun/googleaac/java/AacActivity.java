package vip.ruoyun.googleaac.java;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.lifecycle.ViewModelProvider;

import vip.ruoyun.googleaac.R;
import vip.ruoyun.googleaac.databinding.ActivityAacBinding;

public class AacActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAacBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_aac);

        getSupportFragmentManager().setFragmentFactory(new MyFragmentFactory());

//        getSupportFragmentManager().beginTransaction().replace()

        UserModel viewModel = new ViewModelProvider(this).get(UserModel.class);
        viewModel.getUser().observe(this, user -> {
//            binding. =
            binding.setUser(viewModel);
        });


    }


    class MyFragmentFactory extends FragmentFactory {
        @NonNull
        @Override
        public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
            if (className.endsWith("vip.ruoyun.fragment")) {
                return new Fragment();
            }
            return super.instantiate(classLoader, className);
        }
    }
}
