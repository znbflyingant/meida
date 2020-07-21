package sykent.com.gldemo.pic.save;

import android.content.Context;

import com.sykent.gl.save.encode.EncodeThread;
import com.sykent.gl.save.encode.EncodeUtils;
import com.sykent.gl.save.encode.VideoEncoderCore;
import com.sykent.gl.save.gles.EGLHelper;
import com.sykent.utils.FileUtils;

import java.io.IOException;

import sykent.com.gldemo.pic.GLSaveRendererListener;
import sykent.com.gldemo.pic.PicConstant;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2020/07/19
 */
public class PicVideoSaveRunnable implements Runnable {
    private static final String ENCODE_PATH = FileUtils.getSDPath()
            + "DCIM/Camera/" + System.currentTimeMillis() + ".mp4";
    ;
    private Context mContext;

    // 合成核心
    private EGLHelper mEGLHelper;
    private PicSaveRenderer mSaveRenderer;
    private EncodeThread mEncodeThread;

    private int mEncodeW, mEncodeH;

    private volatile boolean isStop;
    private volatile boolean isCancel;

    public PicVideoSaveRunnable(Context context,
                                int encodeW, int encodeH,
                                GLSaveRendererListener saveListener) {
        mContext = context;
        mEncodeW = encodeW;
        mEncodeH = encodeH;

        prepare(saveListener);
    }

    private void prepare(GLSaveRendererListener saveListener) {
        mSaveRenderer = new PicSaveRenderer(mContext,
                new GLSaveRendererListener.SimpleLayoutGLRendererListener() {
                    @Override
                    public void onDrawFrame(long timestamp) {
                        mEGLHelper.onSwapBuffers(timestamp);
                        mEncodeThread.encode();
                    }

                    @Override
                    public void onRendererFinish() {
                        isStop = true;
                        if (saveListener != null) {
                            saveListener.onEncodeFinish(ENCODE_PATH);
                        }
                    }
                });

        mEGLHelper = new EGLHelper(mSaveRenderer);
    }

    @Override
    public void run() {
        try {
            guardedRun();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ensureExit();
        }
    }

    private void guardedRun() throws IOException {
        mEncodeW = EncodeUtils.checkEncodeSize(mEncodeW);
        mEncodeH = EncodeUtils.checkEncodeSize(mEncodeH);

        VideoEncoderCore.EncodeConfig config =
                new VideoEncoderCore.EncodeConfig(mEncodeW, mEncodeH, PicConstant.FRAME_RATE, ENCODE_PATH);

        mEncodeThread = new EncodeThread(config);
        new Thread(mEncodeThread).start();

        mEGLHelper.initEGLContext(mEncodeThread.getEncodeSurface());
        mEGLHelper.onSurfaceCreated();
        mEGLHelper.onSurfaceChanged(mEncodeW, mEncodeH);

        onDrawFrame();

        mEGLHelper.onSurfaceDestroy();
    }

    private void onDrawFrame() {
        while (!isStop) {
            mEGLHelper.onDrawFrame();
        }
    }

    private void ensureExit() {
        isStop = true;

        boolean isError = false;
        if (mEncodeThread != null) {
            isError = isError || !mEncodeThread.waitForFinish();
        }
        mEGLHelper.release();
    }
}
