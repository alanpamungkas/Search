package com.example.githubusersearch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.githubusersearch.models.SearchUserResponse;
import com.example.githubusersearch.repositories.MainActivityRepository;
import com.example.githubusersearch.utils.Utils;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<SearchUserResponse> searchUserResponseData;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MainActivityRepository mainActivityRepository;

    private int page = 1;
    private String keywords = "";

    public void init(){
        if (searchUserResponseData != null){
            return;
        }
        mainActivityRepository = MainActivityRepository.getInstance();
        searchUserResponseData = MainActivityRepository.getUserMutable();
    }

    public void goSearch(String keywords){
        if (Utils.isEmptyString(keywords))
            return;

        isLoading.setValue(true);
        page = 1;
        this.keywords = keywords;
        searchUserResponseData = mainActivityRepository.getUser(keywords, String.valueOf(page));
    }

    public void loadMore(){
        if (Utils.isEmptyString(keywords))
            return;

        isLoading.setValue(true);
        page++;
        searchUserResponseData = mainActivityRepository.getUser(keywords, String.valueOf(page));
    }

    public LiveData<SearchUserResponse> getUsers() {
        return searchUserResponseData;
    }

    public LiveData<Boolean> getIsLoading(){
        return isLoading;
    }
}
