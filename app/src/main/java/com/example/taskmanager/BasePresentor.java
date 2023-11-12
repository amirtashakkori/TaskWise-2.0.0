package com.example.taskmanager;

public interface BasePresentor<T extends BaseView> {
    void onAttach(T view);
    void onDetach();
}
