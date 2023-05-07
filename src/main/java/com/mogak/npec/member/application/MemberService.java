package com.mogak.npec.member.application;

import com.mogak.npec.auth.domain.Encryptor;
import com.mogak.npec.common.aws.S3Helper;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.dto.MemberCreateRequest;
import com.mogak.npec.member.dto.ProfileImageResponse;
import com.mogak.npec.member.exception.MemberAlreadySavedException;
import com.mogak.npec.member.exception.MemberNotFoundException;
import com.mogak.npec.member.exception.S3UploadException;
import com.mogak.npec.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
public class MemberService {
    private static final String PROFILE_PATH = "profiles";

    private final MemberRepository memberRepository;
    private final Encryptor encryptor;

    private final S3Helper s3Helper;


    public MemberService(MemberRepository memberRepository, Encryptor encryptor, S3Helper s3Helper) {
        this.memberRepository = memberRepository;
        this.encryptor = encryptor;
        this.s3Helper = s3Helper;
    }

    @Transactional
    public void createMember(MemberCreateRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new MemberAlreadySavedException("이미 등록된 이메일입니다.");
        }
        String encryptPassword = encryptor.encrypt(request.getPassword());

        memberRepository.save(new Member(request.getNickname(), request.getEmail(), encryptPassword));
    }

    @Transactional
    public void updateMember(Long memberId, String nickName) {
        Member findMember = findMember(memberId);

        findMember.changeNickname(nickName.trim());
    }

    @Transactional // todo 외부 Api 사용시 위험하지 않을까
    public ProfileImageResponse updateProfileImage(Long memberId, MultipartFile file) {
        Member findMember = findMember(memberId);

        String extension = Objects.requireNonNull(file.getContentType()).split("/")[1];
        String imageName = UUID.randomUUID() + "." + extension;
        String path = findMember.getEmail() + "/" + PROFILE_PATH;

        if (s3Helper.uploadImage(imageName, path, extension, file)) {
            String imageUrl = s3Helper.getPath(path, imageName);
            findMember.changeImageUrl(imageUrl);

            return new ProfileImageResponse(imageUrl);
        }
        throw new S3UploadException("s3 업로드에 실패했습니다.");
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
