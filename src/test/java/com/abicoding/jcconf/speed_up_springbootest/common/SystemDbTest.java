package com.abicoding.jcconf.speed_up_springbootest.common;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SystemDbTestExtension.class)
public @interface SystemDbTest {

}
