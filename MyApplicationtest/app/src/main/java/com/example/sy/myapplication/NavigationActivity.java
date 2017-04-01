package com.example.sy.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sy.myapplication.Fragment.ClearupFragment;
import com.example.sy.myapplication.Fragment.DeveloperFragment;
import com.example.sy.myapplication.Fragment.LaundryFragment;
import com.example.sy.myapplication.Fragment.LicenseFragment;
import com.example.sy.myapplication.Fragment.MainFragment;
import com.example.sy.myapplication.Fragment.RefrigeratorFragment;
import com.example.sy.myapplication.Service.MyService;
import com.example.sy.myapplication.Utils.StatusSave;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager FragM = getFragmentManager();
    FragmentTransaction transaction = FragM.beginTransaction();

    final static int Clear_up = 1;
    final static int Laundry = 2;
    final static int Refrigerator = 3;
    final static int Main = 4;
    final static int Developer = 5;
    final static int license = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("집안꼴이 이게뭐니");

        MainFragment frt = new MainFragment();
        transaction.replace(R.id.frag,frt);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent service = new Intent(this, MyService.class);
        startService(service);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //툴바 오른쪽 설정 추가로 하고싶으면 메뉴폴더에 main 잘 바꿔써먹어
        /*
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.laundry) {
            frag_select(new LaundryFragment(),"빨래",Laundry);
        } else if (id == R.id.refrigerator) {
            frag_select(new RefrigeratorFragment(),"냉장고",Refrigerator);
        } else if (id == R.id.clear_up) {
            frag_select(new ClearupFragment(),"청소",Clear_up);
        } else if (id == R.id.nav_developer) {
            frag_select(new DeveloperFragment(),"개발자",Developer);
        } else if (id == R.id.nav_license) {
            frag_select(new LicenseFragment(),"오픈소스 라이센스",license);
        } else if (id == R.id.main_go) {
            frag_select(new MainFragment(),"집안꼴이 이게뭐니",Main);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setFragment(Fragment Frag, String str){
        FragmentManager FragM = getFragmentManager();
        FragmentTransaction transaction = FragM.beginTransaction();

        getSupportActionBar().setTitle(str);

        transaction.replace(R.id.frag,Frag);
        transaction.commit();
    }
    public void frag_select(Fragment frag,String str,int i){
        StatusSave an = new StatusSave();
        an.setNumber(i);
        setFragment(frag,str);
    }
}
