package com.hh.netty.websocket.controller;

import cn.hutool.core.util.RandomUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author huangh
 * @since 2019/10/14
 */
@RestController
public class IndexController {
    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("socket");
        mav.addObject("uid", RandomUtil.randomNumbers(6));
        return mav;
    }
}