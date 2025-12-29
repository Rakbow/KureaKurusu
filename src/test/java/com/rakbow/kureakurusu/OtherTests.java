package com.rakbow.kureakurusu;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Rakbow
 * @since 2024/3/3 23:26
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApp.class)
public class OtherTests {

}
