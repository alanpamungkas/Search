package com.example.githubusersearch;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubusersearch.adapters.SearchUserAdapter;
import com.example.githubusersearch.databinding.ActivityMainBinding;
import com.example.githubusersearch.models.User;
import com.example.githubusersearch.utils.Utils;
import com.example.githubusersearch.viewmodels.MainActivityViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    private MainActivityViewModel mMainActivityViewModel;
    private LinearLayoutManager linearLayoutManager;

    private SearchUserAdapter searchUserAdapter;
    private List<User> userList = new ArrayList<>();

    private long lastEditTime = 0;
    private long delay = 1500;
    private Handler type = new Handler();

    private Runnable finishedTyping = () -> {
        if (System.currentTimeMillis() > (lastEditTime + delay)){
            goSearch();
        }
    };

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isProgressing())
                return;

            int visibleItem = linearLayoutManager.getChildCount();
            int totalItem = linearLayoutManager.getItemCount();
            int pastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

            if (pastVisibleItem + visibleItem >= totalItem - 8) {
                mMainActivityViewModel.loadMore();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init();

        initRecyclerView();
        initDoneTyping();
        initObservable();
    }

    private void initObservable() {
        mMainActivityViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading)
                showProgressBar();
            else
                hideProgressBar();
        });

        mMainActivityViewModel.getUsers().observe(this, searchUserResponse -> {
            hideProgressBar();

            if (searchUserResponse.getMessage()==null){
                if (searchUserResponse.getTotalCount()==0){
                    dataErrorHandle("No Result ");
                    return;
                }

                userList.clear();
                userList.addAll(searchUserResponse.getUsers());
                searchUserAdapter.notifyDataSetChanged();
            } else {
                dataErrorHandle(searchUserResponse.getMessage());
            }
        });
    }

    private void initDoneTyping() {
        activityMainBinding.etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                type.removeCallbacks(finishedTyping);
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                lastEditTime = System.currentTimeMillis();
                type.postDelayed(finishedTyping, delay);
            }
        });

        activityMainBinding.etSearchUser.setOnEditorActionListener((v, actionId, event) -> {
            type.removeCallbacks(finishedTyping);
            goSearch();
            return false;
        });
    }

    private void initRecyclerView(){
        activityMainBinding.rvSearchUser.addOnScrollListener(onScrollListener);

        linearLayoutManager = new LinearLayoutManager(this);
        activityMainBinding.rvSearchUser.setLayoutManager(linearLayoutManager);

        DividerItemDecoration horizontalDivider =
                new DividerItemDecoration(activityMainBinding.rvSearchUser.getContext(),
                linearLayoutManager.getOrientation());
        activityMainBinding.rvSearchUser.addItemDecoration(horizontalDivider);

        searchUserAdapter = new SearchUserAdapter(this, userList);
        activityMainBinding.rvSearchUser.setAdapter(searchUserAdapter);
    }

    private void goSearch(){
        Utils.hideSoftKeyboard(this);
        activityMainBinding.rvSearchUser.addOnScrollListener(onScrollListener);
        mMainActivityViewModel.goSearch(activityMainBinding.etSearchUser.getText().toString().trim());
    }

    private void dataErrorHandle(String message){
        activityMainBinding.rvSearchUser.clearOnScrollListeners();
        Snackbar.make(findViewById(R.id.rlMainActivity), message, Snackbar.LENGTH_LONG).show();
    }

    private void hideProgressBar() {
        activityMainBinding.pbSearchUser.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        activityMainBinding.pbSearchUser.setVisibility(View.VISIBLE);
    }

    private boolean isProgressing() {
        return activityMainBinding.pbSearchUser.getVisibility() == View.VISIBLE;
    }
}