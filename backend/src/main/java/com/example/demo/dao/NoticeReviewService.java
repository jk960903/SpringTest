package com.example.demo.dao;

import com.example.demo.dto.Notice.DeleteNoticeReviewRequest;
import com.example.demo.dto.Notice.GetNoticeDetailRequest;
import com.example.demo.dto.Notice.UpdateNoticeReviewRequest;
import com.example.demo.vo.notice.ReadNoticeReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeReviewService {

    private NoticeReviewRepository noticeReviewRepository;

    @Autowired
    public NoticeReviewService(NoticeReviewRepository noticeReviewRepository){
        this.noticeReviewRepository=noticeReviewRepository;
    }

    public int AddNoticeReview(ReadNoticeReviewVO noticeReviewVO) throws Exception{
        try{
            noticeReviewRepository.save(noticeReviewVO);
        }catch(Exception e){
            throw new Exception("INTERVAL SERVER ERROR");
        }
        return 1;
    }
    public List<ReadNoticeReviewVO> GetNoticeReviewList(GetNoticeDetailRequest noticeReviewRequest) throws Exception{
        List<ReadNoticeReviewVO> noticeReviewVOList=null;
        try{
            noticeReviewVOList=noticeReviewRepository.findNoticeReviewVOByNoticeidxAndIsDeleted(noticeReviewRequest.getNoticeIdx(),1);
        }catch(Exception e){
            throw new Exception("INTERNAL SERVER ERROR");
        }
        return noticeReviewVOList;
    }



    public ReadNoticeReviewVO UpdateNoticeReview(UpdateNoticeReviewRequest updateNoticeReviewRequest) throws IllegalAccessException,IndexOutOfBoundsException,Exception{
        ReadNoticeReviewVO noticeReviewVO;
        try{
            noticeReviewVO = noticeReviewRepository.findByIdxAndIsDeleted(updateNoticeReviewRequest.getIdx(),1).get(0);
            noticeReviewVO.checkLoginValidate(updateNoticeReviewRequest.getIdx());
            noticeReviewVO.setContent(updateNoticeReviewRequest.getContent());
            noticeReviewRepository.save(noticeReviewVO);
        }catch(IllegalAccessException e){
            throw new IllegalAccessException("자신의 댓글만 수정할수 있습니다.");
        } catch(IndexOutOfBoundsException e){
            throw new IndexOutOfBoundsException("NO DATA");
        } catch (Exception e){
            throw new Exception("INTERVAL SERVER ERROR");
        }
        return noticeReviewVO;
    }
    public int DeleteNoticeReview(DeleteNoticeReviewRequest deleteNoticeReviewRequest) throws IndexOutOfBoundsException, IllegalAccessException ,Exception{
        ReadNoticeReviewVO noticeReviewVO;
        //delete 시도
        try{
            noticeReviewVO = noticeReviewRepository.findByIdxAndIsDeleted(deleteNoticeReviewRequest.getIdx(),1).get(0);
            deleteNoticeReviewRequest.CheckLoginValidate(noticeReviewVO.getMemberidx());
            noticeReviewVO.setIsDeleted(9);
            noticeReviewRepository.save(noticeReviewVO);
        }catch(IndexOutOfBoundsException e){
            throw new IndexOutOfBoundsException("NO DATA");
        }catch(IllegalAccessException e){
            throw new IllegalAccessException("No Token or Token Expired");
        }
        catch(Exception e){
            throw new Exception("INTERNAL SERVER ERROR");
        }
        return 1;
    }

}
