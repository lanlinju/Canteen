package com.example.canteen.utilities;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 解决liveData 订阅的黏性问题
 * @param <T>
 */
public class NonStickyMutableLiveData <T> extends MutableLiveData {

    private boolean stickFlag = false;

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
        hook(observer);
        super.observe(owner, observer);

//        if (!stickFlag){

//        }
    }

    private void hook(Observer<? super T> observer){
//        1. 得到mLastVersion
//        获取listdata 中的mObservers 对象

        try {
            Class<LiveData> liveDataClass = LiveData.class;
            Field mObserverField = liveDataClass.getDeclaredField("mObservers");
            mObserverField.setAccessible(true);
//            获取到这个成员变量的对象
            Object mObserverObject = mObserverField.get(this);
//            得到map 对应的class 对象
            Class<?> mObserverClass = mObserverObject.getClass();
//            获取到mObserver 对应的get方法
            Method get = mObserverClass.getDeclaredMethod("get", Object.class);
            get.setAccessible(true);
//            执行get 方法
            Object invokeEntry = get.invoke(mObserverObject, observer);
//            定义一个空的对象
            Object observerWraper = null;
            if (invokeEntry != null && invokeEntry instanceof Map.Entry){
                observerWraper = ((Map.Entry) invokeEntry).getKey();
            }
            if (observerWraper == null){
                throw new NullPointerException("observerWraper is null");
            }
//            得到observerWraper的类对象 编译擦除问题会引起多态冲突所以用getSuperClass
            Class<?> superClass = observerWraper.getClass().getSuperclass();
            Field mLastVersion = superClass.getDeclaredField("mLastVersion");
            mLastVersion.setAccessible(true);
//            2. 得到mVersion
            Field mVersion = liveDataClass.getDeclaredField("mVersion");
            mLastVersion.setAccessible(true);
//            3.把mVersion的数据填入到LastVersion中
            Object mVersionValue = mVersion.get(this);
            mLastVersion.set(observerWraper, mVersionValue);

            stickFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

