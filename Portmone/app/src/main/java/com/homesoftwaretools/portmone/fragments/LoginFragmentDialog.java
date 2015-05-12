package com.homesoftwaretools.portmone.fragments;/*
 * Created by Wild on 09.05.2015.
 */

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.activities.JournalActivity;
import com.homesoftwaretools.portmone.domain.User;
import com.homesoftwaretools.portmone.rest.ApiController;
import com.homesoftwaretools.portmone.rest.PortmoneApi;
import com.homesoftwaretools.portmone.security.AuthorithationManager;
import com.homesoftwaretools.portmone.tasks.RefreshFromServerTask;
import com.homesoftwaretools.portmone.utils.FormValidator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginFragmentDialog extends DialogFragment {

    EditText nameEditText;
    EditText passwordEditText;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_form, container, false);
        nameEditText = (EditText) v.findViewById(R.id.nameEditText);
        passwordEditText = (EditText) v.findViewById(R.id.passwordEditText);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        v.findViewById(R.id.loginButton).setOnClickListener(new LoginButtonClickListener());
        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setRetainInstance(true);
        return dialog;
    }

    private class LoginButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (validForm()) {
                logIn();
            }
        }
    }

    private void logIn() {
        PortmoneApi api = ApiController.getInstance(getActivity()).getApi();
        String login = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        api.getProfile(login, password, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                progressBar.setVisibility(View.GONE);
                AuthorithationManager.getInstance(getActivity()).login(user);
                JournalActivity.show(getActivity());
                dismiss();
                getActivity().finish();
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Ошибка при входе: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validForm() {
        FormValidator validator = new FormValidator()
                .notEmpty(nameEditText, "Укажите имя")
                .notEmpty(passwordEditText, "Не пропускайте пароль");
        return validator.isValid();
    }
}
