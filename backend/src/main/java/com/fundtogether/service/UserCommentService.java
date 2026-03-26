package com.fundtogether.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fundtogether.entity.UserComment;

public interface UserCommentService extends IService<UserComment> {
    void addComment(Long userId, Long projectId, Long parentId, String content);
    IPage<UserComment> getProjectComments(Long projectId, Integer current, Integer size);
    void deleteComment(Long commentId, Long userId);
    void likeComment(Long commentId, Long userId);
}