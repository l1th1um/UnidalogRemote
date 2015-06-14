package xyz.andri.unidalogremote;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;*/


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, RemoteFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private int active_station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void changeSpinnerEntries()
    {
/*        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.no_channel_b, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner_request);

        spinner1.setAdapter(adapter1);*/
    }

    public void onSectionAttached(int number) {
        Fragment fragment = null;

        switch (number) {
            case 1:
                mTitle = getString(R.string.title_station_1);
                active_station = 1;
                fragment = new RemoteFragment();
                break;
            case 2:
                mTitle = getString(R.string.title_station_2);
                active_station = 2;
                fragment = new RemoteFragment();
                break;
            case 3:
                mTitle = getString(R.string.title_station_3);
                active_station = 3;
                fragment = new RemoteFragment();
                break;
            case 4:
                mTitle = getString(R.string.title_station_4);
                active_station = 4;
                fragment = new RemoteFragment();
                /*Bundle args = new Bundle();
                args.putInt("station", 4);
                fragment.setArguments(args);*/

                break;
            case 5:
                mTitle = getString(R.string.title_station_5);
                active_station = 5;
                fragment = new RemoteFragment();;
                break;
            case 6:
                mTitle = getString(R.string.title_station_6);
                active_station = 6;
                fragment = new RemoteFragment();
                //return true;
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

            // update selected item and title, then close the drawer
                /*mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(navMenuTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);*/
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, xyz.andri.unidalogremote.SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    // If Request Data Button Submitted
    public void reqDataBtn(View view)
    {
        final RadioButton radio_current = (RadioButton) findViewById(R.id.radio_current);
        final RadioButton radio_last_10 = (RadioButton) findViewById(R.id.radio_last_10);

        String command = null;

        if (radio_current.isChecked())
        {
            command = "CH";
        }
        else if (radio_last_10.isChecked())
        {
            command = "MCH";
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner_request);
        int channel = spinner.getSelectedItemPosition();

        command = command + Integer.toString(channel);

        sentSMS(command);
    }

    public void reqRecordBtn(View view)
    {
        final EditText date_period = (EditText) findViewById(R.id.custom_date_time);
        String period = date_period.getText().toString();

        String[] text_split = period.split(" ");

        String[] date_split = text_split[0].split("/");
        String[] time_split = text_split[1].split(":");

        String rec = date_split[2] + date_split[0] + date_split[1] + time_split[0] + time_split[1];

        sentSMS("DA" + rec);
    }

    public void sentSMS(String command)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String _messageNumber = prefs.getString("station" + Integer.toString(active_station), "");
        /*Log.v("Active Station ", Integer.toString(active_station));*/

        String messageText = command;
        String sent = "SMS_SENT";

        Log.v("Message Text ", messageText);

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(sent), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if (getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(getBaseContext(), "Sent Request...Please wait for a moment",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Request not Sent",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(sent));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(_messageNumber, null, messageText, sentPI, null);
    }
}
