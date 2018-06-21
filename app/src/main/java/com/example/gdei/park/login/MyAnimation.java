package com.example.gdei.park.login;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by gdei on 2018/4/17.
 */

public class MyAnimation extends android.view.animation.Animation {

    int centerX, centerY;   //旋转中心
    //动画持续时间
    int duration;
    int rotate;
    private Camera camera = new Camera();

    public MyAnimation(int duration, int rotate){
        this.duration = duration;
        this.rotate = rotate;

    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        centerY = height / 2;
        centerX = width / 2;
        setDuration(duration);  //设置动画持续时间
        //设置动画结束后是否保留
        setFillAfter(true);
        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        System.out.println("执行动画");
        camera.save();
        camera.rotateY(rotate * (interpolatedTime));
        Matrix matrix = t.getMatrix();
        camera.getMatrix(matrix);
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        camera.restore();
    }
}
