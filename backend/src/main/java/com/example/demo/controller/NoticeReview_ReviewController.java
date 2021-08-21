package com.example.demo.controller;


import com.example.demo.JWT.JwtService;
import com.example.demo.dao.NoticeReviewReviewService;
import com.example.demo.vo.Enum.StatusEnum;
import com.example.demo.vo.SendMessage;
import com.example.demo.vo.notice.NoticeReviewReviewVO;
import com.example.demo.vo.notice.NoticeReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/notice-review-review")
public class NoticeReview_ReviewController {

    @Autowired(required = true)
    NoticeReviewReviewService noticeReviewReviewService;

    @Autowired(required = true)
    JwtService jwtService;
    @RequestMapping(value ="/getnotice_reivew_review_list")
    public ResponseEntity<SendMessage<List<NoticeReviewReviewVO>>> getNoticeReviewReviewList(Long notice_review_idx){
        List<NoticeReviewReviewVO> noticeReviewReviewVOList= noticeReviewReviewService.GetNoticeReviewReview(notice_review_idx);
        SendMessage<List<NoticeReviewReviewVO>> message;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        if(notice_review_idx < 0 || notice_review_idx ==null) {
            message = new SendMessage<>(null, StatusEnum.BAD_REQUEST, "BAD REQUEST");
            return new ResponseEntity<>(message, headers, HttpStatus.BAD_REQUEST);
        }
        if(noticeReviewReviewVOList ==null){
            message = new SendMessage<>(null,StatusEnum.INTERNAL_SERVER_ERROOR,"INTERVAL SERVER ERROR");
            return new ResponseEntity<>(message,headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(noticeReviewReviewVOList.size() ==0) {
            message = new SendMessage<>(noticeReviewReviewVOList, StatusEnum.OK, "NO DATA");
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }
        message = new SendMessage<>(noticeReviewReviewVOList,StatusEnum.OK,"OK");
        return new ResponseEntity<>(message,headers,HttpStatus.OK);
    }

    @RequestMapping(value="/addnotice_review_review")
    public ResponseEntity<SendMessage<NoticeReviewReviewVO>> AddNoticeReviewReview(HttpServletRequest request,NoticeReviewReviewVO noticeReviewReviewVO){
        Map<String,Object> auth = jwtService.requestAuthorization(request);
        SendMessage<NoticeReviewReviewVO> message;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json",Charset.forName("UTF-8")));
        if(auth == null){
            message = new SendMessage<>(null,StatusEnum.UNAUTHORIZED,"UNAUTHORZED");
            return new ResponseEntity<>(message,headers,HttpStatus.UNAUTHORIZED);
        }
        noticeReviewReviewVO.setMemberidx(Long.parseLong(((String)auth.get("memberidx"))));
        if(noticeReviewReviewVO.getReviewidx() == null || noticeReviewReviewVO.getReviewidx() < 0 || noticeReviewReviewVO.getMemberidx() == null || noticeReviewReviewVO.getMemberidx() <=0) {
            message = new SendMessage<>(null, StatusEnum.BAD_REQUEST,"BADREQUEST");
            return new ResponseEntity<>(message,headers,HttpStatus.BAD_REQUEST);
        }
        NoticeReviewReviewVO result=noticeReviewReviewService.AddNoticeReviewReview(noticeReviewReviewVO);
        if(result == null){
            message = new SendMessage<>(null,StatusEnum.INTERNAL_SERVER_ERROOR,"INTERVALSERVERERROR");
            return new ResponseEntity<>(message,headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        message = new SendMessage<>(result,StatusEnum.OK,"OK");
        return new ResponseEntity<>(message,headers,HttpStatus.OK);
    }

    @RequestMapping(value="/updatenotice_review_reivew")
    public ResponseEntity<NoticeReviewReviewVO> UpdateNoticeReviewReviewVO(NoticeReviewReviewVO noticeReviewReviewVO){
        return null;
    }

    @RequestMapping(value="/deletenotice_review_review")
    public ResponseEntity<NoticeReviewReviewVO> DeleteNoticeReviewReviewVO(NoticeReviewReviewVO noticeReviewReviewVO){
        return null;
    }
}
