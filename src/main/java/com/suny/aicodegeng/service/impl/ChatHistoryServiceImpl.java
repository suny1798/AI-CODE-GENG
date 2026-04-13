package com.suny.aicodegeng.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.suny.aicodegeng.model.entity.ChatHistory;
import com.suny.aicodegeng.mapper.ChatHistoryMapper;
import com.suny.aicodegeng.service.ChatHistoryService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author suny
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

}
