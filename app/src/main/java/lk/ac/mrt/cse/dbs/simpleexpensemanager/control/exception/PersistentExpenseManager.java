package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.app.Application;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.SimpleExpenseManagerApplication;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;


/**
 * Created by Sugirjan on 2016-11-20.
 */

public class PersistentExpenseManager extends ExpenseManager {
    private SimpleExpenseManagerApplication simpleExpenseManagerApplication;

    public PersistentExpenseManager(SimpleExpenseManagerApplication simpleExpenseManagerApplication){
        this.simpleExpenseManagerApplication = simpleExpenseManagerApplication;
        try {
            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setup() throws ExpenseManagerException {
        /*** Begin generating dummy data for Persistent implementation ***/
        TransactionDAO persistentTransactionDAO = simpleExpenseManagerApplication.getPersistentTransactionDAO();
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = simpleExpenseManagerApplication.getPersistentAccountDAO();
        setAccountsDAO(persistentAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }
}
