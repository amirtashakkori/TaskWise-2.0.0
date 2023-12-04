package com.example.taskwise.Event;

public class EventDetailPresentor implements EventDetailContract.presentor {

    EventDetailContract.view view;

    @Override
    public void onAttach(EventDetailContract.view view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        this.view = null;
    }
}
