package com.example.sy.myapplication.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBUtil extends SQLiteOpenHelper {
    private static DBUtil INSTANCE;
    private StatusSave stat;


    public static DBUtil getInstance(Context context, String name,
                                     SQLiteDatabase.CursorFactory factory, int version) {
        if (INSTANCE == null){
            INSTANCE = new DBUtil(context, name, factory, version);
        }
        return INSTANCE;
    }
    public static DBUtil getInstance() {
        if (INSTANCE == null){
            //인스턴스 없으니까 바로 위 메서드로 만들어라
            throw new NullPointerException();
        }
        return INSTANCE;
    }

    // 생성자,    관리할 DB 이름과 버전 정보를 받음
    private DBUtil(Context context, String name,
                  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        stat = StatusSave.getInstance();
    }

    // 최초 DB를 만들때 한번만 호출된다.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE HOMEMANAGER (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT NOT NULL, " +
                "EXPIRY_DATE TEXT NOT NULL, " +   //만기일
                "TODAY TEXT NOT NULL, " +  //설정일
                "WARNING TEXT NOT NULL," +  //경고일
                "DETAIL TEXT," +  //경고일
                "HWTYPE INT NOT NULL);");
    }

    // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //데이터 입력시 선언함.
    public void insertData(String name, String urgent, String warning, String today, String memo, int type) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO HOMEMANAGER VALUES(NULL, '" +name +"', '"+ urgent+ "', '" + today  + "', '"+ warning + "', '"+ memo +"', '"+ type +"');";
        db.execSQL(sql);
    }

    //각각의 리스트 데이터 가져옴.
    public ArrayList<String> getTypeData(StatusSave.TabGrade tabGrade){
        DateUtil DateU = new DateUtil();
        SQLiteDatabase db = getWritableDatabase();

        ArrayList<String> MyData = new ArrayList<>();

        String Today = DateU.ToDay();
        int Today_int = Integer.parseInt(Today);
        String str;

        //메인액티비티서는 타입관계없이 시급 주의 다뽑음
        if( stat.getCategory() == StatusSave.Category.MAIN ){
            switch ( tabGrade.getNum() ){
                case 1:
                    //시급 리스트 쭉뽑아
                    //지난지 오래될수록 위로
                    Cursor result = db.rawQuery("select NAME from HOMEMANAGER where EXPIRY_DATE <= "+Today_int+
                                                " order by EXPIRY_DATE asc;", null);
                    while (result.moveToNext()) {
                        str = result.getString(0);
                        MyData.add(str);
                        //mAdapter.addItem(str);
                    }
                    result.close();
                    break;
                case 2:
                    //주의 리스트 쭉뽑아
                    //만기기간가까운거 위로
                    Cursor result2 = db.rawQuery("select NAME from HOMEMANAGER where WARNING <="+Today_int+
                                                 " and EXPIRY_DATE>"+Today_int+
                                                 " order by EXPIRY_DATE asc;", null);
                    while (result2.moveToNext()) {
                        str = result2.getString(0);
                        MyData.add(str);
                        //mAdapter.addItem(str);
                    }
                    result2.close();
                    break;
            }
        }
        //냉장고 청소 빨래 각각의 시급 주의 노멀 리스트
        else {
            switch ( tabGrade.getNum() ){
                case 1:
                    //시급 리스트 쭉뽑아
                    //만기 오래될수록 위로
                    Cursor result = db.rawQuery("select NAME from HOMEMANAGER where HWTYPE = "+stat.getCategory().getNum()+
                                                " and EXPIRY_DATE <= "+Today_int+
                                                " order by EXPIRY_DATE asc;", null);
                    while (result.moveToNext()) {
                        str = result.getString(0);
                        MyData.add(str);
                        //mAdapter.addItem(str);
                    }
                    result.close();
                    break;
                case 2:
                    //주의 리스트 쭉뽑아
                    //만기기간 가까운거 위로
                    Cursor result2 = db.rawQuery("select NAME from HOMEMANAGER where HWTYPE = "+stat.getCategory().getNum()+
                                                 " and WARNING <= "+Today_int+" and EXPIRY_DATE >"+Today_int+
                                                 " order by EXPIRY_DATE asc;;", null);
                    while (result2.moveToNext()) {
                        str = result2.getString(0);
                        MyData.add(str);
                        //mAdapter.addItem(str);
                    }
                    result2.close();
                    break;
                case 3:
                    //괜춘 리스트 쭉뽑아
                    //주의기간 가까운거 위로
                    Cursor result3 = db.rawQuery("select NAME from HOMEMANAGER where HWTYPE = "+stat.getCategory().getNum()+
                                                 " and WARNING >"+Today_int+
                                                 " order by WARNING asc;;", null);
                    while (result3.moveToNext()) {
                        str = result3.getString(0);
                        MyData.add(str);
                        //mAdapter.addItem(str);
                    }
                    result3.close();
                    break;
            }
        }
        return MyData;
    }
    //다이얼로그 세부내용 작성시 일자관련 데이터 받아옴
    public String getDate(String s){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select EXPIRY_DATE,TODAY,WARNING from HOMEMANAGER where NAME ='"+s+"';", null);
        cursor.moveToNext();
        String result;

        String str2[]={"","",""};
        for (int i=0;i<3;i++){
            String str;

            str = cursor.getString(i);
            str2[i] = str.substring(0,4)+" - "
                    + str.substring(4,6)+" - "
                    + str.substring(6);
        }

        result = "설정일 : "+str2[1]+"\n경고일 : "+str2[2]+"\n만기일 : "+str2[0]+"\n\n세부내용 : "+ getDetail(s);
        cursor.close();
        return result;
    }
    //위 함수에서 세부내용 추가내용.
    private String getDetail(String s){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select DETAIL from HOMEMANAGER where NAME ='"+s+"';", null);
        cursor.moveToNext();
        String result;

        result = cursor.getString(0);
        if(result.equals("null")){
            result = "작성하지 않았습니다";
        }
        cursor.close();
        return result;
    }
    //리스트 갱신할 때 실행되는 함수
    public void update(String s){
        SQLiteDatabase db = getWritableDatabase();

        //냉장고의 경우 먹어버리면 땡이라서 갱신할 필요가 없기에 딜리트함수 실행
        if(refrigeratorCheck(s,db)){
            delete(s);
        }
        else{
            Cursor cursor = db.rawQuery("select EXPIRY_DATE,TODAY,WARNING from HOMEMANAGER where NAME ='"+s+"';", null);
            cursor.moveToNext();

            String str[]={"","",""};
            for (int i=0;i<3;i++){
                str[i]= cursor.getString(i);
            }

            DateUtil dateUtil = new DateUtil();

            String n_expiry = dateUtil.Expiry_Update(str[0],str[1]); //새로운 만기일
            String n_warning = dateUtil.warning_Update(str[0],str[2],str[1]); //새로운 경고일
            String n_today = dateUtil.ToDay();

            db.execSQL("UPDATE HOMEMANAGER SET EXPIRY_DATE=" + n_expiry + "" +
                    ",TODAY="+ n_today +",WARNING="+ n_warning +" WHERE NAME='" + s + "';");
            cursor.close();
        }
    }
    //리스트 갱신시 냉장고 관련인지 확인하는 함수
    private boolean refrigeratorCheck(String s, SQLiteDatabase SQD){
        Cursor cursor = SQD.rawQuery("select HWTYPE from HOMEMANAGER where NAME ='"+s+"';", null);
        cursor.moveToNext();
        int refr = Integer.parseInt(cursor.getString(0));

        cursor.close();

        if(refr == 3)    return true;
        else             return false;
    }
    //리스트 삭제시
    public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM HOMEMANAGER WHERE NAME='" + item + "';");
    }
    //같은 이름을 갖은 리스트가 있는지 확인하는 함수.
    public boolean overlap_check(String s){
        SQLiteDatabase db = getReadableDatabase();

        boolean result;

        Cursor cursor = db.rawQuery("select EXPIRY_DATE,TODAY,WARNING from HOMEMANAGER where NAME ='"+s+"';", null);
        cursor.moveToNext();

        try {
            //CursorIndexOutOfBoundsException오류가 나면 커서에는 아무내용도 없다는 뜻이므로 중복된게 없다.
            String i = cursor.getString(1);
            result = true;
        }catch (CursorIndexOutOfBoundsException e){
            result = false;
        }

        cursor.close();

        return result;
    }
    //리스트의 개수 가져오는 함수
    public int getTypeCount(int tab){
        DateUtil DateU = new DateUtil();
        SQLiteDatabase db = getWritableDatabase();

        String Today = DateU.ToDay();
        int Today_int = Integer.parseInt(Today);

        int i = 0;
        if(tab == 1){
            //시급 리스트 쭉뽑아
            Cursor result = db.rawQuery("select NAME from HOMEMANAGER where EXPIRY_DATE <= "+Today_int, null);
            while (result.moveToNext()) {
                i++;
            }
            result.close();
        }
        else{
            //주의 리스트 쭉뽑아
            Cursor result = db.rawQuery("select NAME from HOMEMANAGER where WARNING <="+Today_int+" and EXPIRY_DATE>"+Today_int, null);
            while (result.moveToNext()) {
                i++;
            }
            result.close();
        }
        return i;
    }
}
