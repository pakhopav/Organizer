package com.example.pdaorganizer.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.pdaorganizer.model.Issue
import com.example.pdaorganizer.model.User
import java.util.ArrayList
import javax.sql.StatementEvent

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    // create table sql query
    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER +
            "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USER_NAME + " TEXT," +
            COLUMN_USER_PASSWORD + " TEXT" + ")")



    private val CREATE_ISSUE_TABLE = ("CREATE TABLE " + TABLE_ISSUE + "("
            + COLUMN_ISSUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ISSUE_NAME + " TEXT," + COLUMN_ISSUE_USER + " INTEGER,"+ COLUMN_ISSUE_CATEGORY + " TEXT,"
            + COLUMN_ISSUE_DESCRIPTION + " TEXT," + COLUMN_ISSUE_IMPORTANCE + " TEXT,"
            + COLUMN_ISSUE_DEADLINE + " TEXT" + ")")

    // drop table sql query
    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"
    private val DROP_ISSUE_TABLE = "DROP TABLE IF EXISTS $TABLE_ISSUE"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_ISSUE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_USER_TABLE)
        db.execSQL(DROP_ISSUE_TABLE)
        // Create tables again
        onCreate(db)
    }

    fun getAllIssues(): List<Issue> {
        // array of columns to fetch
        val columns = arrayOf(COLUMN_ISSUE_ID, COLUMN_ISSUE_NAME, COLUMN_ISSUE_USER, COLUMN_ISSUE_CATEGORY, COLUMN_ISSUE_DESCRIPTION, COLUMN_ISSUE_IMPORTANCE,
            COLUMN_ISSUE_DEADLINE)

        val issueList = ArrayList<Issue>()

        // selection criteria

        // selection arguments


        val db = this.readableDatabase
        var sortOrder = "$COLUMN_ISSUE_NAME ASC"
        // query the user table
        val cursor = db.query(
            TABLE_ISSUE, //Table to query
            columns,            //columns to return
            null,    //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order

        if (cursor.moveToFirst()) {
            do {
                val issue = Issue(id = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_NAME)),
                    user = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_USER)).toInt(),
                    category = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_CATEGORY)),
                    description = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_DESCRIPTION)),
                    importance = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_IMPORTANCE)),
                    deadline = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_DEADLINE)))

                issueList.add(issue)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return issueList
    }

    fun getAllIssuesOfUser(userId : Int): List<Issue> {
        // array of columns to fetch
        val columns = arrayOf(COLUMN_ISSUE_ID, COLUMN_ISSUE_NAME, COLUMN_ISSUE_USER, COLUMN_ISSUE_CATEGORY, COLUMN_ISSUE_DESCRIPTION, COLUMN_ISSUE_IMPORTANCE,
            COLUMN_ISSUE_DEADLINE)

        val issueList = ArrayList<Issue>()

        // selection criteria
        val selection = "$COLUMN_ISSUE_USER = ?"

        // selection arguments
        val selectionArgs = arrayOf(userId.toString())


        val db = this.readableDatabase
        var sortOrder = "$COLUMN_ISSUE_NAME ASC"
        // query the user table
        val cursor = db.query(
            TABLE_ISSUE, //Table to query
            columns,            //columns to return
            selection,    //columns for the WHERE clause
            selectionArgs,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order

        if (cursor.moveToFirst()) {
            do {
                val issue = Issue(id = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_NAME)),
                    user = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_USER)).toInt(),
                    category = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_CATEGORY)),
                    description = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_DESCRIPTION)),
                    importance = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_IMPORTANCE)),
                    deadline = cursor.getString(cursor.getColumnIndex(COLUMN_ISSUE_DEADLINE)))

                issueList.add(issue)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return issueList
    }
    fun getAllUser(): List<User> {

        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_PASSWORD)

        // sorting orders
        val sortOrder = "$COLUMN_USER_NAME ASC"
        val userList = ArrayList<User>()

        val db = this.readableDatabase

        // query the user table
        val cursor = db.query(TABLE_USER, //Table to query
            columns,            //columns to return
            null,     //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order
        if (cursor.moveToFirst()) {
            do {
                val user = User(id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)))

                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

    fun addIssue(issue: Issue) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_ISSUE_NAME, issue.name)
        values.put(COLUMN_ISSUE_USER, issue.user)
        values.put(COLUMN_ISSUE_CATEGORY, issue.category)
        values.put(COLUMN_ISSUE_DESCRIPTION, issue.description)
        values.put(COLUMN_ISSUE_IMPORTANCE, issue.importance)
        values.put(COLUMN_ISSUE_DEADLINE, issue.deadline)

        // Inserting Row
        db.insert(TABLE_ISSUE, null, values)
        db.close()
    }

    fun returnUserIfExist(username: String, password: String): User? {

        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_PASSWORD)

        val db = this.readableDatabase

        // selection criteria
        val selection = "$COLUMN_USER_NAME = ? AND $COLUMN_USER_PASSWORD = ?"

        // selection arguments
        val selectionArgs = arrayOf(username, password)

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        val cursor = db.query(TABLE_USER, //Table to query
            columns, //columns to return
            selection, //columns for the WHERE clause
            selectionArgs, //The values for the WHERE clause
            null,  //group the rows
            null, //filter by row groups
            null) //The sort order

        if (cursor.moveToFirst()) {
            val user = User(id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toInt(),
                name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)))
            cursor.close()
            db.close()
            return user


        }else{
            cursor.close()
            db.close()
            return null
        }
    }


    fun addUser(user: User) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_PASSWORD, user.password)

        // Inserting Row
        db.insert(TABLE_USER, null, values)
        db.close()
    }


    fun deleteIssue(issue: Issue) {

        val db = this.writableDatabase
        // delete user record by id
        db.delete(
            TABLE_ISSUE, "$COLUMN_ISSUE_ID = ?",
            arrayOf(issue.id.toString()))
        db.close()


    }

    companion object {
        //Shared preferences
        val SHARED_PREFS ="sharedPrefs"
        val USER_ID = "userId"

        // Database Version
        private val DATABASE_VERSION = 5

        // Database Name
        private val DATABASE_NAME = "Organizer.db"

        //table name
        private val TABLE_USER = "user"
        private val TABLE_ISSUE = "issue"
        // User Table Columns names
        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_PASSWORD = "user_password"
        // Issue Table Column names
        private val COLUMN_ISSUE_ID = "issue_id"
        private val COLUMN_ISSUE_NAME = "issue_name"
        private val COLUMN_ISSUE_USER = "issue_user"
        private val COLUMN_ISSUE_CATEGORY ="issue_category"
        private val COLUMN_ISSUE_IMPORTANCE = "issue_importance"
        private val COLUMN_ISSUE_DESCRIPTION = "issue_description"
        private val COLUMN_ISSUE_DEADLINE = "issue_deadline"




    }


}