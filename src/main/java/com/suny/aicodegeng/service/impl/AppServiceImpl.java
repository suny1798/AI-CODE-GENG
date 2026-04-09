package com.suny.aicodegeng.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.suny.aicodegeng.model.entity.App;
import com.suny.aicodegeng.mapper.AppMapper;
import com.suny.aicodegeng.service.AppService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author suny
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

}
