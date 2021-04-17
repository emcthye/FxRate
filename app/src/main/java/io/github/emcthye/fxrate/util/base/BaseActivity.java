package io.github.emcthye.fxrate.util.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.github.emcthye.fxrate.AndroidApplication;
import io.github.emcthye.fxrate.util.di.component.ApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    this.getApplicationComponent().inject(this);
  }

  protected ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication) getApplication()).getApplicationComponent();
  }

  protected void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

}
