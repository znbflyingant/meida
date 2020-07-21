package sykent.com.gldemo.pic;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2020/07/19
 */
public class PicGLSurfaceView extends GLSurfaceView {
    private Context mContext;
    private PicRenderer mRenderer;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private volatile boolean isStop;

    public PicGLSurfaceView(Context context) {
        super(context);
        mContext = context;
    }

    public PicGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void init() {
        mRenderer = new PicRenderer(mContext, null);

        setEGLContextClientVersion(2);
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public void start() {
        isStop = false;
        autoRender();
    }

    public void stop() {
        isStop = true;
    }

    private void autoRender() {
        if (!isStop) {
            mHandler.postDelayed(() -> {
                requestRender();
                autoRender();
            }, PicConstant.FRAME_TIME_GAP);
        } else {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void destroy() {
        stop();
        mRenderer.destroy();
    }
}

