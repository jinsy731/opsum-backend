package com.opusm.backend.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@Transactional
@SpringBootTest(webEnvironment = NONE)
public class BaseServiceTest {
}
