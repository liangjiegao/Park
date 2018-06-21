package com.example.gdei.park.bootpage;

/**
 * Created by gdei on 2018/4/13.
 */

public class BootPagePresenter implements BootPageContract.Presenter{

    private BootPageFragment bootPageFragment;      //presenter持有view

    public BootPagePresenter(BootPageFragment bootPageFragment){
        this.bootPageFragment = bootPageFragment;
    }
    @Override
    public void start() {

    }

    @Override
    public void enterHomePage() {

    }

    @Override
    public void nextPage() {

    }

    @Override
    public void lightDotMoveToNextPosition() {
        bootPageFragment.lightDotMoveToNextPosition();
    }

    @Override
    public void changeViewByClickBreakDot() {
        bootPageFragment.changeViewByClickBreakDot();
    }

    @Override
    public void enterMainPage() {
        bootPageFragment.enterMainPage();
    }
}
