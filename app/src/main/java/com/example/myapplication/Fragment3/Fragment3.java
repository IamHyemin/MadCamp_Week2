package com.example.myapplication.Fragment3;

//import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment3 extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.drawable.honbab_main);

        final View v = inflater.inflate(R.layout.fragment3, null, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //지도타입 - 일반
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        oneMarker();
        // manyMarker();
    }

    //마커하나찍는 기본 예제
    public void oneMarker() {
        // 서울 여의도에 대한 위치 설정
        LatLng seoul = new LatLng(37.52487, 126.92723);

        // 구글 맵에 표시할 마커에 대한 옵션 설정  (알파는 좌표의 투명도이다.)
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions
                .position(seoul)
                .title("원하는 위치(위도, 경도)에 마커를 표시했습니다.")
                .snippet("여기는 여의도인거같네여!!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .alpha(0.5f);

        // 마커를 생성한다. showInfoWindow를 쓰면 처음부터 마커에 상세정보가 뜨게한다. (안쓰면 마커눌러야뜸)
        mMap.addMarker(makerOptions); //.showInfoWindow();

        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener);

        //카메라를 여의도 위치로 옮긴다.
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        //처음 줌 레벨 설정 (해당좌표=>서울, 줌레벨(16)을 매개변수로 넣으면 된다.) (위에 코드대신 사용가능)(중첩되면 이걸 우선시하는듯)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 16));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getActivity(), "눌렀습니다!!", Toast.LENGTH_LONG);
                return false;
            }
        });
    }

    ////////////////////////  구글맵 마커 여러개생성 및 띄우기 //////////////////////////
    public void manyMarker() {
        // for loop를 통한 n개의 마커 생성
        // 여기 수정하면 될 듯
        for (int idx = 0; idx < 10; idx++) {
            // 1. 마커 옵션 설정 (만드는 과정)
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                    .position(new LatLng(37.52487 + idx, 126.92723))
                    .title("마커" + idx); // 타이틀.

            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);
        }
        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener);

        // 카메라를 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.52487, 126.92723)));
    }

    //마커정보창 클릭리스너는 다작동하나, 마커클릭리스너는 snippet정보가 있으면 중복되어 이벤트처리가 안되는거같다.
    // oneMarker(); 는 동작하지않으나 manyMarker(); 는 snippet정보가 없어 동작이가능하다.

    //정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String markerId = marker.getId();
            Toast.makeText(getActivity(), "정보창 클릭 Marker ID : "+markerId, Toast.LENGTH_SHORT).show();
        }
    };

    //마커 클릭 리스너
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            String markerId = marker.getId();
            //선택한 타겟위치
            LatLng location = marker.getPosition();
            Toast.makeText(getActivity(), "마커 클릭 Marker ID : "+markerId+"("+location.latitude+" "+location.longitude+")", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

}