package com.example.eric.ribbit;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class EditFriendsActivity extends ActionBarActivity {

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected List<ParseUser> mUsers;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_edit_friends);

        //display the action bar.
        //setupActionBar();
    }

    protected ListView getListView() {
        if (mListView == null) {
            mListView = (ListView) findViewById(android.R.id.list);
        }
        return mListView;
    }

    protected ListAdapter getListAdapter() {
        ListAdapter adapter = getListView().getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            return ((HeaderViewListAdapter)adapter).getWrappedAdapter();
        } else {
            return adapter;
        }
    }


    //because onCreate only runs once when the class it called
    //we add onResume so that it will go to this point every time,
    //including after the first time the activity is called
    @Override
    protected void onResume() {
        super.onResume();

        setSupportProgressBarIndeterminateVisibility(true);

        //query will return a list of ParseUser objects
        //ParseQuery is a generic type, so we add the specific type in < >
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        //order according to the column declared in (  )
        query.orderByAscending(ParseConstants.KEY_USERNAME);//from the ParseConstants class I added
        query.setLimit(1000);  //limit search to 1000 users
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {

                setSupportProgressBarIndeterminateVisibility(false);

                if(e == null){
                    // Success
                    mUsers = users;
                    String[] usernames = new String[mUsers.size()];  //create string array of users of size mUsers.size()
                    int i = 0;
                    for(ParseUser user : mUsers){    //for each ParseUser user in mUsers
                        usernames[i] = user.getUsername();   //assign element i that username
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (EditFriendsActivity.this, android.R.layout.simple_list_item_checked,
                                    usernames);
                    // Specifically from the ListActivity class
                    getListView().setAdapter(adapter);
                }
                else{
                    // Error
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);    //button to allow to dismiss message

                    AlertDialog dialog = builder.create();  //creates the dialog from builder
                    dialog.show();
                }
            }
        });
    }

    //private void setupActionBar(){
   //     getActionBar().setDisplayHomeAsUpEnabled(true);
   // }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
