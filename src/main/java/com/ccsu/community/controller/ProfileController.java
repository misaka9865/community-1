package com.ccsu.community.controller;

import com.ccsu.community.dto.MyUserDTO;
import com.ccsu.community.dto.PaginationDTO;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.service.NotificationService;
import com.ccsu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 华华
 */
@Controller
public class ProfileController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;
    @Autowired
    NotificationService notificationService;

    private static final String QUESTIONS = "questions";
    private static final String REPLIES = "replies";

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "8") Integer size){

        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        if(QUESTIONS.equals(action)){
            //展示所有我的问题、提问
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            PaginationDTO pagination = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination",pagination);
        }else if(REPLIES.equals(action)){
            //展示所有我的回复
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            PaginationDTO pagination = notificationService.list(user.getId(), page, size);
            notificationService.read(user);
            model.addAttribute("pagination",pagination);
        }
        return "profile";
    }

    @RequestMapping("/profile/people")
    public String goPersonalInfor(HttpServletRequest request,
                                  Model model){
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        MyUserDTO myUserDTO = new MyUserDTO();
        myUserDTO.setAvatarUrl(user.getAvatarUrl());
        myUserDTO.setBio(user.getBio());
        myUserDTO.setLoginName(user.getLoginName());

        model.addAttribute("user",myUserDTO);
        model.addAttribute("section","people");
        model.addAttribute("sectionName","个人资料");
        return "personalInform";
    }
}
