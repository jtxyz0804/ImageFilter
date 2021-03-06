package com.example.imageblur;

import static javax.microedition.khronos.egl.EGL10.EGL_ALPHA_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_BLUE_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_DEFAULT_DISPLAY;
import static javax.microedition.khronos.egl.EGL10.EGL_DEPTH_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_GREEN_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_HEIGHT;
import static javax.microedition.khronos.egl.EGL10.EGL_NONE;
import static javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT;
import static javax.microedition.khronos.egl.EGL10.EGL_RED_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_STENCIL_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_WIDTH;
import static javax.microedition.khronos.opengles.GL10.GL_RGBA;
import static javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_BYTE;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class PixelBuffer {
    final static String TAG = "PixelBuffer";
    final static boolean LIST_CONFIGS = false;

    GLSurfaceView.Renderer mRenderer; // borrow this interface
    int mWidth, mHeight;
    Bitmap mBitmap;
        
    EGL10 mEGL; 
    EGLDisplay mEGLDisplay;
    EGLConfig[] mEGLConfigs;
    EGLConfig mEGLConfig;
    EGLContext mEGLContext;
    EGLSurface mEGLSurface;
    GL10 mGL;
    //使用OPENGL2.0关键部分
    int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    int[] attrib_list = { EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE };
    String mThreadOwner;
    Bitmap bitmap;
    
    public void setBitmapSrc(Bitmap bitmap){
    	this.bitmap = bitmap;
    }
    
    public PixelBuffer(int width, int height) {
        mWidth = width;
        mHeight = height;
                
        int[] version = new int[2];
        int[] attribList = new int[] {
            EGL_WIDTH, mWidth,
            EGL_HEIGHT, mHeight,
            EGL_NONE
        };
                
        // No error checking performed, minimum required code to elucidate logic
        mEGL = (EGL10) EGLContext.getEGL();
        mEGLDisplay = mEGL.eglGetDisplay(EGL_DEFAULT_DISPLAY);
        mEGL.eglInitialize(mEGLDisplay, version);
        mEGLConfig = chooseConfig(); // Choosing a config is a little more complicated
        mEGLContext = mEGL.eglCreateContext(mEGLDisplay, mEGLConfig, EGL_NO_CONTEXT, attrib_list);
        mEGLSurface = mEGL.eglCreatePbufferSurface(mEGLDisplay, mEGLConfig,  attribList);
        mEGL.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext);
        mGL = (GL10) mEGLContext.getGL();
        
        // Record thread owner of OpenGL context
        mThreadOwner = Thread.currentThread().getName();
    }
    
    public void setRenderer(GLSurfaceView.Renderer renderer) {
        mRenderer = renderer;
        
        // Does this thread own the OpenGL context?
        if (!Thread.currentThread().getName().equals(mThreadOwner)) {
            Log.e(TAG, "setRenderer: This thread does not own the OpenGL context.");
            return;
        }
        
        // Call the renderer initialization routines
        mRenderer.onSurfaceCreated(mGL, mEGLConfig);
        mRenderer.onSurfaceChanged(mGL, mWidth, mHeight);
    }
        
    public Bitmap getBitmap() {
        // Do we have a renderer?
        if (mRenderer == null) {
            Log.e(TAG, "getBitmap: Renderer was not set.");
            return null;
        }
        
        // Does this thread own the OpenGL context?
        if (!Thread.currentThread().getName().equals(mThreadOwner)) {
            Log.e(TAG, "getBitmap: This thread does not own the OpenGL context.");
            return null;
        }
                
        // Call the renderer draw routine
        mRenderer.onDrawFrame(mGL);
        convertToBitmap();
        return mBitmap;
    }

    private EGLConfig chooseConfig() {
        int[] attribList = new int[] {                  
            EGL_DEPTH_SIZE, 0,
            EGL_STENCIL_SIZE, 0,
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_ALPHA_SIZE, 8,
            EGL_NONE
        };

        // No error checking performed, minimum required code to elucidate logic
        // Expand on this logic to be more selective in choosing a configuration
        int[] numConfig = new int[1];
        mEGL.eglChooseConfig(mEGLDisplay, attribList, null, 0, numConfig);
        int configSize = numConfig[0];
        mEGLConfigs = new EGLConfig[configSize];
        mEGL.eglChooseConfig(mEGLDisplay, attribList, mEGLConfigs, configSize, numConfig);

        if (LIST_CONFIGS) {
            listConfig();
        }

        return mEGLConfigs[0];  // Best match is probably the first configuration
    }
    
    private void listConfig() {
        Log.i(TAG, "Config List {");

        for (EGLConfig config : mEGLConfigs) {
            int d, s, r, g, b, a;
                    
            // Expand on this logic to dump other attributes        
            d = getConfigAttrib(config, EGL_DEPTH_SIZE);
            s = getConfigAttrib(config, EGL_STENCIL_SIZE);
            r = getConfigAttrib(config, EGL_RED_SIZE);
            g = getConfigAttrib(config, EGL_GREEN_SIZE);
            b = getConfigAttrib(config, EGL_BLUE_SIZE);
            a = getConfigAttrib(config, EGL_ALPHA_SIZE);                
            Log.i(TAG, "    <d,s,r,g,b,a> = <" + d + "," + s + "," + 
                r + "," + g + "," + b + "," + a + ">");
        }

        Log.i(TAG, "}");
    }
        
    private int getConfigAttrib(EGLConfig config, int attribute) {
        int[] value = new int[1];
        return mEGL.eglGetConfigAttrib(mEGLDisplay, config,
                        attribute, value)? value[0] : 0;
    }
        
    private void convertToBitmap() {
    	 Log.i("jiangtao4", "convertToBitmap mHeight is : " + mHeight);
        IntBuffer ib = IntBuffer.allocate(mWidth*mHeight);
        IntBuffer ibt = IntBuffer.allocate(mWidth*mHeight);
        mGL.glReadPixels(0, 0, mWidth, mHeight, GL_RGBA, GL_UNSIGNED_BYTE, ib);

        // Convert upside down mirror-reversed image to right-side up normal image.
        for (int i = 0; i < mHeight; i++) {     
            for (int j = 0; j < mWidth; j++) {
                ibt.put((mHeight-i-1)*mWidth + j, ib.get(i*mWidth + j));
            }
        }                  

        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mBitmap.copyPixelsFromBuffer(ibt);
    }
    
    public Bitmap getBitmap(Bitmap inBitmap, IntBuffer buffer) {
		// Do we have a renderer?
		if (mRenderer == null) {
			Log.e(TAG, "getBitmap: Renderer was not set.");
			return null;
		}

		// Does this thread own the OpenGL context?
		if (!Thread.currentThread().getName().equals(mThreadOwner)) {
			Log.e(TAG, "getBitmap: This thread does not own the OpenGL context.");
			return null;
		}

		// Call the renderer draw routine
		mRenderer.onDrawFrame(mGL);

		// copy pixels from OpenGL
		if (buffer == null || buffer.capacity() != mWidth * mHeight) {
			buffer = IntBuffer.allocate(mWidth * mHeight);
		}

		mGL.glReadPixels(0, 0, mWidth, mHeight, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		// vertical mirror image
		int[] swapArray = new int[mWidth];
		int[] array = buffer.array();
		for (int i = 0; i < mHeight / 2; i++) {
			System.arraycopy(array, i * mWidth, swapArray, 0, mWidth);
			System.arraycopy(array, (mHeight - i - 1) * mWidth, array, i * mWidth, mWidth);
			System.arraycopy(swapArray, 0, array, (mHeight - i - 1) * mWidth, mWidth);
		}

		// copy to Bitmap
		if (inBitmap == null || !inBitmap.isMutable() || inBitmap.getWidth() != mWidth || inBitmap.getHeight() != mHeight) {
			inBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
		}

		inBitmap.copyPixelsFromBuffer(buffer);

		return inBitmap;
	}

}
