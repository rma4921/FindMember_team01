package com.estsoft.findmember_team01.information.controller;

import com.estsoft.findmember_team01.information.domain.Comment;
import com.estsoft.findmember_team01.information.domain.Information;
import com.estsoft.findmember_team01.information.domain.Status;
import com.estsoft.findmember_team01.information.domain.TargetType;
import com.estsoft.findmember_team01.information.dto.CommentRequest;
import com.estsoft.findmember_team01.information.dto.CommentView;
import com.estsoft.findmember_team01.information.dto.InformationRequest;
import com.estsoft.findmember_team01.information.dto.InformationView;
import com.estsoft.findmember_team01.information.service.CommentService;
import com.estsoft.findmember_team01.information.service.InformationService;
import com.estsoft.findmember_team01.information.service.ReportService;
import com.estsoft.findmember_team01.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/information")
@RequiredArgsConstructor
public class InformationViewController {

    private final InformationService informationService;
    private final CommentService commentService;
    private final ReportService reportService;

    // 상세 페이지
    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id,
        Model model,
        @AuthenticationPrincipal Member loginMember) {
        Information info = informationService.findInformationById(id);

        model.addAttribute("post", InformationView.from(info));
        model.addAttribute("comments", info.getComments().stream()
            .map(CommentView::from)
            .collect(Collectors.toList()));
        model.addAttribute("loginMemberId", loginMember != null ? loginMember.getId() : null);

        return "informationdetail";
    }

    // 글쓰기 폼
    @GetMapping("/write")
    public String showWriteForm(Model model) {
        InformationRequest dto = new InformationRequest();
        model.addAttribute("informationRequest", dto);
        return "informationWrite";
    }

    // 글 등록 후 리다이렉션
    @PostMapping
    public String saveInformation(@ModelAttribute InformationRequest request,
        @AuthenticationPrincipal Member loginMember) {
        request.setMemberId(loginMember.getId());
        Information saved = informationService.addInformation(request);
        return "redirect:/information/" + saved.getInformationId();
    }

    // 댓글 등록
    @PostMapping("/comments")
    public String addComment(@RequestParam Long informationId,
        @AuthenticationPrincipal Member loginMember,
        @RequestParam String content) {
        CommentRequest request = new CommentRequest(content);
        commentService.addComment(informationId, loginMember.getId(), request);
        return "redirect:/information/" + informationId;
    }

    //글 목록 10개 이상되면 다음 페이지로 넘어가는 기능
    @GetMapping
    public String showList(@RequestParam(defaultValue = "0") int page,
        Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createAt").descending());
        Page<Information> pageInfo = informationService.findAllPaged(pageable);

        List<InformationView> posts = pageInfo.getContent().stream()
            .map(InformationView::from)
            .toList();

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", pageInfo.getNumber());
        model.addAttribute("totalPages", pageInfo.getTotalPages());
        return "information";
    }

    // 수정 폼 페이지
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
        Model model,
        @AuthenticationPrincipal Member loginUser) {
        Information information = informationService.findInformationById(id);

        // 작성자 검증
        if (!information.getMember().getId().equals(loginUser.getId())) {
            throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
        }

        InformationRequest request = new InformationRequest(
            information.getTitle(),
            information.getContent(),
            information.getMember().getId()
        );

        model.addAttribute("informationRequest", request);
        model.addAttribute("informationId", id);
        return "informationEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateInformation(@PathVariable Long id,
        @ModelAttribute InformationRequest request) {
        informationService.updateInformation(id, request);
        return "redirect:/information/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteInformation(@PathVariable Long id, @AuthenticationPrincipal Member member) {
        Information info = informationService.findInformationById(id);
        if (!info.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }
        informationService.deleteInformation(id);
        return "redirect:/information";
    }

    @GetMapping("/search")
    public String search(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        Status filterStatus = null;
        if ("SOLVED".equalsIgnoreCase(status)) {
            filterStatus = Status.SOLVED;
        } else if ("UNSOLVED".equalsIgnoreCase(status)) {
            filterStatus = Status.UNSOLVED;
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by("createAt").descending());
        Page<Information> filtered = informationService.searchByStatusAndKeywordPaged(filterStatus,
            keyword, pageable);

        List<InformationView> posts = filtered.stream()
            .map(InformationView::from)
            .toList();

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", filtered.getNumber());
        model.addAttribute("totalPages", filtered.getTotalPages());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        return "information";
    }

    @PostMapping("/solve/{id}")
    public String solveInformation(@PathVariable Long id,
        @AuthenticationPrincipal Member member) {
        informationService.markAsSolved(id, member.getId());
        return "redirect:/information/" + id;
    }

    @PostMapping("/comments/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId,
        @AuthenticationPrincipal Member loginMember) {

        Comment comment = commentService.findById(commentId);
        Information info = comment.getInformation(); // 댓글이 달린 글

        // 글 작성자만 삭제 가능
        if (!info.getMember().getId().equals(loginMember.getId())) {
            throw new AccessDeniedException("글 작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentService.deleteComment(commentId);
        return "redirect:/information/" + info.getInformationId();
    }

    @PostMapping("/report")
    public String handleReport(
        @RequestParam("targetType") String type,
        @RequestParam("targetId") Long targetId,
        @RequestParam("reason") String reason,
        @AuthenticationPrincipal Member loginMember
    ) {
        if (loginMember == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        TargetType targetType = TargetType.valueOf(type.toUpperCase());

        reportService.submitReport(loginMember.getId(), targetType, targetId, reason);

        String redirectUrl = (targetType == TargetType.POST)
            ? "/information/" + targetId
            : "/information/" + reportService.findPostIdByCommentId(targetId);

        return "redirect:" + redirectUrl;
    }

}
