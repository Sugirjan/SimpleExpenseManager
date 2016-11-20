package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.app.Application;

import java.sql.SQLException;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
/**
 * Created by Sugirjan on 2016-11-20.
 */

public class SimpleExpenseManagerApplication extends Application {
    private PersistentAccountDAO persistentAccountDAO;
    private PersistentTransactionDAO persistentTransactionDAO;

    @Override
    public void onCreate() {
        super.onCreate();

        persistentAccountDAO = new PersistentAccountDAO(getApplicationContext());
        persistentTransactionDAO = new PersistentTransactionDAO(getApplicationContext());
        try {
            persistentAccountDAO.open();
            persistentTransactionDAO.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PersistentAccountDAO getPersistentAccountDAO() {
        return persistentAccountDAO;
    }

    public PersistentTransactionDAO getPersistentTransactionDAO() {
        return persistentTransactionDAO;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        persistentAccountDAO.close();
        persistentTransactionDAO.close();
    }
}
