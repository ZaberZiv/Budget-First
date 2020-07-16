package com.budgetfirst.financialapp.presenter.locker;

public interface LockerContract {

    interface View {
        void setViewsByBinding();
        void setListenersForEditText();
        void showKeyBoard();
        void checkIfPinIsAlreadyExist();
    }

    interface Presenter {
        int getCodeForLocker();
        boolean checkIfCodeForLockerInDatabase();
        void saveCodeForLockerInDatabase(int code);
        void deleteCodeForLockerFromDatabase(int code);
    }
}
