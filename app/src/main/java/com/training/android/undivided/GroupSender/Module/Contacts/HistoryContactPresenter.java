package com.training.android.undivided.GroupSender.Module.Contacts;

import android.support.annotation.NonNull;

import com.training.android.undivided.GroupSender.DB.message.MessageManager;
import com.training.android.undivided.GroupSender.Objects.Message;
import java.util.List;
import io.reactivex.functions.Consumer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class HistoryContactPresenter implements HistoryContactContract.Presenter {



    private HistoryContactContract.View mView;

    public HistoryContactPresenter(HistoryContactContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void loadHistoryContact() {
        mView.setLoadingIndicator(true);
        Observable.create(new ObservableOnSubscribe<List<Message>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Message>> e) throws Exception {
                List<Message> list = MessageManager.getInstance().getMessages();
                e.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Message>>() {
                    @Override
                    public void accept(@NonNull List<Message> messages) throws Exception {
                        mView.setLoadingIndicator(false);
                        if (messages.isEmpty()) {
                            mView.showEmpty(true);
                        } else {
                            mView.showEmpty(false);
                            mView.showHistoryContactList(messages);
                        }
                    }
                });
    }
    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }
}
