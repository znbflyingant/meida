package sykent.com.gldemo.pic;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2020/07/20
 */
public interface GLSaveRendererListener {

    void onDrawFrame(long timestamp);

    void onProgress(float progress);

    void onRendererFinish();

    void onEncodeFinish(String path);

    void onCancel();

    class SimpleLayoutGLRendererListener implements GLSaveRendererListener {

        @Override
        public void onDrawFrame(long timestamp) {
            // no-op by default
        }

        @Override
        public void onProgress(float progress) {
            // no-op by default
        }

        @Override
        public void onRendererFinish() {
            // no-op by default
        }

        @Override
        public void onEncodeFinish(String path) {
            // no-op by default
        }

        @Override
        public void onCancel() {
            // no-op by default
        }
    }
}
