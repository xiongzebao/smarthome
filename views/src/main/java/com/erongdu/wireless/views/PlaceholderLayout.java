package com.erongdu.wireless.views;

import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2017/1/5 15:57
 * <p/>
 * Description: 占位图
 */
@SuppressWarnings("unused")
public class PlaceholderLayout extends FrameLayout {
    /**
     * 成功
     */
    public final static int SUCCESS = 0;
    /**
     * 空页
     */
    public final static int EMPTY = 1;
    /**
     * 错误
     */
    public final static int ERROR = 2;
    /**
     * 无网络
     */
    public final static int NO_NETWORK = 3;
    /**
     * 加载中
     */
    public final static int LOADING = 4;

    private int state;
    private Context mContext;
    private View loadingPage;
    private View errorPage;
    private View emptyPage;
    private View networkPage;
    private View defineLoadingPage;
    private ImageView errorImg;
    private ImageView emptyImg;
    private ImageView networkImg;
    private TextView errorText;
    private TextView emptyText;
    private TextView networkText;
    private TextView errorReloadBtn;
    private TextView networkReloadBtn;
    /**
     * 点击重试 - 回调接口
     */
    private OnReloadListener listener;
    /**
     * 是否一开始显示 contentView，默认不显示
     */
    private boolean isFirstVisible;
    // 配置
    private static Config mConfig = new Config();
    private static String emptyStr = "暂无数据~";
    private static String errorStr = "加载失败，请稍后重试···";
    private static String networkStr = "无网络连接，请检查网络···";
    private static String reloadBtnStr = "点击重试";
//        private static int    emptyImgId      = R.drawable.placeholder_empty;
    private static int emptyImgId = R.drawable.noun_ico_xx3;
    private static int errorImgId = R.drawable.placeholder_error;
    private static int networkImgId = R.drawable.placeholder_network;
    private static int reloadBtnId = R.drawable.selector_btn_back_gray;
    private static int tipTextSize = 14;
    private static int buttonTextSize = 14;
    private static int tipTextColor = R.color.placeholder_text_color;
    private static int buttonTextColor = R.color.placeholder_text_color;
    private static int buttonWidth = -1;
    private static int buttonHeight = -1;
    private static int loadingLayoutId = R.layout.widget_loading_page;
    private static int backgroundColor = R.color.white;
    private NoDoubleClickButton nbApplyBtn;

    public PlaceholderLayout(Context context) {
        super(context);
        init(context);
    }

    public PlaceholderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlaceholderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        // TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
        // isFirstVisible = a.getBoolean(R.styleable.LoadingLayout_isFirstVisible, false);
        // a.recycle();
    }

    private void init(Context context) {
        this.mContext = context;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        build();
    }

    private void build() {
        loadingPage = LayoutInflater.from(mContext).inflate(loadingLayoutId, this, false);
        errorPage = LayoutInflater.from(mContext).inflate(R.layout.widget_error_page, this, false);
        emptyPage = LayoutInflater.from(mContext).inflate(R.layout.widget_empty_page, this, false);
        networkPage = LayoutInflater.from(mContext).inflate(R.layout.widget_nonetwork_page, this, false);
        defineLoadingPage = null;

        loadingPage.setBackgroundColor(ContextCompat.getColor(mContext, backgroundColor));
        errorPage.setBackgroundColor(ContextCompat.getColor(mContext, backgroundColor));
        emptyPage.setBackgroundColor(ContextCompat.getColor(mContext, backgroundColor));
        networkPage.setBackgroundColor(ContextCompat.getColor(mContext, backgroundColor));

        errorText = (TextView) errorPage.findViewById(R.id.error_text);
        emptyText = (TextView) emptyPage.findViewById(R.id.empty_text);
        networkText = (TextView) networkPage.findViewById(R.id.no_network_text);

        errorImg = (ImageView) errorPage.findViewById(R.id.error_img);
        emptyImg = (ImageView) emptyPage.findViewById(R.id.empty_img);
        networkImg = (ImageView) networkPage.findViewById(R.id.no_network_img);

        errorReloadBtn = (TextView) errorPage.findViewById(R.id.error_reload_btn);
        networkReloadBtn = (TextView) networkPage.findViewById(R.id.no_network_reload_btn);
        nbApplyBtn = (NoDoubleClickButton)emptyPage.findViewById(R.id.nb_apply);
        nbApplyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onApply(v);
                }
            }
        });
        errorReloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReload(v);
                }
            }
        });
        networkReloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReload(v);
                }
            }
        });

        errorText.setText(errorStr);
        emptyText.setText(emptyStr);
        networkText.setText(networkStr);

        errorText.setTextSize(tipTextSize);
        emptyText.setTextSize(tipTextSize);
        networkText.setTextSize(tipTextSize);

        errorText.setTextColor(ContextCompat.getColor(mContext, tipTextColor));
