package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sugirjan on 2016-11-20.
 */

public class DBContentProvider extends SQLiteOpenHelper {
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String BANK_NAME = "bank_name";
    public static final String ACCOUNT_HOLDER_NAME = "account_holder_name";
    public static final String BALANCE = "balance";

    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String DATE= "date";
    //account
    public static final String AMOUNT= "amount";
    public static final String EXPENSE_TYPE = "expense_type";


    private static final String DATABASE_NAME = "140607F.db";
    private static final int DATABASE_VERSION = 6;

    // Database creation sql statement
    private static final String CREATE_ACCOUNTS = "create table IF NOT EXISTS "
            + TABLE_ACCOUNTS + "(" + ACCOUNT_NUMBER
            + " TEXT primary key , " + BANK_NAME
            + " TEXT not null, " + ACCOUNT_HOLDER_NAME
            + " TEXT not null, " + BALANCE
            + " REAL check("+BALANCE+">= 0));";

    private static final String CREATE_TRANSACTIONS = "create table IF NOT EXISTS "
            + TABLE_TRANSACTIONS + "(" + DATE
            + " TEXT not null , " + ACCOUNT_NUMBER
            + " TEXT not null, " + AMOUNT
            + " REAL check("+AMOUNT+">= 0), " + EXPENSE_TYPE
            + " TEXT not null);";

    public DBContentProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_ACCOUNTS);
        database.execSQL(CREATE_TRANSACTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("","Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL(CREATE_TRANSACTIONS);
    }
}