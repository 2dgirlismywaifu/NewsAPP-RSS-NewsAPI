/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notmiyouji.newsapp.java.activity.userlogin;

import static com.notmiyouji.newsapp.java.retrofit.NewsAppApi.getAPIClient;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.model.UserInformation;
import com.notmiyouji.newsapp.kotlin.model.VerifyNickName;
import com.notmiyouji.newsapp.kotlin.sharedsettings.SaveUserLogined;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class UpdateInformation {
    private final String userId;
    private final AppCompatActivity activity;
    NewsAppInterface newsAPPInterface = getAPIClient().create(NewsAppInterface.class);

    public UpdateInformation(String userId, AppCompatActivity activity) {
        this.activity = activity;
        this.userId = userId;
    }

    public void updateAvatar(String avatar) {
        Call<UserInformation> call = newsAPPInterface.updateUserInformation
                (userId, "", "", "", "", avatar);
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveBirthday(response.body().getBirthday());
                        Toast.makeText(activity, R.string.update_avatar_image_success, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateBirthday(String birthday) {
        Call<UserInformation> call = newsAPPInterface.updateUserInformation
                (userId, "", "", birthday, "", "");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveBirthday(response.body().getBirthday());
                        Toast.makeText(activity, R.string.birthday_updated, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.birthday_updated_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateFullName(String fullName) {
        Call<UserInformation> call = newsAPPInterface.updateUserInformation
                (userId, "", fullName, "", "", "");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveBirthday(response.body().getBirthday());
                        Toast.makeText(activity, R.string.fullname_updated, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.fullname_already_exists, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateGender(String gender) {
        //Retrofit call update gender request
        Call<UserInformation> call = newsAPPInterface.updateUserInformation
                (userId, "", "", "", gender, "");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveGender(response.body().getGender());
                        Toast.makeText(activity, R.string.gender_updated, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.gender_updated_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkUserName(String userName, String email) {
        Call<VerifyNickName> call = newsAPPInterface.verifyNickName(userName, email);
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerifyNickName> call, @NonNull Response<VerifyNickName> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (Objects.equals(response.body().getNickname(), userName)) {
                            Toast.makeText(activity, R.string.username_already_exists, Toast.LENGTH_SHORT).show();
                        } else {
                            updateUserName(userName);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyNickName> call, @NonNull Throwable t) {
            }
        });
    }

    public void updateUserName(String userName) {
        //Retrofit call update username request
        Call<UserInformation> call = newsAPPInterface.updateUserInformation
                (userId, userName, "", "", "", "");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveUsername(response.body().getUserName());
                        Toast.makeText(activity, R.string.username_updated, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.username_already_exists, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void updatePassword(String email, String newsPass) {
        //Retrofit call update password request
        Call<UserInformation> updatePasswordNow = newsAPPInterface.changeUserToken(userId, email, newsPass);
        assert updatePasswordNow != null;
        updatePasswordNow.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserInformation> call, @NonNull retrofit2.Response<UserInformation> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(activity, R.string.password_updated, Toast.LENGTH_SHORT).show();
                        //Update password in firebase
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(newsPass);
                        //Sign out
                        FirebaseAuth.getInstance().signOut();
                        SaveUserLogined saveUserLogined = new SaveUserLogined(activity);
                        saveUserLogined.saveUserLogined("", "", "", "", "", "", "");
                        saveUserLogined.saveBirthday("");
                        saveUserLogined.saveGender("");
                        //Restart application
                        Toast.makeText(activity, R.string.sign_out_success, Toast.LENGTH_SHORT).show();
                        Intent intent = activity.getBaseContext().getPackageManager().getLaunchIntentForPackage(
                                activity.getBaseContext().getPackageName());
                        if (intent != null) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, R.string.password_updated_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInformation> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.password_updated_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}