//        emptyText.setTextColor(ContextCompat.getColor(mContext, tipTextColor));
        networkText.setTextColor(ContextCompat.getColor(mContext, tipTextColor));

        errorImg.setImageResource(errorImgId);
        emptyImg.setImageResource(emptyImgId);
        networkImg.setImageResource(networkImgId);

        errorReloadBtn.setBackgroundResource(reloadBtnId);
        networkReloadBtn.setBackgroundResource(reloadBtnId);

        errorReloadBtn.setText(reloadBtnStr);
        networkReloadBtn.setText(reloadBtnStr);

        errorReloadBtn.setTextSize(buttonTextSize);
        networkReloadBtn.setTextSize(buttonTextSize);

        errorReloadBtn.setTextColor(ContextCompat.getColor(mContext, buttonTextColor));
        networkReloadBtn.setTextColor(ContextCompat.getColor(mContext, buttonTextColor));

        if (buttonHeight != -1) {
            errorReloadBtn.setHeight(dp2px(mContext, buttonHeight));
            networkReloadBtn.setHeight(dp2px(mContext, buttonHeight));
        }
        if (buttonWidth != -1) {
            errorReloadBtn.setWidth(dp2px(mContext, buttonWidth));
            networkReloadBtn.setWidth(dp2px(mContext, buttonWidth));
        }

        this.addView(networkPage);
        this.addView(emptyPage);
        this.addView(errorPage);
        this.addView(loadingPage);
    }

    public void setStatus(@Flavour int status) {
        this.state = status;
        setVisibility(View.VISIBLE);

        switch (status) {
            case SUCCESS:
                emptyPage.setVisibility(View.GONE);
                errorPage.setVisibility(View.GONE);
                networkPage.setVisibility(View.GONE);
                if (defineLoadingPage != null) {
                    defineLoadingPage.setVisibility(View.GONE);
                } else {
                    loadingPage.setVisibility(View.GONE);
                }
                setVisibility(View.GONE);
                break;

            case LOADING:
                emptyPage.setVisibility(View.GONE);
                errorPage.setVisibility(View.GONE);
                networkPage.setVisibility(View.GONE);
                if (defineLoadingPage != null) {
                    defineLoadingPage.setVisibility(View.VISIBLE);
                } else {
                    loadingPage.setVisibility(View.VISIBLE);
                }
                break;

            case EMPTY:
                emptyPage.setVisibility(View.VISIBLE);
                errorPage.setVisibility(View.GONE);
                networkPage.setVisibility(View.GONE);
                if (defineLoadingPage != null) {
                    defineLoadingPage.setVisibility(View.GONE);
                } else {
                    loadingPage.setVisibility(View.GONE);
                }
                break;

            case ERROR:
                loadingPage.setVisibility(View.GONE);
                emptyPage.setVisibility(View.GONE);
                errorPage.setVisibility(View.VISIBLE);
                networkPage.setVisibility(View.GONE);
                if (defineLoadingPage != null) {
                    defineLoadingPage.setVisibility(View.GONE);
                } else {
                    loadingPage.setVisibility(View.GONE);
                }
                break;

            case NO_NETWORK:
                loadingPage.setVisibility(View.GONE);
                emptyPage.setVisibility(View.GONE);
                errorPage.setVisibility(View.GONE);
                networkPage.setVisibility(View.VISIBLE);
                if (defineLoadingPage != null) {
                    defineLoadingPage.setVisibility(View.GONE);
                } else {
                    loadingPage.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 返回当前状态{SUCCESS, EMPTY, ERROR, NO_NETWORK, LOADING}
     */
    public int getStatus() {
        return state;
    }

    /**
     * 设置Empty状态提示文本，仅对当前所在的地方有效
     */
    public PlaceholderLayout setEmptyText(String text) {
        emptyText.setText(text);
        return this;
    }

    /**
     * 设置Empty状态提示文本，仅对当前所在的地方有效
     */
    public PlaceholderLayout setEmptyButtonVisibility(int visibility) {
        nbApplyBtn.setVisibility(visibility);
        return this;
    }

    /**
     * 设置Empty状态提示文本，仅对当前所在的地方有效
     */
    public PlaceholderLayout setEmptyButtonText(String text) {
        nbApplyBtn.setText(text);
        return this;
    }

    /**
     * 设置Error状态提示文本，仅对当前所在的地方有效
     */
    public PlaceholderLayout setErrorText(String text) {
        errorText.setText(text);
        return this;
    }

    /**
     * 设置No_Network状态提示文本，仅对当前所在的地方有效
     */
    public PlaceholderLayout setNoNetworkText(String text) {
        networkText.setText(text);
        return this;
    }

    /**
     * 设置Empty状态显示图片，仅对当前所在的地方有效
     */
    public PlaceholderLayout setEmptyImage(@DrawableRes int id) {
        emptyImg.setImageResource(id);
        return this;
    }

    /**
     * 设置Error状态显示图片，仅对当前所在的地方有效
     */
    public PlaceholderLayout setErrorImage(@DrawableRes int id) {
        errorImg.setImageResource(id);
        return this;
    }

    /**
     * 设置No_Network状态显示图片，仅对当前所在的地方有效
     */
    public PlaceholderLayout setNoNetworkImage(@DrawableRes int id) {
        networkImg.setImageResource(id);
        return this;
    }

    /**
     * 设置Empty状态提示文本的字体大小，仅对当前所在的地方有效
     */
    public PlaceholderLayout setEmptyTextSize(int sp) {
        emptyText.setTextSize(sp);
        return this;
    }

    /**
     * 设置Error状态提示文本的字体大小，仅对当前所在的地方有效
     */
    public PlaceholderLayout setErrorTextSize(int sp) {
        errorText.setTextSize(sp);
        return this;
    }

    /**
     * 设置No_Network状态提示文本的字体大小，仅对当前所在的地方有效
     */
    public PlaceholderLayout setNoNetworkTextSize(int sp) {
        networkText.setTextSize(sp);
        return this;
    }

    /**
     * 设置Empty状态图片的显示与否，仅对当前所在的地方有效
     */
    public PlaceholderLayout setEmptyImageVisible(boolean bool) {
        if (bool) {
            emptyImg.setVisibility(View.VISIBLE);
        } else {
            emptyImg.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置Error状态图片的显示与否，仅对当前所在的地方有效
     */
    public PlaceholderLayout setErrorImageVisible(boolean bool) {
        if (bool) {
            errorImg.setVisibility(View.VISIBLE);
        } else {
            errorImg.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置No_Network状态图片的显示与否，仅对当前所在的地方有效
     */
    public PlaceholderLayout setNoNetworkImageVisible(boolean bool) {
        if (bool) {
            networkImg.setVisibility(View.VISIBLE);
        } else {
            networkImg.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置ReloadButton的文本，仅对当前所在的地方有效
     */
    public PlaceholderLayout setReloadButtonText(@NonNull String text) {
        errorReloadBtn.setText(text);
        networkReloadBtn.setText(text);
        return this;
    }

    /**
     * 设置ReloadButton的文本字体大小，仅对当前所在的地方有效
     */
    public PlaceholderLayout setReloadButtonTextSize(int sp) {
        errorReloadBtn.setTextSize(sp);
        networkReloadBtn.setTextSize(sp);
        return this;
    }

    /**
     * 设置ReloadButton的文本颜色，仅对当前所在的地方有效
     */
    public PlaceholderLayout setReloadButtonTextColor(@ColorRes int id) {
        errorReloadBtn.setTextColor(ContextCompat.getColor(mContext, id));
        networkReloadBtn.setTextSize(ContextCompat.getColor(mContext, id));
        return this;
    }

    /**
     * 设置ReloadButton的背景，仅对当前所在的地方有效
     */
    public PlaceholderLayout setReloadButtonBackgroundResource(@DrawableRes int id) {
        errorReloadBtn.setBackgroundResource(id);
        networkReloadBtn.setBackgroundResource(id);
        return this;
    }

    /**
     * 设置ReloadButton的监听器
     */
    public PlaceholderLayout setOnReloadListener(OnReloadListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 自定义加载页面，仅对当前所在的Activity有效
     */
    public PlaceholderLayout setLoadingPage(View view) {
        defineLoadingPage = view;
        this.removeView(loadingPage);
        defineLoadingPage.setVisibility(View.GONE);
        this.addView(view);
        return this;
    }

    /**
     * 自定义加载页面，仅对当前所在的地方有效
     */
    public PlaceholderLayout setLoadingPage(@LayoutRes int id) {
        this.removeView(loadingPage);
        View view = LayoutInflater.from(mContext).inflate(id, null);
        defineLoadingPage = view;
        defineLoadingPage.setVisibility(View.GONE);
        this.addView(view);
        return this;
    }

    /**
     * 获取当前自定义的loadingPage
     */
    public View getLoadingPage() {
        return defineLoadingPage;
    }

    /**
     * 获取全局使用的loadingPage
     */
    public View getGlobalLoadingPage() {
        return loadingPage;
    }

    /**
     * 获取全局配置的class
     */
    public static Config getConfig() {
        return mConfig;
    }

    /**
     * DP 转 PX
     */
    public static int dp2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    @IntDef({SUCCESS, EMPTY, ERROR, NO_NETWORK, LOADING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flavour {
    }

    public interface OnReloadListener {
        void onReload(View v);
        void onApply(View v);
    }

    /**
     * 全局配置的Class，对所有使用到的地方有效
     */
    public static class Config {
        public Config setErrorText(@NonNull String text) {
            errorStr = text;
            return mConfig;
        }

        public Config setEmptyText(@NonNull String text) {
            emptyStr = text;
            return mConfig;
        }

        public Config setNoNetworkText(@NonNull String text) {
            networkStr = text;
            return mConfig;
        }

        public Config setReloadButtonText(@NonNull String text) {
            reloadBtnStr = text;
            return mConfig;
        }

        /**
         * 设置所有提示文本的字体大小
         */
        public Config setAllTipTextSize(int sp) {
            tipTextSize = sp;
            return mConfig;
        }

        /**
         * 设置所有提示文本的字体颜色
         */
        public Config setAllTipTextColor(@ColorRes int color) {
            tipTextColor = color;
            return mConfig;
        }

        public Config setReloadButtonTextSize(int sp) {
            buttonTextSize = sp;
            return mConfig;
        }

        public Config setReloadButtonTextColor(@ColorRes int color) {
            buttonTextColor = color;
            return mConfig;
        }

        public Config setReloadButtonBackgroundResource(@DrawableRes int id) {
            reloadBtnId = id;
            return mConfig;
        }

        public Config setReloadButtonWidthAndHeight(int width_dp, int height_dp) {
            buttonWidth = width_dp;
            buttonHeight = height_dp;
            return mConfig;
        }

        public Config setErrorImage(@DrawableRes int id) {
            errorImgId = id;
            return mConfig;
        }

        public Config setEmptyImage(@DrawableRes int id) {
            emptyImgId = id;
            return this;
        }

        public Config setNoNetworkImage(@DrawableRes int id) {
            networkImgId = id;
            return mConfig;
        }

        public Config setLoadingPageLayout(@LayoutRes int id) {
            loadingLayoutId = id;
            return mConfig;
        }

        public Config setAllPageBackgroundColor(@ColorRes int color) {
            backgroundColor = color;
            return mConfig;
        }
    }
}
