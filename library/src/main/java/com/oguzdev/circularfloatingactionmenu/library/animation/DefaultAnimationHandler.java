/*
 *   Copyright 2014 Oguz Bilgener
 */
package com.oguzdev.circularfloatingactionmenu.library.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Point;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

/**
 * An example animation handler
 * Animates translation, rotation, scale and alpha at the same time using Property Animation APIs.
 */
public class DefaultAnimationHandler extends MenuAnimationHandler {

    /** duration of animations, in milliseconds */
    protected static final int DURATION = 500;
    /** duration to wait between each of  */
    protected static final int LAG_BETWEEN_ITEMS = 20;
    /** holds the current state of animation */
    private boolean animating;

    public DefaultAnimationHandler() {
        setAnimating(false);
    }

    @Override
    public void animateMenuOpening(Point center) {
        super.animateMenuOpening(center);

        setAnimating(true);

        Animator lastAnimation = null;
        for (int i = 0; i < menu.getSubActionItems().size(); i++) {


            //menu.getSubActionItems().get(i).view.setTranslationX(menu.getActionViewRadius());
            menu.getSubActionItems().get(i).view.setAlpha(1);
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, menu.getSubActionItems().get(i).x - center.x + menu.getSubActionItems().get(i).width / 2);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, menu.getSubActionItems().get(i).y - center.y + menu.getSubActionItems().get(i).height / 2);
            PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 360);

            final ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(menu.getSubActionItems().get(i).view, pvhX, pvhY, pvhR);
            animation.setDuration(DURATION);
            animation.setInterpolator(new OvershootInterpolator(0.9f));
            animation.addListener(new SubActionItemAnimationListener(menu.getSubActionItems().get(i), ActionType.OPENING));

            if(i == 0) {
                lastAnimation = animation;
            }

            // Put a slight lag between each of the menu items to make it asymmetric
            animation.setStartDelay((menu.getSubActionItems().size() - i) * LAG_BETWEEN_ITEMS);
            animation.start();
        }
        if(lastAnimation != null) {
            lastAnimation.addListener(new LastAnimationListener());
        }

    }

    @Override
    public void animateMenuClosing(Point center) {
        super.animateMenuOpening(center);

        setAnimating(true);

        Animator lastAnimation = null;
        for (int i = 0; i < menu.getSubActionItems().size(); i++) {
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, - (menu.getSubActionItems().get(i).x - center.x + menu.getSubActionItems().get(i).width / 2));
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, - (menu.getSubActionItems().get(i).y - center.y + menu.getSubActionItems().get(i).height / 2));
            PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);

            final ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(menu.getSubActionItems().get(i).view, pvhX, pvhY, pvhR);
            animation.setDuration(DURATION);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.addListener(new SubActionItemAnimationListener(menu.getSubActionItems().get(i), ActionType.CLOSING));

            if(i == 0) {
                lastAnimation = animation;
            }

            animation.setStartDelay((menu.getSubActionItems().size() - i) * LAG_BETWEEN_ITEMS);
            animation.start();
        }
        if(lastAnimation != null) {
            lastAnimation.addListener(new LastAnimationListener());
        }
    }

    @Override
    public boolean isAnimating() {
        return animating;
    }

    @Override
    protected void setAnimating(boolean animating) {
        this.animating = animating;
    }

    protected class SubActionItemAnimationListener implements Animator.AnimatorListener {

        private FloatingActionMenu.Item subActionItem;
        private ActionType actionType;

        public SubActionItemAnimationListener(FloatingActionMenu.Item subActionItem, ActionType actionType) {
            this.subActionItem = subActionItem;
            this.actionType = actionType;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            restoreSubActionViewAfterAnimation(subActionItem, actionType);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            restoreSubActionViewAfterAnimation(subActionItem, actionType);
        }

        @Override public void onAnimationRepeat(Animator animation) {}
    }
}
