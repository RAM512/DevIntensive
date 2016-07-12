package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

//    @BindView (R.id.login_btn) Button mSignIn;
//    @BindView(R.id.forgot_txt) TextView mForgotPassword;
    @BindView(R.id.login_email_et) EditText mLogin;
    @BindView(R.id.login_password_et) EditText mPassword;
    @BindView(R.id.main_coordinator_layout) CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();
    }

    @Override
    @OnClick({R.id.login_btn, R.id.forgot_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                signIn();
                break;

            case R.id.forgot_txt:
                remindPassword();
                break;
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void remindPassword() {
        Intent remindIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(remindIntent);
    }

    private void loginSuccsess(UserModelRes userModel) {
        showSnackBar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        saveUserFields(userModel);
        saveUserImages(userModel);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(
                    mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccsess(response.body());
                    } else if (response.code() == 403) {
                        showSnackBar("Неверный логин или пароль");
                    } else {
                        showSnackBar("Ошибка " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // TODO: 12.07.2016 обработать ошибки ретрофита
                }
            });
        } else {
            showSnackBar("Сеть недоступна, попробуйте позже");
        }
    }

    private void saveUserValues(UserModelRes userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()
        };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);

    }

    private void saveUserFields(UserModelRes userModel) {
        List<String> userFields = new ArrayList<>();
        UserModelRes.User user = userModel.getData().getUser();
        UserModelRes.Contacts contacts = user.getContacts();

        userFields.add(contacts.getPhone());
        userFields.add(contacts.getEmail());
        userFields.add(contacts.getVk());

        List<UserModelRes.Repo> repos = user.getRepositories().getRepos();
        String gitRepo = null;
        if (repos.size() > 0) {
            gitRepo = repos.get(0).getGit();
        } else {
            gitRepo = "null";
        }
        userFields.add(gitRepo);
        userFields.add(user.getPublicInfo().getBio());

        mDataManager.getPreferencesManager().saveUserProfileData(userFields);

        String fullName = user.getFirstName() + " " + user.getSecondName();
        mDataManager.getPreferencesManager().saveUserName(fullName);
    }

    private void saveUserImages(UserModelRes userModel) {
        // TODO: 13.07.2016 сохранять загруженные изображения на устройстве
        UserModelRes.PublicInfo publicInfo = userModel.getData().getUser().getPublicInfo();
        PreferencesManager pm = mDataManager.getPreferencesManager();
        pm.saveUserPhoto(Uri.parse(publicInfo.getPhoto()));
        pm.saveUserAvatar(Uri.parse(publicInfo.getAvatar()));
    }
}
