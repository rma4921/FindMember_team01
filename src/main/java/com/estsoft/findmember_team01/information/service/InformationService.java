package com.estsoft.findmember_team01.information.service;

import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.information.domain.Information;
import com.estsoft.findmember_team01.information.domain.Status;
import com.estsoft.findmember_team01.information.dto.InformationRequest;
import com.estsoft.findmember_team01.information.repository.InformationRepository;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InformationService {

    private final MemberRepository memberRepository;
    private final InformationRepository informationRepository;

    public InformationService(InformationRepository informationRepository,
        MemberRepository memberRepository) {
        this.informationRepository = informationRepository;
        this.memberRepository = memberRepository;
    }

    public List<Information> findAllInformation() {
        return informationRepository.findAll(Sort.by(Direction.DESC, "createAt"));
    }

    public Information findInformationById(Long id) {
        return informationRepository.findById(id)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.INFORMATION_NOT_FOUND));
    }

    public Information addInformation(InformationRequest request) {
        var member = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.MEMBER_NOT_FOUND));

        var information = Information.builder().title(request.getTitle())
            .content(request.getContent()).member(member).build();

        return informationRepository.save(information);
    }

    @Transactional
    public Information updateInformation(Long id, InformationRequest request) {
        var information = informationRepository.findById(id)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.INFORMATION_NOT_FOUND));
        information.updateInformation(request.getTitle(), request.getContent());
        return information;
    }

    public void deleteInformation(Long id) {
        informationRepository.deleteById(id);
    }

    public Page<Information> findAllPaged(Pageable pageable) {
        return informationRepository.findByHideStatusFalse(pageable);
    }

    public Page<Information> searchByStatusAndKeywordPaged(Status status, String keyword,
        Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = "";
        }
        return informationRepository.findByStatusAndKeywordPaged(status, keyword, pageable);
    }

    @Transactional
    public void markAsSolved(Long id, Long memberId) {
        var info = informationRepository.findById(id)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.INFORMATION_NOT_FOUND));
        if (!info.getMember().getId().equals(memberId)) {
            throw new GlobalException(GlobalExceptionType.ONLY_AUTHOR_CAN_UPDATE);
        }
        info.setStatus(Status.SOLVED);
    }

    public Page<Information> searchByKeywordPaged(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createAt").descending());
        if (keyword != null && !keyword.trim().isEmpty()) {
            return informationRepository.findByTitleContainingOrContentContaining(keyword, keyword,
                pageable);
        } else {
            return informationRepository.findAll(pageable);
        }
    }

    @Transactional
    public void updateHideStatus(Long id, boolean hideStatus) {
        Information information = informationRepository.findById(id)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.INFORMATION_NOT_FOUND));

        information.toggleHideStatus(hideStatus);
    }

    public Information findVisibleInformationById(Long id) {
        Information info = informationRepository.findById(id)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.INFORMATION_NOT_FOUND));

        if (info.getHideStatus()) {
            throw new GlobalException(GlobalExceptionType.CONTENT_HIDDEN);
        }

        return info;
    }

    public Page<Information> searchVisibleByStatusAndKeywordPaged(Status status, String keyword,
        Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = "";
        }
        return informationRepository.findVisibleByStatusAndKeyword(status, keyword, pageable);
    }
}
