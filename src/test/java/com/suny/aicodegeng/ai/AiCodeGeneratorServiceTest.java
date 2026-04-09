package com.suny.aicodegeng.ai;

import com.suny.aicodegeng.ai.model.HtmlCodeResult;
import com.suny.aicodegeng.ai.model.MultiFileCodeResult;
import com.suny.aicodegeng.core.CodeFileSaver;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;


@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;


    @Test
    void generateHtmlCode() {
        HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode("做个工作记录小工具，不超过20行");
        File file = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode("做个简约的留言板,不超过50行");
        File file = CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
        Assertions.assertNotNull(file);
    }
}


