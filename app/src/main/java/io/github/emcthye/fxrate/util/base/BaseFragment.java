package io.github.emcthye.fxrate.util.base;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

import io.github.emcthye.fxrate.util.di.component.HasComponent;

public abstract class BaseFragment extends Fragment {

  protected void showToastMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }

  protected <C> C getComponent(Class<C> componentType) {
    return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
  }
}
