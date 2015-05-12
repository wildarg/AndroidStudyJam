package com.homesoftwaretools.portmone.fragments;

/*
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
import com.homesoftwaretools.portmone.domain.User;
import com.homesoftwaretools.portmone.rest.ApiController;
import com.homesoftwaretools.portmone.rest.PortmoneApi;
import com.homesoftwaretools.portmone.utils.FormValidator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignInFragmentDialog extends DialogFragment {

    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText password2EditText;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sign_in_form, container, false);
        nameEditText = (EditText) v.findViewById(R.id.nameEditText);
        emailEditText = (EditText) v.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) v.findViewById(R.id.passwordEditText);
        password2EditText = (EditText) v.findViewById(R.id.password2EditText);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        v.findViewById(R.id.signinButton).setOnClickListener(new SignInButtonClickListener());
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

    private class SignInButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (validForm()) {
                signIn();
            }
        }
    }

    private void signIn() {
        PortmoneApi api = ApiController.getInstance(getActivity()).getApi();
        User user = new User(nameEditText.getText().toString(), emailEditText.getText().toString(),
                passwordEditText.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        api.createUser(user, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Пользователь успешно добавлен", Toast.LENGTH_SHORT).show();
                dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Ошибка при регистрации: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validForm() {
        FormValidator validator = new FormValidator()
                .notEmpty(nameEditText, "Укажите имя")
                .notEmpty(emailEditText, "Задайте e-mail")
                .isValidEmail(emailEditText, "Данная строка не похожа на e-mail адрес")
                .notEmpty(passwordEditText, "Не пропускайте пароль")
                .notEmpty(password2EditText, "Введите еще раз пароль для проверки")
                .equals(passwordEditText, password2EditText, "Очень жаль, но пароли не совпадают");
        return validator.isValid();
    }
}
