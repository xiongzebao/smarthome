package com.erongdu.wireless.views.refreshFooter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.erongdu.wireless.views.R;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 16/11/9 下午3:50
 * <p/>
 * Description: twitter刷新底部
 */
public class TwitterLoadMoreFooterView extends SwipeLoadMoreFooterLayout {
    private TextView    tvLoadMore;
    private ImageView   ivSuccess;
    private ProgressBar progressBar;
    private int         mFooterHeight;

    public TwitterLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public TwitterLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwitterLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height_twitter);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvLoadMore = (TextView) findViewById(R.id.tvLoadMore);
        ivSuccess = (ImageView) findViewById(R.id.ivSuccess);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    public void onPrepare() {
        ivSuccess.setVisibility(GONE);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            ivSuccess.setVisibility(GONE);
            progressBar.setVisibility(GONE);
            if (-y >= mFooterHeight) {
                tvLoadMore.setText(getContext().getString(R.string.release_to_load_more));
            } else {
                tvLoadMore.setText(getContext().getString(R.string.swipe_to_load_more));
            }
        }
    }

    @Override
    public void onLoadMore() {
        tvLoadMore.setText(getContext().getString(R.string.loading_more));
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        progressBar.setVisibility(GONE);
        ivSuccess.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
        ivSuccess.setVisibility(GONE);
    }
}
