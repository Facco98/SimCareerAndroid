package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.championship;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.ranking.RankingFragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.Championship;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.SettingItem;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.persistence.PersistenceManager;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Result;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.util.Constants;

import java.text.ParseException;

public class ChampionshipDetailsInfoFragment extends Fragment {

    private static final String TAG = "ChampionshipDetailsInfoFragment";

    @NonNull
    private Championship championship;

    private MaterialButton btnSubscribe;
    private MaterialButton btnRanking;
    private MaterialButton btnForum;

    private TextView lblCarsList;
    private TextView lblSettingsList;

    private User user;

    private AlertDialog dialog;

    public ChampionshipDetailsInfoFragment(@NonNull Championship championship) {

        super();
        this.championship = championship;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_championship_detail_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //super.onViewCreated(view, savedInstanceState);
        if( this.getView() == null )
            return;

        this.user = new PersistenceManager(this.getView().getContext()).getUser();
        this.setupUI();

        this.updateUI();

        Log.e(TAG, new Gson().toJson(this.championship));

    }


    private void btnSubscribeDidClick( View v ){

        Log.i(TAG, "Button subscribe did tap");
        if( this.user != null ){

            try {
                Boolean partecipating = this.championship.userPartecipates(user);
                if( partecipating != null ){

                    if( partecipating ){

                        this.removeSubscription();

                    } else {

                        this.addSubscription();

                    }

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }

    private void btnRankingDidClick( View v ){

        Log.i(TAG, "Button ranking did tap");
        FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_frg_container, new RankingFragment(this.championship));
        ft.addToBackStack(null);
        ft.commit();
    }

    private void btnForumDidClick( View v ){

        Log.i(TAG, "Button forum did tap");

    }

    private void setupUI() {

        this.btnSubscribe = this.getView().findViewById(R.id.championship_detail_info_btn_subscribe);
        this.btnForum = this.getView().findViewById(R.id.championship_detail_info_btn_forum);
        this.btnRanking = this.getView().findViewById(R.id.championship_detail_info_btn_ranking);

        this.lblCarsList = this.getView().findViewById(R.id.championship_detail_info_lbl_cars_list);
        this.lblSettingsList = this.getView().findViewById(R.id.championship_detail_info_lbl_settings_list);

        this.btnSubscribe.setOnClickListener(this::btnSubscribeDidClick);
        this.btnRanking.setOnClickListener(this::btnRankingDidClick);
        this.btnForum.setOnClickListener(this::btnForumDidClick);

        this.user = new PersistenceManager(this.getActivity().getApplicationContext()).getUser();

    }

    private void updateUI(){

        StringBuilder sb = new StringBuilder();
        for( SettingItem setting : this.championship.getGameSettings() ){

            sb.append(setting.getType());
            sb.append(": ");
            sb.append(setting.getValue());
            sb.append(Constants.LINE_SEPARATOR);

        }
        this.lblSettingsList.setText(sb.toString());

        sb = new StringBuilder();
        for( String car : this.championship.getCarList() ){
            sb.append(car);
            sb.append(Constants.LINE_SEPARATOR);
        }
        this.lblCarsList.setText(sb.toString());

        try {
            Boolean partecipating = this.championship.userPartecipates(this.user);
            Log.e(TAG, "Partecipating: " + partecipating);
            if( partecipating == null ){

                this.btnSubscribe.setVisibility(View.GONE);

            } else if( partecipating ){

                this.btnSubscribe.setText(R.string.championships_details_info_btn_unsubscribe_text);
                this.btnSubscribe.setBackgroundColor(R.color.championships_chip_over_background);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void subscriptionCompletion(Result<Integer> result){

        if( result.getError() != null ){

            Toast.makeText(this.getActivity(), R.string.championships_details_info_subscription_error, Toast.LENGTH_SHORT).show();
            result.getError().printStackTrace();

        } else if( result.isSuccessfull() ){


            assert this.getActivity() != null;

            if( this.dialog != null ) {
                this.dialog.dismiss();
                this.dialog = null;
            }

            FragmentManager fragmentManager =  this.getActivity().getSupportFragmentManager();
            Fragment f = fragmentManager.findFragmentById(R.id.home_frg_container);
            if( f != null )
                fragmentManager.beginTransaction().detach(f).attach(f).commit();

        }
    }

    private void addSubscription(){

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this.getView().getContext());
        this.dialog = builder.setView(R.layout.dialog_championship_subscribe).show();

        dialog.show();

        EditText txtTeam = dialog.findViewById(R.id.dialog_subscribe_txt_team);
        Spinner spinnerCar = dialog.findViewById(R.id.dialog_subscribe_spinner_car);
        MaterialButton btnConfirm = dialog.findViewById(R.id.dialog_subscribe_btn_subcribe);
        MaterialButton btnCancel = dialog.findViewById(R.id.dialog_subscribe_btn_cancel);

        Log.e(TAG, "VIEWS DONE");

        spinnerCar.setAdapter(new ArrayAdapter<String>(dialog.getContext(), android.R.layout.simple_spinner_item,
        this.championship.getCarList()));


        Log.e(TAG, "ADAPTER DONE");

        btnConfirm.setOnClickListener((v) -> {

            Remote.subscribeUserToChampionship(this.championship, this.user, txtTeam.getText().toString(),
                    (String) spinnerCar.getSelectedItem(),this::subscriptionCompletion);


        });

        btnCancel.setOnClickListener((v) -> dialog.dismiss());

        Log.e(TAG, "END");

    }

    private void removeSubscription(){

        Log.i(TAG, "Removing subscription");
        Remote.removeSubscritionToChampionship(this.championship, this.user, this::subscriptionCompletion);

    }

}
