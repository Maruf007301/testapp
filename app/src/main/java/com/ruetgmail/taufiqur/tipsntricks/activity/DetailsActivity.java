package com.ruetgmail.taufiqur.tipsntricks.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdView;
import com.ruetgmail.taufiqur.tipsntricks.R;
import com.ruetgmail.taufiqur.tipsntricks.data.constant.AppConstant;
import com.ruetgmail.taufiqur.tipsntricks.listeners.WebListener;
import com.ruetgmail.taufiqur.tipsntricks.models.content.Contents;
import com.ruetgmail.taufiqur.tipsntricks.utility.AdsUtilities;
import com.ruetgmail.taufiqur.tipsntricks.utility.AppUtilities;
import com.ruetgmail.taufiqur.tipsntricks.utility.TtsEngine;
import com.ruetgmail.taufiqur.tipsntricks.webengine.WebEngine;

public class DetailsActivity extends BaseActivity {
    private Activity mActivity;
    private Context mContext;

    private Contents mModel;
    private ImageView mPostImage;
    private TextView mTitleText;
    private FloatingActionButton mFab;

    private TtsEngine mTtsEngine;
    private boolean mIsTtsPlaying = false;
    private String mTtsText;
    private MenuItem menuItemTTS;

    private Bitmap bitmap;

    private WebView mWebView;
    private WebEngine mWebEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();
        initListener();
    }

    private void initVar() {
        mActivity = DetailsActivity.this;
        mContext = mActivity.getApplicationContext();

        Intent intent = getIntent();
        if (intent != null) {
            mModel = intent.getParcelableExtra(AppConstant.BUNDLE_KEY_ITEM);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_details);

        mPostImage = (ImageView) findViewById(R.id.post_img);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mFab = (FloatingActionButton) findViewById(R.id.share_post);

        initWebEngine();

        initLoader();
        initToolbar(false);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(Html.fromHtml(mModel.getTitle()));

        enableUpButton();
    }

    public void initWebEngine() {

        mWebView = (WebView) findViewById(R.id.web_view);

        mWebEngine = new WebEngine(mWebView, mActivity);
        mWebEngine.initWebView();


        mWebEngine.initListeners(new WebListener() {
            @Override
            public void onStart() {
                showLoader();
            }

            @Override
            public void onLoaded() {
                hideLoader();
            }

            @Override
            public void onProgress(int progress) {
            }

            @Override
            public void onNetworkError() {
                showEmptyView();
            }

            @Override
            public void onPageTitle(String title) {
            }
        });
    }

    private void initFunctionality() {

        mTtsEngine = new TtsEngine(mActivity);

        String imgUrl = mModel.getImageUrl();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .into(mPostImage);
        }

        getBitmap();

        mTitleText.setText(Html.fromHtml(mModel.getTitle()));

        mTtsText = new StringBuilder(Html.fromHtml(mModel.getTitle())).append(Html.fromHtml(mModel.getDetails())).toString();
        mWebEngine.loadHtml(mModel.getDetails());

        // show full-screen ads
        AdsUtilities.getInstance(mContext).showFullScreenAd();
        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));
    }

    private void initListener() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = mActivity.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mModel.getTitle() + mModel.getDetails())
                        + AppConstant.EMPTY_STRING
                        + mActivity.getResources().getString(R.string.share_text)
                        + " https://play.google.com/store/apps/details?id=" + appPackageName);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menus_read_article:
                if (mModel != null) {
                    toggleTtsPlay();
                }
                return true;
            case R.id.menus_copy_text:
                if (mModel != null) {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", Html.fromHtml(mModel.getTitle() + AppConstant.EMPTY_STRING + AppConstant.EMPTY_STRING + mModel.getDetails()));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menus_download_image:
                if (mModel != null) {
                    AppUtilities.downloadFile(mContext, mActivity, bitmap);
                }
                break;
            case R.id.menus_set_image:
                if (mModel != null) {
                    try {
                        WallpaperManager wm = WallpaperManager.getInstance(mContext);
                        wm.setBitmap(bitmap);
                        Toast.makeText(mActivity, getString(R.string.wallpaper_set), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mActivity, getString(R.string.wallpaper_set_failed), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void toggleTtsPlay() {
        if (mIsTtsPlaying) {
            mTtsEngine.releaseEngine();
            mIsTtsPlaying = false;
        } else {
            mTtsEngine.startEngine(mTtsText);
            mIsTtsPlaying = true;
        }
        toggleTtsView();
    }

    private void toggleTtsView() {
        if (mIsTtsPlaying) {
            menuItemTTS.setTitle(R.string.site_menu_stop_reading);
        } else {
            menuItemTTS.setTitle(R.string.read_post);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTtsEngine.releaseEngine();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTtsEngine.releaseEngine();
        mModel = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mIsTtsPlaying) {
            mIsTtsPlaying = false;
            menuItemTTS.setTitle(R.string.read_post);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);

        menuItemTTS = menu.findItem(R.id.menus_read_article);

        return true;
    }

    public void getBitmap() {
        Glide.with(mContext)
                .asBitmap()
                .load(mModel.getImageUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        bitmap = resource;
                    }
                });
    }

}
