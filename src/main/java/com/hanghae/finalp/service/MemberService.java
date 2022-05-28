package com.hanghae.finalp.service;

import com.hanghae.finalp.config.exception.customexception.entity.MemberNotExistException;
import com.hanghae.finalp.entity.Member;
import com.hanghae.finalp.entity.dto.MemberDto;
import com.hanghae.finalp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    /**
     * 내 프로필 조회
     */
    public MemberDto.ProfileRes getMyProfile(Long memberId) {
       Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistException::new);
        return new MemberDto.ProfileRes(member.getId(), member.getUsername(), member.getImageUrl());
    }

    /**
     * 프로필 수정
     */
    @Transactional()
    public MemberDto.ProfileRes editMyProfile(String username, MultipartFile file, Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistException::new);

        String fullFilePath = s3Service.uploadFile(file);
        if(fullFilePath == null) fullFilePath = "https://mj-file-bucket.s3.ap-northeast-2.amazonaws.com/memberDefaultImg.png";
        member.patchMember(username, fullFilePath);

        String currentFilePath = member.getImageUrl();
        s3Service.deleteFile(currentFilePath);

        return new MemberDto.ProfileRes(member.getId(), member.getUsername(), member.getImageUrl());
    }

    /**
     * 회원탈퇴
     */
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistException::new);
        s3Service.deleteFile(member.getImageUrl());
        memberRepository.deleteById(memberId);
    }
}
