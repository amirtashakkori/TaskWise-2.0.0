package com.example.taskwise;

public interface BasePresentor<T extends BaseView> {
    void onAttach(T view);
    void onDetach();
}
