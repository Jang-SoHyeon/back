package com.won.myongjiCamp.service;

import com.won.myongjiCamp.exception.MemberNoMatchException;
import com.won.myongjiCamp.model.Member;
import com.won.myongjiCamp.model.Resume;
import com.won.myongjiCamp.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    
    //이력서 작성
    @Transactional
    public void write(String title, String content, String url, Member member) {
        Resume resume = Resume.builder()
                .title(title)
                .content(content)
                .url(url)
                .member(member)
                .build();

        resumeRepository.save(resume);
    }
    //이력서 수정
    @Transactional
    public void update(String title, String content, String url, long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이력서가 존재하지 않습니다."));
        resume.setTitle(title);
        resume.setContent(content);
        resume.setUrl(url);
        resume.setCreateDate(new Timestamp(System.currentTimeMillis()));
    }
    //이력서 삭제
    @Transactional
    public void delete(long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이력서가 존재하지 않습니다."));
        resumeRepository.delete(resume);
    }

    //이력서 목록조회
    public List<Resume> resumeAll(Member member) {
        return resumeRepository.findByMember(member);
    }

    //이력서 상세조회
    public Resume resumeDetail(long id, Member member) throws MemberNoMatchException {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이력서가 존재하지 않습니다."));
        if(!member.getId().equals(resume.getMember().getId()))
            throw new MemberNoMatchException("본인 이력서만 열람할 수 있습니다.");
        return resume;
    }
}
