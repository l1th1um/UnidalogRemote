package xyz.andri.unidalogremote;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RemoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RemoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Button btn;
    View view;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemoteFragment newInstance(String param1, String param2) {
        RemoteFragment fragment = new RemoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RemoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_remote_a, container, false);

        TabHost mTabHost = (TabHost)view.findViewById(R.id.tabHost);

        mTabHost.setup();
        TabHost.TabSpec spec1, spec2, spec3;

        spec1 = mTabHost.newTabSpec("tab1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Request Data");
        mTabHost.addTab(spec1);

        spec2 = mTabHost.newTabSpec("tab2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Automatic SMS");
        mTabHost.addTab(spec2);

        spec3 = mTabHost.newTabSpec("tab3");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("Station Settings");
        mTabHost.addTab(spec3);

        /*Spinner spinner = (Spinner) view.findViewById(R.id.spinner_request);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.parameter_station_a, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/
        ;
        /*TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);*/
        EditText custom_date = (EditText) view.findViewById(R.id.custom_date_time);
        custom_date.setOnClickListener(custom_date_handler);
        return view;
        //return inflater.inflate(R.layout.fragment_remote_a, container, false);
    }

    View.OnClickListener custom_date_handler = new View.OnClickListener() {
        public String selected_date;
        public String selected_time;

        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_datetimepicker);

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
            datePicker.init(year, month, day, null);
            selected_date = String.format("%02d/%02d/%02d", datePicker.getMonth(),
                                        datePicker.getDayOfMonth(), datePicker.getYear() % 100);

            final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);

            timePicker.setCurrentHour(c.get(Calendar.HOUR));
            timePicker.setCurrentMinute(c.get(Calendar.MINUTE));

            selected_time = String.format("%02d:%02d", c.get(Calendar.HOUR), c.get(Calendar.MINUTE) );

            timePicker.setOnTimeChangedListener(change_time_handler);


            /*timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    String time_pick = String.format("%02d:%02d", hourOfDay, minute );
                    Log.v("Tanggal Berubah", selected_date + " " + time_pick);
                }
            });*/


            dialog.setTitle(getString(R.string.pick_date_time));
            dialog.show();

            Button okBtn = (Button) dialog.findViewById(R.id.btnOK);
            // if decline button is clicked, close the custom dialog
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close dialog
                    Log.v("Dialog Button", "Sip");
                    EditText custom_date =  (EditText) view.findViewById(R.id.custom_date_time);
                    custom_date.setText(selected_date + " " + selected_time);

                    dialog.dismiss();
                }
            });

        }

        TimePicker.OnTimeChangedListener change_time_handler = new OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                selected_time = String.format("%02d:%02d", hourOfDay, minute );
                //String time_pick = String.format("%02d:%02d", hourOfDay, minute );
                //Log.v("Tanggal Berubah", selected_date + " " + time_pick);
            }
        };

    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
