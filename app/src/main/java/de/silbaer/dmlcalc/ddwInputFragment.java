package de.silbaer.dmlcalc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ddwInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ddwInputFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private String DDW;
    private String DDW_mom;
    private String DDW_dad;

    public ddwInputFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment ddwInputFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ddwInputFragment newInstance(/*String param1, String param2*/) {
        ddwInputFragment fragment = new ddwInputFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ddw_input, container, false);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }
    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        int a = 0;


        AutoCompleteTextView textDad;
        AutoCompleteTextView textMom;
        AutoCompleteTextView textDDW;

        if(a == 1) {
            return;
        }

        textDad = (AutoCompleteTextView) view.findViewById(R.id.ddw_inputFragment_autoCompleteDad);
        textMom = (AutoCompleteTextView) view.findViewById(R.id.ddw_inputFragment_autoCompleteMom);
        textDDW = (AutoCompleteTextView) view.findViewById(R.id.ddw_inputFragment_autoCompleteDDW);


        ArrayAdapter<Dragon> ad = new ArrayAdapter<Dragon>(view.getContext(), R.layout.support_simple_spinner_dropdown_item);
        ad.addAll(DMLcalc.Instance().dragons.values());
        textDad.setAdapter(ad);
        textDad.setThreshold(1);

        textMom.setAdapter(ad);
        textMom.setThreshold(1);

        textDDW.setAdapter(ad);
        textDDW.setThreshold(1);

        textDDW.setOnItemClickListener(onDDWItemClick);
        textMom.setOnItemClickListener(onDDWMomItemClick);
        textDad.setOnItemClickListener(onDDWDadItemClick);


        DDW = DMLcalc.Instance().getDDW();
        DDW_dad = DMLcalc.Instance().getDDW_dad();
        DDW_mom = DMLcalc.Instance().getDDW_mom();

        Dragon d;

        d = DMLcalc.Instance().dragons.get(DDW);
        if(d != null) {
            textDDW.setText(d.toString());
        }

        d = DMLcalc.Instance().dragons.get(DDW_mom);
        if(d != null) {
            textMom.setText(d.toString());
        }

        d = DMLcalc.Instance().dragons.get(DDW_dad);
        if(d != null) {
            textDad.setText(d.toString());
        }

    }

    private AdapterView.OnItemClickListener onDDWItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            Toast.makeText(getBaseContext(), "DDW Dragon:" + arg0.getItemAtPosition(arg2),
//                    Toast.LENGTH_LONG).show();

//            InputMethodManager imm = (InputMethodManager) arg1. .getSystemService(
//                    INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            Dragon d = (Dragon) arg0.getItemAtPosition(arg2);

            DDW = d.getId();
            DMLcalc.Instance().setDDW(DDW,DDW_mom,DDW_dad);

        }
    };
    private AdapterView.OnItemClickListener onDDWMomItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            Toast.makeText(getBaseContext(), "DDW Mom:" + arg0.getItemAtPosition(arg2),
//                    Toast.LENGTH_LONG).show();
//
//            InputMethodManager imm = (InputMethodManager) getSystemService(
//                    INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            Dragon d = (Dragon) arg0.getItemAtPosition(arg2);

            DDW_mom = d.getId();
            DMLcalc.Instance().setDDW(DDW,DDW_mom,DDW_dad);
        }
    };
    private AdapterView.OnItemClickListener onDDWDadItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            Toast.makeText(getBaseContext(), "DDW Dad:" + arg0.getItemAtPosition(arg2),
//                    Toast.LENGTH_LONG).show();
//
//            InputMethodManager imm = (InputMethodManager) getSystemService(
//                    INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            Dragon d = (Dragon) arg0.getItemAtPosition(arg2);

            DDW_dad = d.getId();
            DMLcalc.Instance().setDDW(DDW,DDW_mom,DDW_dad);
        }
    };


    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
