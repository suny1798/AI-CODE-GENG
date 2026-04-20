package com.suny.aicodegeng.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.suny.aicodegeng.model.dto.app.AppQueryRequest;
import com.suny.aicodegeng.model.entity.App;
import com.suny.aicodegeng.model.entity.User;
import com.suny.aicodegeng.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 服务层。
 *
 * @author suny
 */
public interface AppService extends IService<App> {

    String deployApp(Long appId, User loginUser);

    void generateAppScreenshotAsync(Long appId, String appUrl);

    /**
     * 根据App实体对象获取对应的AppVO（View Object）对象
     * @param app App实体对象，包含应用的基本信息
     * @return AppVO 视图对象，用于前端展示，可能包含格式化或额外处理后的数据
     */
    AppVO getAppVO(App app);

    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 通过对话生成代码
     *
     * @param appId     应用ID
     * @param message   用户提示词
     * @param loginUser 登录用户
     * @return 代码生成结果
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);
}
