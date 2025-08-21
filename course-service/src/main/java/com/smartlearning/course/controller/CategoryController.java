package com.smartlearning.course.controller;

import com.smartlearning.common.result.Result;
import com.smartlearning.course.entity.Category;
import com.smartlearning.course.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "课程分类相关接口")
public class CategoryController {
    
    private final CategoryService categoryService;
    
    /**
     * 获取分类树
     */
    @GetMapping("/tree")
    @Operation(summary = "获取分类树", description = "获取完整的分类树结构")
    public Result<List<Category>> getCategoryTree() {
        log.info("获取分类树请求");
        List<Category> categories = categoryService.getCategoryTree();
        return Result.success("查询成功", categories);
    }
    
    /**
     * 根据父ID获取子分类
     */
    @GetMapping("/children/{parentId}")
    @Operation(summary = "获取子分类", description = "根据父分类ID获取子分类列表")
    public Result<List<Category>> getChildrenCategories(@Parameter(description = "父分类ID") @PathVariable Long parentId) {
        log.info("获取子分类请求: parentId={}", parentId);
        List<Category> categories = categoryService.getChildrenCategories(parentId);
        return Result.success("查询成功", categories);
    }
    
    /**
     * 根据ID获取分类详情
     */
    @GetMapping("/{categoryId}")
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详细信息")
    public Result<Category> getCategoryById(@Parameter(description = "分类ID") @PathVariable Long categoryId) {
        log.info("获取分类详情请求: categoryId={}", categoryId);
        Category category = categoryService.findById(categoryId);
        return Result.success("查询成功", category);
    }
}
