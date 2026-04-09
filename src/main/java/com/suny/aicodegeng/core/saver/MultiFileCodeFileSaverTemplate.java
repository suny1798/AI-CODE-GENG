package com.suny.aicodegeng.core.saver;

import com.suny.aicodegeng.ai.model.HtmlCodeResult;
import com.suny.aicodegeng.ai.model.MultiFileCodeResult;
import com.suny.aicodegeng.exception.BusinessException;
import com.suny.aicodegeng.exception.ErrorCode;
import com.suny.aicodegeng.model.enums.CodeGenTypeEnum;

public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult>{
    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        writeToFile(baseDirPath, "script.js", result.getJsCode());
        writeToFile(baseDirPath, "style.css", result.getCssCode());
    }

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);
        // 校验HTML代码不能为空
        if (result.getHtmlCode() == null || result.getHtmlCode().isEmpty()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码不能为空");
        }
    }
}
