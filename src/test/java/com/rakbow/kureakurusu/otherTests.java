package com.rakbow.kureakurusu;

import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Rakbow
 * @since 2022-12-01 23:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApplication.class)
public class otherTests {


    @Test
    public void tmpTest() {
        String url = "https://img.rakbow.com/album/6/f57fa8a51b594e30.jpg";
        System.out.println(QiniuImageUtil.getImageKeyByFullUrl(url));
    }

    public static int getDuration(String time) {
        String[] parts = time.split(":");
        if (parts.length == 2) {
            try {
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);

                return minutes * 60 + seconds;
            } catch (NumberFormatException e) {
                // 处理格式错误的情况
                System.err.println("Invalid time format");
            }
        } else {
            // 处理格式错误的情况
            System.err.println("Invalid time format");
        }

        var a = 1;

        return 0; // 或者返回合适的默认值
    }

    @Test
    public void test2() {

    }

}
