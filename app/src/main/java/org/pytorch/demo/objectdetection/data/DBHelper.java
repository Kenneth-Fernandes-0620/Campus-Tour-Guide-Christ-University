//package org.pytorch.demo.objectdetection.data;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import androidx.annotation.Nullable;
//
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//public class DBHelper extends SQLiteOpenHelper {
//
//    //variables
//    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_NAME = "christdb";
//    private static final String Table_Building_Info = "blockdata";
//    private static final String Table_2 = "block1";
//    //  private static final String Table_3 = "flights";
//    private static final String KEY_ID = "id";
//    private static final String KEY_NAME = "name";
//    private static final String KEY_PH_NO = "room_no";
//    private static final String key_nationality = "type";
//    private static final String key_gender = "floor";
//    private static  final String key_blocks="block_name";
//
//    String Dbpath;
//
//    public DBHelper(@Nullable Context mcontext, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//        this.context = mcontext;
//        Dbpath = "data/data" + context.getPackageName() + "/databases";
//    }
//
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//
//    }
//
//    public void CheckDatabase(){
//        try{
//            String path = DATABASE_NAME + Dbpath;
//            SQLiteDatabase.openDatabase(path, null, 0);
//        }catch (Exception e){}
//        this.getReadableDatabase();
//        CopyDatabase();
//    }
//
//    public void CopyDatabase(){
//        try{
//            InputStream io = context.getAssets().open(DATABASE_NAME);
//            String oufilename = Dbpath + DATABASE_NAME;
//            OutputStream outputStream = new FileOutputStream(oufilename);
//            int length;
//            byte[] buffer = new byte[];
//            while((length = io.read(buffer)) > 0){
//                outputStream.write(buffer, length,0);
//            }
//            io.close();
//            outputStream.flush();
//            outputStream.close();
//        }catch (Exception e){}
//    }
//
//    public void OpenDatabase(){
//        String path = Dbpath + DATABASE_NAME;
//        SQLiteDatabase.openDatabase(path, null, 0);
//    }
//}
