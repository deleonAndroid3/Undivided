package com.training.android.undivided.GroupSender.Module.Message_List;

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

public class MessagePresenter implements MessageContract.Presenter {

    private MessageContract.View mView;

    public MessagePresenter(MessageContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void queryMessage() {
        Observable.create(new ObservableOnSubscribe<List<Message>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Message>> e) throws Exception {
                MessageManager manager = MessageManager.getInstance();
                List<Message> messages = manager.getMessages();
                e.onNext(messages);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Message>>() {
                    @Override
                    public void accept(@NonNull List<Message> messages) throws Exception {
                        if (messages.isEmpty()) {
                            mView.setEmptyViewState(true);
                        } else {
                            mView.setEmptyViewState(false);
                            mView.showMessages(messages);
                        }
                    }
                });


    }

    @Override
    public void removeMessage(Message message) {
        MessageManager manager = MessageManager.getInstance();
        manager.delMessage(message);
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }
}
