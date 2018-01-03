package de.silbaer.dmlcalc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedingCalcFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedingCalcFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedingCalcFragment extends Fragment {

    int[] feedingCostsSum = {0,
            4,
            12,
            28,
            68,
            148,
            308,
            628,
            1268,
            2548,
            5108,
            10228,
            16628,
            25028,
            37028,
            53028,
            73528,
            98528,
            133528,
            183528,
            248528,
            331028,
            431028,
            551028,
            701028,
            881028,
            1097028,
            1349028,
            1637028,
            1961028,
            2325028,
            2737028,
            3197028,
            3709028,
            4277028,
            4909028,
            5605028,
            6373028,
            7217028,
            8141028,
            9153028,
            10257028,
            11457028,
            12761028,
            14177028,
            15709028,
            17365028,
            19153028,
            21081028,
            23153028,
            25381028,
            27769028,
            30329028,
            33065028,
            35989028,
            39109028,
            42433028,
            45973028,
            49737028,
            53737028,
            57937028,
            62337028,
            66937028,
            71737028,
            76737028,
            81937028,
            87337028,
            92937028,
            98737028,
            104737028,
            110937028,
            117337028,
            123937028,
            130737028,
            137737028,
            144937028,
            152337028,
            159937028,
            167737028,
            175737028};



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FeedingCalcFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedingCalcFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedingCalcFragment newInstance(String param1, String param2) {
        FeedingCalcFragment fragment = new FeedingCalcFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_feeding_calc, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
