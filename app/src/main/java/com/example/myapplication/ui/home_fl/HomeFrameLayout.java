//package com.example.myapplication.ui.home_fl;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//
//import com.example.myapplication.R;
//import com.example.myapplication.databinding.FramelayoutHomeBinding;
//import com.example.myapplication.ui.home.HomeFragment;
//
//public class HomeFrameLayout extends Fragment {
//
//    private FramelayoutHomeBinding binding;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//
//        binding = FramelayoutHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_home, new HomeFragment()).commit();
//
//        return root;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}
