package com.budgetfirst.financialapp.presenter.locker;

public interface LockerContract {

    interface View {
        void setViewsByBinding();
        void setListenersForEditText();
        void showKeyBoard();
        void isPinIsAlreadyExist();
    }

    interface Presenter {
        int getCodeForLocker();
        boolean isCodeForLockerInDatabase();
        void saveCodeForLockerInDatabase(int code);
        void deleteCodeForLockerFromDatabase(int code);
    }
}
