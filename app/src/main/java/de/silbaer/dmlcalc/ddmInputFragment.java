package de.silbaer.dmlcalc;

import android.app.Activity;
import android.content.Context;
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
 * Use the {@link ddmInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ddmInputFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private String DDM;
    private String DDM_e1;
    private String DDM_e2;
    private String DDM_e3;
    private String DDM_e4;

    public ddmInputFragment() {
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
    public static ddmInputFragment newInstance(/*String param1, String param2*/) {
        ddmInputFragment fragment = new ddmInputFragment();
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
        return inflater.inflate(R.layout.fragment_ddm_input, container, false);
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
        AutoCompleteTextView textDDM;

        AutoCompleteTextView textE1;
        AutoCompleteTextView textE2;
        AutoCompleteTextView textE3;
        AutoCompleteTextView textE4;

        if(a == 1) {
            return;
        }

        textDDM = (AutoCompleteTextView) view.findViewById(R.id.ddm_inputFragment_autoCompleteDDM);

        textE1 = (AutoCompleteTextView) view.findViewById(R.id.ddm_inputFragment_ac_ddmElement1);
        textE2 = (AutoCompleteTextView) view.findViewById(R.id.ddm_inputFragment_ac_ddmElement2);
        textE3 = (AutoCompleteTextView) view.findViewById(R.id.ddm_inputFragment_ac_ddmElement3);
        textE4 = (AutoCompleteTextView) view.findViewById(R.id.ddm_inputFragment_ac_ddmElement4);



        ArrayAdapter<Dragon> ad = new ArrayAdapter<Dragon>(view.getContext(), R.layout.support_simple_spinner_dropdown_item);
        ad.addAll(DMLcalc.Instance().dragons.values());

        textDDM.setAdapter(ad);
        textDDM.setThreshold(1);
        textDDM.setOnItemClickListener(onDDMItemClick);

        ArrayAdapter<element> ade = new ArrayAdapter<element>(view.getContext(), R.layout.support_simple_spinner_dropdown_item);

        ade.addAll(DMLcalc.Instance().elements);
        textE1.setAdapter(ade);
        textE1.setThreshold(1);
        textE1.setOnItemClickListener(onElement1ItemClick);

        textE2.setAdapter(ade);
        textE2.setThreshold(1);
        textE2.setOnItemClickListener(onElement2ItemClick);

        textE3.setAdapter(ade);
        textE3.setThreshold(1);
        textE3.setOnItemClickListener(onElement3ItemClick);

        textE4.setAdapter(ade);
        textE4.setThreshold(1);
        textE4.setOnItemClickListener(onElement4ItemClick);

        DDM = DMLcalc.Instance().getDDM();
        DDM_e1 = DMLcalc.Instance().getDDM_e1();
        DDM_e2 = DMLcalc.Instance().getDDM_e2();
        DDM_e3 = DMLcalc.Instance().getDDM_e3();
        DDM_e4 = DMLcalc.Instance().getDDM_e4();

        Dragon d = DMLcalc.Instance().dragons.get(DDM);
        if(d != null) {
            textDDM.setText(d.toString());
        }
        element e1 = new element(DDM_e1);
        element e2 = new element(DDM_e2);
        element e3 = new element(DDM_e3);
        element e4 = new element(DDM_e4);
        textE1.setText(e1.toString());
        textE2.setText(e2.toString());
        textE3.setText(e3.toString());
        textE4.setText(e4.toString());
    }

    private AdapterView.OnItemClickListener onElement1ItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Activity  a = getActivity();
            InputMethodManager imm = (InputMethodManager) a.getSystemService(
                    a.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( a.getCurrentFocus().getWindowToken(), 0);

            element e = (element)  arg0.getItemAtPosition(arg2);
            DDM_e1 = e.id;
            DMLcalc.Instance().setDDM(DDM,DDM_e1,DDM_e2,DDM_e3,DDM_e4);
        }
    };
    private AdapterView.OnItemClickListener onElement2ItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Activity  a = getActivity();
            InputMethodManager imm = (InputMethodManager) a.getSystemService(
                    a.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( a.getCurrentFocus().getWindowToken(), 0);

            element e = (element)  arg0.getItemAtPosition(arg2);
            DDM_e2 = e.id;
            DMLcalc.Instance().setDDM(DDM,DDM_e1,DDM_e2,DDM_e3,DDM_e4);
        }
    };
    private AdapterView.OnItemClickListener onElement3ItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Activity  a = getActivity();
            InputMethodManager imm = (InputMethodManager) a.getSystemService(
                    a.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( a.getCurrentFocus().getWindowToken(), 0);

            element e = (element)  arg0.getItemAtPosition(arg2);
            DDM_e3 = e.id;
            DMLcalc.Instance().setDDM(DDM,DDM_e1,DDM_e2,DDM_e3,DDM_e4);
        }
    };
    private AdapterView.OnItemClickListener onElement4ItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Activity  a = getActivity();
            InputMethodManager imm = (InputMethodManager) a.getSystemService(
                    a.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( a.getCurrentFocus().getWindowToken(), 0);

            element e = (element)  arg0.getItemAtPosition(arg2);
            DDM_e4 = e.id;
            DMLcalc.Instance().setDDM(DDM,DDM_e1,DDM_e2,DDM_e3,DDM_e4);
        }
    };



    private AdapterView.OnItemClickListener onDDMItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            Activity  a = getActivity();
            InputMethodManager imm = (InputMethodManager) a.getSystemService(
                    a.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( a.getCurrentFocus().getWindowToken(), 0);

            Dragon d = (Dragon) arg0.getItemAtPosition(arg2);

            DDM = d.getId();

            DMLcalc.Instance().setDDM(DDM,DDM_e1,DDM_e2,DDM_e3,DDM_e4);
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
