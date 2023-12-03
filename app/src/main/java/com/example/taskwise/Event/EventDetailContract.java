package com.example.taskwise.Event;

import com.example.taskwise.BasePresentor;
import com.example.taskwise.BaseView;

public interface EventDetailContract {
    interface view extends BaseView{

    }

    interface presentor extends BasePresentor<view> {

    }
}
