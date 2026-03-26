package com.fundtogether.controller;

import com.fundtogether.common.Result;
import com.fundtogether.dto.UpdateCreateDTO;
import com.fundtogether.entity.ProjectUpdate;
import com.fundtogether.security.LoginUser;
import com.fundtogether.service.ProjectUpdateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectUpdateController {

    @Autowired
    private ProjectUpdateService projectUpdateService;

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @PostMapping("/sponsor/update/publish")
    public Result<?> publishUpdate(@RequestBody @Valid UpdateCreateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        projectUpdateService.publishUpdate(getCurrentUserId(), dto.getProjectId(), dto.getTitle(), dto.getContent(), dto.getImages());
        return Result.success("动态发布成功");
    }

    @GetMapping("/public/project/{projectId}/updates")
    public Result<List<ProjectUpdate>> getProjectUpdates(@PathVariable Long projectId) {
        return Result.success(projectUpdateService.getProjectUpdates(projectId));
    }
}
