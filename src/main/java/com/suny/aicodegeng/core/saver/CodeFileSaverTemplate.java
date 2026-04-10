package com.suny.aicodegeng.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.suny.aicodegeng.constant.AppConstant;
import com.suny.aicodegeng.exception.BusinessException;
import com.suny.aicodegeng.exception.ErrorCode;
import com.suny.aicodegeng.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 抽象代码文件保存--模板方法
 *
 * @param <T>
 */
public abstract class CodeFileSaverTemplate<T> {

    // 文件保存根目录
    private static final String FILE_SAVE_ROOT_DIR = AppConstant.CODE_OUTPUT_ROOT_DIR;


    /**
     * 模板方法 保存代码标准流程
     * @param result 结果对象
     * @return 文件对象
     */
    public final File saveCode(T result, Long appId) {
        // 1. 验证输入
        validateInput(result);
        // 2. 构架唯一目录
        String baseDirPath = buildUniqueDir(appId);
        // 3. 保存文件（交给子类）
        saveFiles(result, baseDirPath);
        // 4. 返回文件
        return new File(baseDirPath);
    }


    /**
     * 验证输入结果的有效性
     *
     * @param result 需要验证的结果对象
     * @throws BusinessException 当结果为空时抛出业务异常
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "代码结果为空");
        }
    }

    /**
     * 保存文件 交由子类实现
     * @param result 结果对象
     * @param baseDirPath 文件保存根目录
     */
    protected abstract void saveFiles(T result, String baseDirPath);

    /**
     * 构建唯一目录路径：tmp/code_output/bizType_雪花ID
     */
    protected String buildUniqueDir(Long appId) {
        if (appId == null) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "appId不能为空");
        }
        String codeType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", codeType, appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 获取代码类型
     * @return 代码类型
     */
    protected abstract CodeGenTypeEnum getCodeType();

    /**
     * 写入单个文件
     *
     * @param dirPath 目录
     * @param filename 文件名
     * @param content 内容
     */
    public final void writeToFile(String dirPath, String filename, String content) {
        if(StrUtil.isNotBlank(content)){
            String filePath = dirPath + File.separator + filename;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }


}
