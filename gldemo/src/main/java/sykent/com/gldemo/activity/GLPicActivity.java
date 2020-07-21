package sykent.com.gldemo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sykent.UIRun;
import com.sykent.framework.activity.BaseActivity;
import com.sykent.utils.ToastUtils;
import com.sykent.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import sykent.com.gldemo.R;
import sykent.com.gldemo.pic.GLSaveRendererListener;
import sykent.com.gldemo.pic.PicGLSurfaceView;
import sykent.com.gldemo.pic.save.PicVideoSaveRunnable;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2020/07/19
 */
public class GLPicActivity extends BaseActivity {

    @BindView(R.id.pgsv_show)
    PicGLSurfaceView mGLTextureView;

    private PicVideoSaveRunnable mSaveRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListener();
    }

    @Override
    public void initView() {
        super.initView();

        // 设置标题
        ((TextView) findViewById(R.id.normal_title_caption)).setText("GL 图片渲染");

        ViewGroup.LayoutParams layoutParams = mGLTextureView.getLayoutParams();
        layoutParams.height = Utils.getScreenWidth();
        mGLTextureView.setLayoutParams(layoutParams);
        mGLTextureView.init();
//        mGLTextureView.start();
    }

    @OnClick({R.id.normal_back_icon,
            R.id.bt_save_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.normal_back_icon:
                finish();
                break;

            case R.id.bt_save_video:
                mSaveRunnable = new PicVideoSaveRunnable(this,
                        640, 640,
                        new GLSaveRendererListener.SimpleLayoutGLRendererListener() {
                            @Override
                            public void onEncodeFinish(String path) {
                                mSaveRunnable = null;
                                UIRun.post(() -> ToastUtils.showToast(
                                        GLPicActivity.this, "保存路径：" + path));
                            }
                        });
                new Thread(mSaveRunnable).start();
                break;
        }
    }

    private void initListener() {

    }

    @Override
    public int provideTitleViewLayoutResID() {
        return R.layout.normal_title;
    }

    @Override
    public int provideContentViewLayoutResID() {
        return R.layout.activity_pic_layout;
    }

    @Override
    protected void onDestroy() {
        mGLTextureView.destroy();
        super.onDestroy();
    }
}
