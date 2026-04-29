package com.suny.aicodegeng.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.suny.aicodegeng.ai.AiCodeGeneratorService;
import com.suny.aicodegeng.ai.AiCodeGeneratorServiceFactory;
import com.suny.aicodegeng.ai.ImageCollectorService;
import com.suny.aicodegeng.ai.model.HtmlCodeResult;
import com.suny.aicodegeng.langgraph4j.model.ImageResource;
import com.suny.aicodegeng.ai.model.MultiFileCodeResult;
import com.suny.aicodegeng.ai.model.message.AiResponseMessage;
import com.suny.aicodegeng.ai.model.message.ToolExecutedMessage;
import com.suny.aicodegeng.ai.model.message.ToolRequestMessage;
import com.suny.aicodegeng.core.parser.CodeParserExecutor;
import com.suny.aicodegeng.core.saver.CodeFileSaverExecutor;
import com.suny.aicodegeng.exception.BusinessException;
import com.suny.aicodegeng.exception.ErrorCode;
import com.suny.aicodegeng.model.enums.CodeGenTypeEnum;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

/**
 * AI 代码生成外观类，组合生成和保存功能
 */
@Slf4j
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;

    @Resource
    private ImageCollectorService imageCollectorService;

    /**
     * 构建增强后的提示词，包含图片资源信息
     *
     * @param originalPrompt 原始用户提示词
     * @param imageList      收集到的图片资源列表
     * @return 增强后的提示词
     */
    public String buildEnhancedPrompt(String originalPrompt, List<ImageResource> imageList) {
        if (CollUtil.isEmpty(imageList)) {
            return originalPrompt;
        }
        StringBuilder enhancedPromptBuilder = new StringBuilder();
        enhancedPromptBuilder.append(originalPrompt);
        enhancedPromptBuilder.append("\n\n## 可用素材资源\n");
        enhancedPromptBuilder.append("请在生成网站使用以下图片资源，将这些图片合理地嵌入到网站的相应位置中。\n");
        for (ImageResource image : imageList) {
            enhancedPromptBuilder.append("- ")
                    .append(image.getCategory().getText())
                    .append("：")
                    .append(image.getDescription())
                    .append("（")
                    .append(image.getUrl())
                    .append("）\n");
        }
        return enhancedPromptBuilder.toString();
    }

    /**
     * 收集图片资源
     *
     * @param userPrompt 用户提示词
     * @return 收集到的图片资源列表
     */
    public List<ImageResource> collectImages(String userPrompt) {
        try {
            return imageCollectorService.collectImages(userPrompt);
        } catch (Exception e) {
            log.error("图片收集失败: {}", e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId           应用ID
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        // 先收集图片资源
        List<ImageResource> imageList = collectImages(userMessage);
        // 构建增强提示词
        String enhancedPrompt = buildEnhancedPrompt(userMessage, imageList);
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(enhancedPrompt);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(enhancedPrompt);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 统一入口：根据类型生成并保存代码(流式)
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId           应用ID
     * @return 保存的目录
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        // 先收集图片资源
        List<ImageResource> imageList = collectImages(userMessage);
        // 构建增强提示词
        String enhancedPrompt = buildEnhancedPrompt(userMessage, imageList);
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(enhancedPrompt);
                yield processCodeStream(result, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(enhancedPrompt);
                yield processCodeStream(result, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            case VUE_PROJECT -> {
                TokenStream tokenStream = aiCodeGeneratorService.generateVueProjectCodeStream(appId, enhancedPrompt);
                yield processTokenStream(tokenStream);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 将 TokenStream 转换为 Flux<String>，并传递工具调用信息
     *
     * @param tokenStream TokenStream 对象
     * @return Flux<String> 流式响应
     */
    private Flux<String> processTokenStream(TokenStream tokenStream) {
        return Flux.create(sink -> {
            tokenStream.onPartialResponse((String partialResponse) -> {
                        AiResponseMessage aiResponseMessage = new AiResponseMessage(partialResponse);
                        sink.next(JSONUtil.toJsonStr(aiResponseMessage));
                    })
                    .onPartialToolExecutionRequest((index, toolExecutionRequest) -> {
                        ToolRequestMessage toolRequestMessage = new ToolRequestMessage(toolExecutionRequest);
                        sink.next(JSONUtil.toJsonStr(toolRequestMessage));
                    })
                    .onToolExecuted((ToolExecution toolExecution) -> {
                        ToolExecutedMessage toolExecutedMessage = new ToolExecutedMessage(toolExecution);
                        sink.next(JSONUtil.toJsonStr(toolExecutedMessage));
                    })
                    .onCompleteResponse((ChatResponse response) -> {
                        sink.complete();
                    })
                    .onError((Throwable error) -> {
                        error.printStackTrace();
                        sink.error(error);
                    })
                    .start();
        });
    }


    /**
     * 通用流式代码处理方法
     *
     * @param codeStream 代码流
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        // 当流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        // 实时收集代码片段
        return codeStream
                .doOnNext(codeBuilder::append)
                .doOnComplete(() -> {
                    // 流式返回完成后保存代码
                    try {
                        String completeCode = codeBuilder.toString();
                        //使用执行器解析代码
                        Object executeParser = CodeParserExecutor.executeParser(completeCode, codeGenTypeEnum);
                        // 使用执行器保存代码到文件
                        File savedDir = CodeFileSaverExecutor.executeSaver(executeParser, codeGenTypeEnum, appId);
                        log.info("保存成功，路径为：" + savedDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败: {}", e.getMessage());
                    }
                });
    }

}
