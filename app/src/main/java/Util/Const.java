package Util;

import com.example.mate.R;

/**
 * Created by 가애 on 2017-12-21.
 */

public class Const {

    final static public String[] APP_THEME_COLORS = {
            //분홍계열
            "#F7CAC9", "#EF9599", "#E9696F",

            //노란계열
            "#F39C12", "#F2D000",

            //초록계열
            "#8ABA2B", "#2ECC71", "#1ABC9C",

            //파란계열
            "#ABDBDF", "#97D4E0", "#92A8D1",

            //보라계열
            "#D991E8", "#9B59B6",

            //갈색계열
            "#ECD9B0", "#D1AF94"
    };

    final static public int[] APP_PW_SETTING = {1, 2, 3, 4, 5, 6, 7, 8, 9, R.drawable.btn_cancel, 0, R.drawable.btn_back};

    //배경
    final static public int RESULT_SETTING_THEME = 10; //결과값
    final static public int REQUEST_THEME_SET = 0; // 요청값

    final static public int TAKE_PICTURE_CAMERA = 1; // 요청값
    final static public int TAKE_PICTURE_ALBUM = 2; // 요청값
    final static public int CROP_PICTURE = 3; // 요청값

    final static public int REQUEST_PERMISSION_CAMERA = 100; // 요청값
    final static public int REQUEST_PERMISSION_ALBUM = 200;

    final static public int REQUEST_REMOVE_DIARY = 1234;
    final static public int RESULT_REMOVE_DIARY = 4321;
}