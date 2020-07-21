package sykent.com.gldemo.pic;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.sykent.gl.GLSimpleLayer;
import com.sykent.gl.utils.GLMatrixUtils;
import com.sykent.gl.utils.GLUtilsEx;
import com.sykent.imagedecode.EBitmapFactory;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static sykent.com.gldemo.pic.PicConstant.FRAME_TIME_GAP;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2020/07/19
 */
public class PicRenderer implements GLSurfaceView.Renderer {
    public String TAG = this.getClass().getSimpleName();
    private int[] mPicTextureIds = new int[5];
    public Context mContext;
    private int mWidth, mHeight;
    private GLSimpleLayer mPicLayer;

    private int mIndex;
    private int mFrameCount;
    private GLSaveRendererListener mSaveListener;

    public PicRenderer(Context context) {
        this(context, null);
    }

    public PicRenderer(Context context, GLSaveRendererListener saveListener) {
        mContext = context;
        mSaveListener = saveListener;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mPicLayer = new GLSimpleLayer(mContext);
        createTextures();
    }

    private void createTextures() {
        for (int i = 0; i < 5; i++) {
            Bitmap bitmap = EBitmapFactory.decode(mContext, "pic/scenery/test" + i + ".jpg");
            int texture = GLUtilsEx.createTexture(bitmap, false, true);
            if (texture != GLUtilsEx.NO_TEXTURE) {
                mPicTextureIds[i] = texture;
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        GLES20.glViewport(0, 0, width, height);
        mPicLayer.setProjectOrtho(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d(TAG,"onDrawFrame");
        switchPic();
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mPicLayer.onDraw(mPicTextureIds[mIndex],
                mPicLayer.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());

        mFrameCount++;

        // 保存才有
        if (mSaveListener != null) {
            // 画完一帧
            mSaveListener.onDrawFrame(
                    mFrameCount * FRAME_TIME_GAP * 1000);

            // 渲染完毕
            if (mFrameCount == 250) {
                GLES20.glFinish();
                mSaveListener.onRendererFinish();
            }
        }
    }

    private void switchPic() {
        if (mFrameCount != 0 && mFrameCount % PicConstant.SWITCH_FRAME == 0) {
            mIndex = (mIndex + 1) % mPicTextureIds.length;
        }
    }

    public void destroy() {
        mPicLayer.destroy();
    }
}
