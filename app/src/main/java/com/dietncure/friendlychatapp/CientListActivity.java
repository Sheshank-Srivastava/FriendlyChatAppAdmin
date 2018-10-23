package com.dietncure.friendlychatapp;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class CientListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 55;
    private ArrayList<UserListModel> mdata;
    private UserListAdapter userListAdapter;
    private ArrayList<UserListModel> unread, read;
    private ArrayList<Integer> new1;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cient_list);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("overview").child("8");
        findViewById(R.id.butToMainActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CientListActivity.this, MainActivity.class);
                intent.putExtra("ClientId", "88");
                startActivity(intent);
            }
        });
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is Signed In
                    Toast.makeText(CientListActivity.this, "User is signed in", Toast.LENGTH_SHORT).show();
                } else {
                    //user is Signed Out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
/**
 *
 */
        mdata = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recUserList);
        recyclerView.setHasFixedSize(true);
        userListAdapter = new UserListAdapter(this, mdata);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userListAdapter);
        getListdata2();
        getListdata();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Sign in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
        getListdata();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    public void getListdata() {
        if (mChildEventListener == null) {
            Toast.makeText(CientListActivity.this, "Hello Snapshot", Toast.LENGTH_SHORT).show();
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    UserListModel model = dataSnapshot.getValue(UserListModel.class);
                    Integer no =new Integer(model.getuid());
                    int n = new1.indexOf(no);
                    if(n==-1){
                        Log.d("PositionOfDataFound",no+"data Not Found"+"");
                    }
                    else{
                        mdata.remove(n);
                        mdata.add(n,model);
                    }
                    Comparator<UserListModel> cmp = Collections.reverseOrder(new TimeStamp());
                    Collections.sort(mdata, cmp);
                    Toast.makeText(CientListActivity.this, "Hello Snapshot", Toast.LENGTH_SHORT).show();
                    Log.d("DataSnapShot", model.getName() + ".........." + model.isRead() + ".........." + model.getuid() + "\n" + dataSnapshot.toString());
                    userListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            databaseReference.addChildEventListener(mChildEventListener);
        }
    }

    public void getListdata2() {
        final int[] i = {0};
        mdata.clear();
        String response = "[" +
                "  {" +
                "    \"name\":\"Sheshank\"," +
                "    \"uid\":88" +
                "  }," +
                "  {" +
                "    \"name\":\"Tanshu\"," +
                "    \"uid\":87" +
                "  }," +
                "  {" +
                "    \"name\":\"Akshit\"," +
                "    \"uid\":102" +
                "  }," +
                "  {" +
                "    \"name\":\"Manya\"," +
                "    \"uid\":113" +
                "  }," +
                "  {" +
                "    \"name\":\"Paras\"," +
                "    \"uid\":115" +
                "  }," +
                "  {" +
                "    \"name\":\"Sandeep\"," +
                "    \"uid\":117" +
                "  }," +
                "  {" +
                "    \"name\":\"Tanya\"," +
                "    \"uid\":119" +
                "  }," +
                "  {" +
                "    \"name\":\"Tushar\"," +
                "    \"uid\":121" +
                "  }," +
                "  {" +
                "    \"name\":\"Piyush\"," +
                "    \"uid\":123" +
                "  }" +
                "]";
        unread = new ArrayList<>();
        read = new ArrayList<>();
        new1 = new ArrayList<>();
        JSONArray arr = null;
        JSONObject ob;
        try {
            arr = new JSONArray(response);
            ob = new JSONObject();

            Log.d("Response SIze", arr.length() + "");
            for (i[0] = 0; i[0] < arr.length(); i[0]++) {
                ob = arr.getJSONObject(i[0]);
                final String name = ob.getString("name");
                final int uid = ob.getInt("uid");
                mdata.add(new UserListModel(ob.getString("name"), true, 0, ob.getInt("uid")));

                new1.add(ob.getInt("uid"));
                Log.d("Addnew1",new1.get(i[0]).toString()+"");

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.print(i[0] + "Hello bhai");

    }

    public class TimeStamp implements Comparator<UserListModel> {

        @Override
        public int compare(UserListModel userListModel, UserListModel t1) {
            return (userListModel.getTimeStamp()<t1.getTimeStamp()) ? -1 : (userListModel.getTimeStamp()>t1.getTimeStamp()) ? 1 : 0;
        }
    }
}
