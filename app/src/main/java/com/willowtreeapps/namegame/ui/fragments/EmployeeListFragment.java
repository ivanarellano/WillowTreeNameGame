package com.willowtreeapps.namegame.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.namegame.R;
import com.willowtreeapps.namegame.core.ApplicationComponent;
import com.willowtreeapps.namegame.network.api.ProfilesRepository;
import com.willowtreeapps.namegame.network.api.model.Item;
import com.willowtreeapps.namegame.ui.EmployeeDialogFragment;
import com.willowtreeapps.namegame.ui.activities.NameGameActivity;
import com.willowtreeapps.namegame.ui.adapters.ItemListAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EmployeeListFragment extends NameGameBaseFragment {

    private static final String EMPLOYEE_DIALOG_TAG = "employee_dialog_tag";

    @Inject ProfilesRepository profileRepository;

    @BindView(R.id.recyclerview) RecyclerView employeeRecyclerView;

    private Unbinder unbinder;
    private ItemListAdapter itemListAdapter;

    public static EmployeeListFragment newInstance() {
        return new EmployeeListFragment();
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.employee_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        employeeRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();

        populateRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_employee_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_name_game:
                NameGameActivity.startNameGameActivity(getActivity());
                return true;
            case R.id.action_sort_first_name_ascending:
                itemListAdapter.sortByFirstName();
                resetPosition();
                return true;
            case R.id.action_sort_last_name_ascending:
                itemListAdapter.sortByLastName();
                resetPosition();
                return true;
            case R.id.action_shuffle_names:
                itemListAdapter.shuffleNames();
                resetPosition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateRecyclerView() {
        List<Item> itemList = profileRepository.getItemList();
        itemListAdapter = new ItemListAdapter(itemList);
        itemListAdapter.setItemClickListener(itemClickListener);

        employeeRecyclerView.setHasFixedSize(true);
        employeeRecyclerView.setAdapter(itemListAdapter);
    }

    private void resetPosition() {
        employeeRecyclerView.scrollToPosition(0);
    }

    private final ItemListAdapter.ItemClickListener itemClickListener = new ItemListAdapter.ItemClickListener() {
        @Override
        public void onClick(@NonNull Item item) {
            EmployeeDialogFragment fragment = EmployeeDialogFragment.newInstance(item);
            fragment.show(getFragmentManager(), EMPLOYEE_DIALOG_TAG);
        }
    };
}
