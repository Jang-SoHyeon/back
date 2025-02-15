package com.won.myongjiCamp.controller.api;

import com.won.myongjiCamp.config.auth.PrincipalDetail;
import com.won.myongjiCamp.dto.request.CommentDto;
import com.won.myongjiCamp.dto.response.CommentResponseDto;
import com.won.myongjiCamp.dto.response.ResponseDto;
import com.won.myongjiCamp.model.board.Comment;
import com.won.myongjiCamp.service.CommentService;
import com.won.myongjiCamp.service.FcmService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;
    private final FcmService fcmService;

    //댓글 작성
    @PostMapping("/api/auth/recruit/{id}/comment")
    public ResponseDto<String> createComment(@RequestBody @Valid CommentDto commentDto, @AuthenticationPrincipal PrincipalDetail principal, @PathVariable Long id) throws IOException {
        commentService.create(commentDto,principal.getMember(),id);
        fcmService.sendNotification(principal.getMember(), commentDto, id);
        return new ResponseDto<String>(HttpStatus.OK.value(), "댓글 작성 완료");
    }

    //댓글 삭제
    @DeleteMapping("/api/auth/recruit/{board_id}/comment/{comment_id}")
    public ResponseDto<String> deleteComment(@PathVariable("board_id") Long board_id, @PathVariable("comment_id") Long comment_id){
        commentService.delete(board_id, comment_id);
        return new ResponseDto<String>(HttpStatus.OK.value(), "댓글이 삭제되었습니다.");
    }


    //댓글 전체 조회
    @GetMapping("/api/auth/recruit/{board_id}/comment")
    private Result CommentList(@PathVariable("board_id") Long id,@AuthenticationPrincipal PrincipalDetail principalDetail){

        List<CommentResponseDto> result = new ArrayList<>();
        Map<Long, CommentResponseDto> map = new HashMap<>();
        List<Comment> commentList = commentService.commentAll(id);

        commentList.stream().forEach(c->{
            CommentResponseDto rDto = convertResponseCommentToDto(c);
            map.put(c.getId(), rDto);
            if(c.getCdepth() == 1){// 댓글이 부모가 있으면
                map.get(c.getParent().getId()).getChildren().add(rDto);
            }
            else{
                result.add(rDto);
            }

        });

        return new Result(result);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    public CommentResponseDto convertResponseCommentToDto(Comment comment){
        return new CommentResponseDto(
                comment.getId(),
                comment.getBoard().getId(),
                comment.getContent(),
                comment.getCreatedDate(),
                comment.getWriter().getId(),
                comment.getWriter().getNickname(), 
                comment.getWriter().getProfileIcon(),
                comment.getIsSecret(),

                new ArrayList<>(),
                comment.isDelete()
        );

    }



}


