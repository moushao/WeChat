package com.tw.wechat.widget.photo.observer;

/**
 * 类名: {@link PhotoImageObservable}
 * <br/> 功能描述:观察者
 */
public class PhotoAdapterObservable extends PhotoImageObservable<PhotoBaseDataObserver> {

    public void notifyChanged() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }

    public void notifyInvalidated() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onInvalidated();
            }
        }
    }
}
