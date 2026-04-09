package com.suny.aicodegeng.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.suny.aicodegeng.model.dto.app.AppQueryRequest;
import com.suny.aicodegeng.model.entity.App;
import com.suny.aicodegeng.model.vo.AppVO;

import java.util.List;

/**
 *  服务层。
 *
 * @author suny
 */
public interface AppService extends IService<App> {

    AppVO getAppVO(App app);

    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    List<AppVO> getAppVOList(List<App> appList);
}
