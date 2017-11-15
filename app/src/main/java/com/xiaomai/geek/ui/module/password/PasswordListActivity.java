package com.xiaomai.geek.ui.module.password;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.xiaomai.geek.R;
import com.xiaomai.geek.contract.password.PasswordsContract;
import com.xiaomai.geek.data.PasswordRepository;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.presenter.password.PasswordsPresenter;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.ui.widget.ErrorView;
import com.xiaomai.geek.ui.widget.TitleView;

import java.util.List;

/**
 * Created by XiaoMai on 2017/11/7.
 */

public class PasswordListActivity extends BaseActivity implements PasswordsContract.View {

    private PasswordsContract.Presenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ErrorView mErrorView;
    private ErrorView mEmptyView;
    private ImageView mAddView;

    private Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PasswordsPresenter(PasswordRepository.getInstance(mContext));
        mPresenter.attachView(this);

        loadData();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_password_list;
    }

    @Override
    public void initViews() {
        super.initViews();

        TitleView titleView = findViewById(R.id.title_view);
        titleView.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_view);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new Adapter();
        mAdapter.setCallback(new Callback() {
            @Override
            public void onItemClick(Password password) {
                Intent intent = new Intent(mContext, PasswordDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onPublishClick(Password password) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mErrorView = findViewById(R.id.error_view);
        mEmptyView = findViewById(R.id.empty_view);

        mAddView = findViewById(R.id.flb_add);
        mAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddEditPasswordActivity.class));
            }
        });
    }

    private void loadData() {
        mPresenter.loadPasswords();
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dismissLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showContent(List<Password> data) {
        mAdapter.setContent(data);

        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable e) {
        mErrorView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Nullable
        private List<Password> passwords;
        @Nullable
        private Callback callback;
        private final ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

        public void setContent(@Nullable List<Password> passwords) {
            this.passwords = passwords;
            notifyDataSetChanged();
        }

        public void setCallback(@Nullable Callback callback) {
            this.callback = callback;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            Holder holder;
            if (viewHolder instanceof Holder) {
                holder = (Holder) viewHolder;
                final Password password = getItem(position);

                String passwordPlatform = password.getPlatform();

                if (!TextUtils.isEmpty(passwordPlatform)) {
                    TextDrawable textDrawable = TextDrawable.builder()
                            .beginConfig()
                            .toUpperCase()
                            .endConfig()
                            .buildRound(passwordPlatform.substring(0, 1), colorGenerator.getColor(passwordPlatform));
                    holder.iconView.setImageDrawable(textDrawable);
                } else {
                    holder.iconView.setImageDrawable(null);
                }
                holder.platformView.setText(passwordPlatform);
                holder.userNameView.setText(password.getUserName());
                holder.passwordView.setText(password.getPassword());

                holder.publishView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null) {
                            callback.onPublishClick(password);
                        }
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null) {
                            callback.onItemClick(password);
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return passwords == null ? 0 : passwords.size();
        }

        Password getItem(int position) {
            return passwords == null ? null : passwords.get(position);
        }
    }

    private static class Holder extends RecyclerView.ViewHolder {
        ImageView iconView;
        TextView platformView;
        TextView userNameView;
        TextView passwordView;
        View publishView;
        View eyeView;

        private Holder(View itemView) {
            super(itemView);

            iconView = itemView.findViewById(R.id.circle_view_icon);
            platformView = itemView.findViewById(R.id.tv_platform);
            userNameView = itemView.findViewById(R.id.tv_userName);
            passwordView = itemView.findViewById(R.id.tv_password);
            publishView = itemView.findViewById(R.id.iv_publish);
            eyeView = itemView.findViewById(R.id.iv_eye);
        }
    }

    public interface Callback {
        void onItemClick(Password password);

        void onPublishClick(Password password);
    }
}