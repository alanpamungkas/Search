package com.example.githubusersearch.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.githubusersearch.Application;
import com.example.githubusersearch.R;
import com.example.githubusersearch.networks.interfaces.GithubAPI;
import com.example.githubusersearch.models.SearchUserResponse;
import com.example.githubusersearch.networks.REST_Controller;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityRepository {

    private static MainActivityRepository newsRepository;
    private static MutableLiveData<SearchUserResponse> userData;
    private GithubAPI newsApi;
    private boolean isValid;


    public static MainActivityRepository getInstance(){
        if (newsRepository == null){
            newsRepository = new MainActivityRepository();
        }

        return newsRepository;
    }

    public static MutableLiveData<SearchUserResponse> getUserMutable(){
        if (userData==null){
            userData = new MutableLiveData<>();
        }

        return userData;
    }

    public MainActivityRepository(){
        newsApi = REST_Controller.create(GithubAPI.class);
    }

    public MutableLiveData<SearchUserResponse> getUser(String keywords, String page){
        newsApi.getUserList(keywords, page, "50").enqueue(new Callback<SearchUserResponse>() {
                    @Override
                    public void onResponse(Call<SearchUserResponse> call,
                                           Response<SearchUserResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getUsers().size() == 0) {
                                errorHandle(Application.getAppContext()
                                        .getString(page.equals("1") ?
                                                R.string.no_result : R.string.end_of_result));
                                return;
                            }

                            if (page.equals("1")) {
                                userData.setValue(response.body());
                            } else {
                                SearchUserResponse searchUserResponse = userData.getValue();
                                searchUserResponse.getUsers().addAll(response.body().getUsers());

                                userData.setValue(searchUserResponse);
                            }
                        } else {
                            errorHandle(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                        errorHandle(t.getMessage());
                    }
                });

        return userData;
    }

    private void errorHandle(String message){
        SearchUserResponse fail = new SearchUserResponse();
        fail.setMessage(message);
        userData.setValue(fail);
    }
}
