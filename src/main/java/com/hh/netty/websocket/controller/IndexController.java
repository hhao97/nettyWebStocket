package com.hh.netty.websocket.controller;

import cn.hutool.core.util.RandomUtil;
import com.hh.netty.websocket.send.sendBySocket;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

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

    public static String sendBySocket(String text, String ip, int port)
            throws Exception {
        System.out.println("开始建立socket连接");
        Socket socket = new Socket(ip, port);
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "GBK");
        // 2.获取客户端输出流
        OutputStream dos = socket.getOutputStream();
        System.out.println("连上服务端");
        // 3.向服务端发送消息
        dos.write(text.getBytes());
        dos.flush();
        System.out.println("成功向服务器发送消息");
        // 4.获取输入流，并读取服务器端的响应信息
        BufferedReader br = new BufferedReader(isr);
        String returnInfo = br.readLine();
        System.out.println("服务器端返回数据为：" + returnInfo);
        // 4.关闭资源
        br.close();
        isr.close();
        is.close();
        dos.close();
        socket.close();
        return returnInfo;
    }

}