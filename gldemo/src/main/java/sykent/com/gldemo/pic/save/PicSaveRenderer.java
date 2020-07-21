package sykent.com.gldemo.pic.save;

import android.content.Context;

import com.sykent.gl.save.gles.EGLHelper;

import sykent.com.gldemo.pic.GLSaveRendererListener;
import sykent.com.gldemo.pic.PicRenderer;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2020/07/19
 */
public class PicSaveRenderer implements EGLHelper.Renderer {
    private PicRenderer mRenderer;

    public PicSaveRenderer(Context context, GLSaveRendererListener saveListener) {
        mRenderer = new PicRenderer(context, saveListener);
    }

    @Override
    public void onSurfaceCreated() {
        mRenderer.onSurfaceCreated(null, null);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        mRenderer.onSurfaceChanged(null, width, height);
    }

    @Override
    public void onDrawFrame() {
        mRenderer.onDrawFrame(null);
    }

    @Override
    public void onSurfaceDestroy() {
        mRenderer.destroy();
    }
}
