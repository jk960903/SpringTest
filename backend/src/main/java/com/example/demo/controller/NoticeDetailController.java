package com.example.demo.controller;

import com.example.demo.JWT.JwtService;
import com.example.demo.dao.NoticeDetailService;
import com.example.demo.dao.NoticeReviewReviewService;
import com.example.demo.dao.NoticeReviewService;
import com.example.demo.dao.NoticeService;
import com.example.demo.dto.Notice.DeleteNoticeDetailRequest;
import com.example.demo.dto.Notice.GetNoticeDetailRequest;
import com.example.demo.dto.Notice.UpdateNoticeDetailRequest;
import com.example.demo.vo.Enum.StatusEnum;
import com.example.demo.SendMessage.SendMessage;
import com.example.demo.vo.notice.*;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="api/noticedetail")
@NoArgsConstructor
public class NoticeDetailController {

    private  NoticeDetailService noticeDetailService;


    private  JwtService jwtService;

    private  NoticeService noticeService;

    private  NoticeReviewService noticeReviewService;

    private  NoticeReviewReviewService noticeReviewReviewService;


    @Autowired
    public NoticeDetailController(NoticeDetailService noticeDetailService, JwtService jwtService, NoticeService noticeService, NoticeReviewService noticeReviewService, NoticeReviewReviewService noticeReviewReviewService) {
        this.noticeDetailService = noticeDetailService;
        this.jwtService = jwtService;
        this.noticeService = noticeService;
        this.noticeReviewService = noticeReviewService;
        this.noticeReviewReviewService = noticeReviewReviewService;
    }
    //테스트완
    @GetMapping(value = "/getnoticedetail")
    public ResponseEntity<SendMessage<NoticeResultVO>> GetNoticeDetail(HttpServletRequest request , GetNoticeDetailRequest getNoticeReviewRequest){
        SendMessage<NoticeResultVO> sendMessage = null;
        NoticeDetailVO noticeDetailVO = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        NoticeResultVO noticeResultVO = null;
        NoticeVO noticeVO = null;
        List<ReadNoticeReviewVO> noticeReviewVO;
        List<NoticeReviewResult> noticeReviewResults = new ArrayList<>();
        try{
            noticeVO = noticeService.GetNoticeData(getNoticeReviewRequest.getNoticeIdx()); //공지사항 정보
            noticeDetailVO = noticeDetailService.GetNoticeDetail(getNoticeReviewRequest.getNoticeIdx());//공지사항 상세정보
            noticeReviewVO = noticeReviewService.GetNoticeReviewList(getNoticeReviewRequest);//공지사항 댓글 리스트
            for(int i = 0 ; i<noticeReviewVO.size(); i++){//공지사항 댓글리스트의 idx review_review table 의 review_idx 를 토대로 대댓글 리스트 불러오기
                List<ReadNoticeReviewReviewVO> noticeReviewReviewVOList = noticeReviewReviewService.GetNoticeReviewReviewList(noticeReviewVO.get(i).getIdx());
                noticeReviewResults.add(new NoticeReviewResult(noticeReviewVO.get(i)));
            }
        }catch(IndexOutOfBoundsException e ){
            sendMessage = new SendMessage<>(null,StatusEnum.BAD_REQUEST,e.getMessage());
            return new ResponseEntity<>(sendMessage,headers,HttpStatus.BAD_REQUEST);
        }catch(NullPointerException e){
            sendMessage = new SendMessage<>(null,StatusEnum.BAD_REQUEST,e.getMessage());
            return new ResponseEntity<>(sendMessage,headers,HttpStatus.BAD_REQUEST);
        } catch(Exception e){// 해당 목표를 못찾음
            sendMessage = new SendMessage<>(null,StatusEnum.INTERNAL_SERVER_ERROOR,e.getMessage());
            return new ResponseEntity<>(sendMessage,headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        noticeResultVO = new NoticeResultVO(noticeVO,noticeDetailVO,noticeReviewResults);
        sendMessage = new SendMessage<>(noticeResultVO,StatusEnum.OK,"OK");
        return new ResponseEntity<>(sendMessage,headers,HttpStatus.OK);


    }

    //테스트 완
    @PatchMapping(value="/updatenoticedetail")
    public ResponseEntity<SendMessage<NoticeDetailVO>> UpdateNoticeDetail(HttpServletRequest request, UpdateNoticeDetailRequest noticeDetailRequest){
        Map<String,Object> auth;
        SendMessage<NoticeDetailVO> sendMessage=null;
        HttpHeaders headers = new HttpHeaders();
        NoticeDetailVO noticeDetailVO=null;
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        try{
            noticeDetailRequest.CheckValidate();
            noticeDetailVO=noticeDetailService.UpdateNoticeDetail(noticeDetailRequest);
        }catch(NullPointerException e){
            sendMessage = new SendMessage<>(null,StatusEnum.BAD_REQUEST,e.getMessage());
            return new ResponseEntity<>(sendMessage,headers,HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            sendMessage = new SendMessage<>(null,StatusEnum.INTERNAL_SERVER_ERROOR,e.getMessage());
            return new ResponseEntity<>(sendMessage,headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        sendMessage = new SendMessage<>(noticeDetailVO,StatusEnum.OK,"OK");
        return new ResponseEntity<>(sendMessage,headers,HttpStatus.OK);
    }


    @PatchMapping(value="/deletenotice")
    public ResponseEntity<SendMessage<Integer>> DeleteNoticeDetail(HttpServletRequest request, DeleteNoticeDetailRequest deleteNoticeDetailRequest){
        Map<String,Object> auth;
        SendMessage<Integer> sendMessage=null;
        HttpHeaders headers = new HttpHeaders();
        NoticeDetailVO noticeDetailVO=null;
        try{
            deleteNoticeDetailRequest.CheckValidate();
            noticeDetailVO=noticeDetailService.GetNoticeDetail(deleteNoticeDetailRequest.getIdx());
            noticeService.DeleteNotice(noticeDetailVO.getNoticeidx());
            noticeDetailService.DeleteNoticeDetail(noticeDetailVO);
        }catch(NullPointerException e){
            sendMessage = new SendMessage<>(0,StatusEnum.BAD_REQUEST,e.getMessage());
            return new ResponseEntity<>(sendMessage,headers,HttpStatus.BAD_REQUEST);
        }catch(IndexOutOfBoundsException e){
            sendMessage =new SendMessage<>(0,StatusEnum.BAD_REQUEST,e.getMessage());
            return new ResponseEntity<>(sendMessage,headers,HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            sendMessage = new SendMessage<>(0,StatusEnum.BAD_REQUEST,e.getMessage());
            return new ResponseEntity<>(sendMessage,headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        sendMessage= new SendMessage<>(1,StatusEnum.OK,"OK");
        return new ResponseEntity<>(sendMessage,headers,HttpStatus.OK);
    }

}
