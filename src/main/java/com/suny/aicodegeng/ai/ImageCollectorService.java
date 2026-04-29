package com.suny.aicodegeng.ai;

import com.suny.aicodegeng.langgraph4j.ai.ImageCollectionPlanService;
import com.suny.aicodegeng.langgraph4j.model.ImageCollectionPlan;
import com.suny.aicodegeng.langgraph4j.model.ImageResource;
import com.suny.aicodegeng.langgraph4j.tools.ImageSearchTool;
import com.suny.aicodegeng.langgraph4j.tools.LogoGeneratorTool;
import com.suny.aicodegeng.langgraph4j.tools.MermaidDiagramTool;
import com.suny.aicodegeng.langgraph4j.tools.UndrawIllustrationTool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 图片收集服务
 * 封装图片收集的完整流程：计划 + 收集
 */
@Slf4j
@Service
public class ImageCollectorService {

    @Resource
    private ImageCollectionPlanService planService;

    @Resource
    private ImageSearchTool imageSearchTool;

    @Resource
    private UndrawIllustrationTool undrawIllustrationTool;

    @Resource
    private MermaidDiagramTool mermaidDiagramTool;

    @Resource
    private LogoGeneratorTool logoGeneratorTool;

    /**
     * 收集图片
     * 第一步：根据用户提示词制定图片收集计划
     * 第二步：并发执行各种图片收集任务
     *
     * @param userPrompt 用户提示词
     * @return 收集到的图片资源列表
     */
    public List<ImageResource> collectImages(String userPrompt) {
        List<ImageResource> collectedImages = new ArrayList<>();

        try {
            // 第一步：获取图片收集计划
            ImageCollectionPlan plan = planService.planImageCollection(userPrompt);
            log.info("获取到图片收集计划，开始并发执行");

            // 第二步：并发执行各种图片收集任务
            List<CompletableFuture<List<ImageResource>>> futures = new ArrayList<>();

            // 并发执行内容图片搜索
            if (plan.getContentImageTasks() != null) {
                for (ImageCollectionPlan.ImageSearchTask task : plan.getContentImageTasks()) {
                    futures.add(CompletableFuture.supplyAsync(() ->
                            imageSearchTool.searchContentImages(task.query())));
                }
            }

            // 并发执行插画图片搜索
            if (plan.getIllustrationTasks() != null) {
                for (ImageCollectionPlan.IllustrationTask task : plan.getIllustrationTasks()) {
                    futures.add(CompletableFuture.supplyAsync(() ->
                            undrawIllustrationTool.searchIllustrations(task.query())));
                }
            }

            // 并发执行架构图生成
            if (plan.getDiagramTasks() != null) {
                for (ImageCollectionPlan.DiagramTask task : plan.getDiagramTasks()) {
                    futures.add(CompletableFuture.supplyAsync(() ->
                            mermaidDiagramTool.generateMermaidDiagram(task.mermaidCode(), task.description())));
                }
            }

            // 并发执行Logo生成
            if (plan.getLogoTasks() != null) {
                for (ImageCollectionPlan.LogoTask task : plan.getLogoTasks()) {
                    futures.add(CompletableFuture.supplyAsync(() ->
                            logoGeneratorTool.generateLogos(task.description())));
                }
            }

            // 等待所有任务完成并收集结果
            if (!futures.isEmpty()) {
                CompletableFuture<Void> allTasks = CompletableFuture.allOf(
                        futures.toArray(new CompletableFuture[0]));
                allTasks.join();

                // 收集所有结果
                for (CompletableFuture<List<ImageResource>> future : futures) {
                    List<ImageResource> images = future.get();
                    if (images != null) {
                        collectedImages.addAll(images);
                    }
                }
            }

            log.info("并发图片收集完成，共收集到 {} 张图片", collectedImages.size());

        } catch (Exception e) {
            log.error("图片收集失败: {}", e.getMessage(), e);
        }

        return collectedImages;
    }
}
