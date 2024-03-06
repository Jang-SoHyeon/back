package com.won.myongjiCamp.repository;

import com.won.myongjiCamp.model.Member;
import com.won.myongjiCamp.model.board.Board;
import com.won.myongjiCamp.model.board.Comment;
import com.won.myongjiCamp.model.board.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report,Long> {

    Report findByReporterAndReportedBoard(Member reporter, Board board); // board와 신고한 사람으로 찾기
    Report findByReporterAndReportedComment(Member reporter, Comment comment);
}
