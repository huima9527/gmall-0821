package com.atguigu.gmall.search.controller;

import com.atguigu.gmall.search.pojo.SearchParmVo;
import com.atguigu.gmall.search.pojo.SearchResponseVo;
import com.atguigu.gmall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huima9527
 * @create 2021-02-04 22:30
 */
@Controller
@RequestMapping("search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping
//    @ResponseBody
    private String search(SearchParmVo ParmVo,Model model){
        System.out.println(ParmVo);
        SearchResponseVo responseVo = this.searchService.search(ParmVo);
        System.out.println(responseVo);
        model.addAttribute("response",responseVo);
        model.addAttribute("searchParam",ParmVo);
        return "search";
    }
}
