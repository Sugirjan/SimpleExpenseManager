package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Sugirjan on 2016-11-20.
 */

public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase database;
    private DBContentProvider dbContentProvider;
    private String[] allColumns = {DBContentProvider.ACCOUNT_NUMBER, DBContentProvider.BANK_NAME,
            DBContentProvider.ACCOUNT_HOLDER_NAME, DBContentProvider.BALANCE};

    public PersistentAccountDAO(Context context) {
        dbContentProvider = new DBContentProvider(context);
    }

    public void open() throws SQLException {
        database = dbContentProvider.getWritableDatabase();
    }

    public void close() {
        dbContentProvider.close();
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumberList = new ArrayList<String>();
        String args[] = {String.valueOf(dbContentProvider.ACCOUNT_NUMBER)};

        Cursor cursor = database.query(DBContentProvider.TABLE_ACCOUNTS,args,null,null,null,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            accountNumberList.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<Account>();

        Cursor cursor = database.query(DBContentProvider.TABLE_ACCOUNTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Account account = cursorToAccount(cursor);
            accounts.add(account);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String args[] = {accountNo};
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBContentProvider.TABLE_ACCOUNTS + " WHERE " + DBContentProvider.ACCOUNT_NUMBER + "=?", args);
        cursor.moveToFirst();

        Account account = cursorToAccount(cursor);
        if(account == null){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContentProvider.ACCOUNT_NUMBER,account.getAccountNo());
        contentValues.put(DBContentProvider.ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(DBContentProvider.BANK_NAME,account.getBankName());
        contentValues.put(DBContentProvider.BALANCE, account.getBalance());

        database.insert(DBContentProvider.TABLE_ACCOUNTS, null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String[] args = {accountNo};
        if(getAccount(accountNo) == null){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        database.delete(DBContentProvider.TABLE_ACCOUNTS, DBContentProvider.ACCOUNT_NUMBER
                + " =?", args);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        Double balance = account.getBalance();
        if(expenseType == ExpenseType.EXPENSE){
            balance = balance - amount;
        }else{
            balance = balance + amount;
        }
        if(balance < 0){
            balance = 0.0;
        }
        account.setBalance(balance);
        removeAccount(accountNo);
        addAccount(account);
    }

    private Account cursorToAccount(Cursor cursor){
        String accountName = cursor.getString(0);
        String bankName = cursor.getString(1);
        String accountHolderName = cursor.getString(2);
        Double balance = cursor.getDouble(3);

        return new Account(accountName,bankName,accountHolderName,balance);
    }
}

