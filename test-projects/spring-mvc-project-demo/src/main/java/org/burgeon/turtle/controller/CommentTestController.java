package org.burgeon.turtle.controller;

import org.burgeon.turtle.model.CommentModel;
import org.burgeon.turtle.model.CommentModel0;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 用来测试注释的收集和导出 -_- !#$%&'()*+,/:;=?@[]\/
 *
 * @author luxiaocong
 * @version 1.0.0 -_- !#$%&'()*+,/:;=?@[]\/
 * @group 注释测试 -_- !#$%&'()*+,/:;=?@[]\/
 * @createdOn 2021/4/13
 */
@RestController
public class CommentTestController {

    /**
     * 注释方法 -_- !#$%&'()*+,/:;=?@[]\/
     * 详细描述:
     * 1...
     * 2...
     * 3...
     * -_- !#$%&'()*+,/:;=?@[]\/
     *
     * @param path 路径参数00 -_- !#$%&'()*+,/:;=?@[]\/
     * @param str uri参数00
     * @param commentModel body参数00
     * @param commentModel0.bool model的bool参数
     * @param commentModel0.model.bool model的model的bool参数
     * @return
     * @version 1.0.1 -_- !#$%&'()*+,/:;=?@[]\/
     * @header x-appid0
     * @header x-appid1 客户端ID -_- !#$%&'()*+,/:;=?@[]\/
     * @header x-appid2 客户端ID 2
     */
    @PostMapping("/comment/{path}")
    public CommentModel comment(/** 路径参数0 */@PathVariable("path") String path,
                                /** uri参数0 */@RequestParam String str,
                                /** uri参数xxx */@ModelAttribute CommentModel0 commentModel0,
                                @RequestBody CommentModel commentModel) {
        return commentModel;
    }

    /**
     * 测试1
     * <p>哈哈哈哈哈</p>
     *
     * @param path 路径参数11
     * @param str uri参数11
     * @param commentModel body参数11
     * @return
     */
    @PostMapping("/comment1/{path}")
    public CommentModel comment1(// 路径参数1
                                 // xxx
                                 @PathVariable("path") String path,
                                 // uri参数1
                                 // 888
                                 @RequestParam String str,
                                 @RequestBody CommentModel commentModel) {
        return commentModel;
    }

    /**
     * 测试2
     * <ol>
     * <li>第一点</li>
     * <li>第二点</li>
     * </ol>
     *
     * @param path 路径参数2
     * @param str uri参数2
     * @param commentModel body参数2
     * @return
     */
    @PostMapping("/comment2/{path}")
    public CommentModel comment2(@PathVariable("path") String path,
                                 @RequestParam String str,
                                 @RequestBody CommentModel commentModel) {
        return commentModel;
    }

    /**
     * 测试3
     *
     * @param path
     * @param str
     * @param commentModel
     * @return
     */
    @PostMapping("/comment3/{path}")
    public List<CommentModel> comment3(@PathVariable("path") String path,
                                       @RequestParam String str,
                                       @RequestBody CommentModel commentModel) {
        return Arrays.asList(commentModel);
    }

    /**
     * 测试4
     *
     * @param path
     * @param str
     * @param commentModel
     * @return
     */
    @PostMapping("/comment4/{path}")
    public List<String> comment4(@PathVariable("path") String path,
                                 @RequestParam String str,
                                 @RequestBody CommentModel commentModel) {
        return Arrays.asList("xxx");
    }

    // 测试5
    @PostMapping("/comment5/{path}")
    public boolean comment5(@PathVariable("path") String path,
                            @RequestParam String str,
                            @RequestBody CommentModel commentModel) {
        return true;
    }

}
