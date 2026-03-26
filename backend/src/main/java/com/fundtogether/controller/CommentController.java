package com.fundtogether.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fundtogether.common.Result;
import com.fundtogether.dto.CommentCreateDTO;
import com.fundtogether.entity.UserComment;
import com.fundtogether.security.LoginUser;
import com.fundtogether.service.UserCommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private UserCommentService userCommentService;

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @PostMapping("/create")
    public Result<?> addComment(@RequestBody @Valid CommentCreateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        
        userCommentService.addComment(getCurrentUserId(), dto.getProjectId(), dto.getParentId(), dto.getContent());
        return Result.success("评论发布成功");
    }

    @GetMapping("/project/{projectId}")
    public Result<IPage<UserComment>> getProjectComments(@PathVariable Long projectId,
                                                         @RequestParam(defaultValue = "1") Integer current,
                                                         @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(userCommentService.getProjectComments(projectId, current, size));
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteComment(@PathVariable Long id) {
        userCommentService.deleteComment(id, getCurrentUserId());
        return Result.success("评论已删除");
    }

    @PostMapping("/{id}/like")
    public Result<?> likeComment(@PathVariable Long id) {
        userCommentService.likeComment(id, getCurrentUserId());
        return Result.success("点赞成功");
    }
}