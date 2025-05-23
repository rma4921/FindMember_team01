package com.estsoft.findmember_team01.information.controller;

import com.estsoft.findmember_team01.comment.domain.Comment;
import com.estsoft.findmember_team01.comment.dto.CommentRequest;
import com.estsoft.findmember_team01.comment.dto.CommentView;
import com.estsoft.findmember_team01.comment.service.CommentService;
import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.information.domain.Information;
import com.estsoft.findmember_team01.information.domain.Status;
import com.estsoft.findmember_team01.information.dto.InformationRequest;
import com.estsoft.findmember_team01.information.dto.InformationView;
import com.estsoft.findmember_team01.information.service.InformationService;
import com.estsoft.findmember_team01.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/information")
@RequiredArgsConstructor
public class InformationViewController {

    private final InformationService informationService;
    private final CommentService commentService;

    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model,
        @AuthenticationPrincipal Member loginMember) {
        Information info = informationService.findVisibleInformationById(id);

        model.addAttribute("post", InformationView.from(info));

        List<CommentView> visibleComments = info.getComments().stream()
            .filter(comment -> !comment.getHideStatus()).map(CommentView::from)
            .collect(Collectors.toList());

        model.addAttribute("comments", visibleComments);
        model.addAttribute("loginMemberId", loginMember != null ? loginMember.getId() : null);
        model.addAttribute("loginMember", loginMember);

        return "information/informationdetail";
    }

    @GetMapping("/write")
    public String showWriteForm(Model model) {
        InformationRequest dto = new InformationRequest();
        model.addAttribute("informationRequest", dto);

        return "information/informationWrite";
    }

    @PostMapping
    public String saveInformation(@ModelAttribute InformationRequest request,
        @AuthenticationPrincipal Member loginMember) {
        if (loginMember == null) {
            return "redirect:/login";
        }

        request.setMemberId(loginMember.getId());
        Information saved = informationService.addInformation(request);

        return "redirect:/api/information/" + saved.getInformationId();
    }

    @PostMapping("/comments")
    public String addComment(@RequestParam Long informationId,
        @AuthenticationPrincipal Member loginMember, @RequestParam String content) {
        List<String> allowedRoles = List.of("ADMIN", "MASTER");

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!allowedRoles.contains(loginMember.getRole())) {
            throw new GlobalException(GlobalExceptionType.FORBIDDEN_COMMENT);

        }

        CommentRequest request = new CommentRequest(content);
        commentService.addComment(informationId, loginMember.getId(), request);

        return "redirect:/api/information/" + informationId;
    }

    @GetMapping
    public String showList(@RequestParam(defaultValue = "0") int page, Model model,
        @AuthenticationPrincipal Member loginMember) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createAt").descending());
        Page<Information> pageInfo = informationService.findAllPaged(pageable);

        List<InformationView> posts = pageInfo.getContent().stream().map(InformationView::from)
            .toList();

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", pageInfo.getNumber());
        model.addAttribute("totalPages", pageInfo.getTotalPages());

        return "information/information";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
        @AuthenticationPrincipal Member loginUser) {
        Information information = informationService.findInformationById(id);

        if (!information.getMember().getId().equals(loginUser.getId())) {
            throw new GlobalException(GlobalExceptionType.ONLY_AUTHOR_CAN_UPDATE);
        }

        InformationRequest request = new InformationRequest(information.getTitle(),
            information.getContent(), information.getMember().getId());

        model.addAttribute("informationRequest", request);
        model.addAttribute("informationId", id);

        return "information/informationEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateInformation(@PathVariable Long id,
        @ModelAttribute InformationRequest request) {
        informationService.updateInformation(id, request);

        return "redirect:/api/information/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteInformation(@PathVariable Long id, @AuthenticationPrincipal Member member) {
        Information info = informationService.findInformationById(id);
        if (!info.getMember().getId().equals(member.getId())) {
            throw new GlobalException(GlobalExceptionType.ONLY_AUTHOR_CAN_DELETE);
        }
        informationService.deleteInformation(id);

        return "redirect:/api/information";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String status,
        @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page,
        Model model) {
        Status filterStatus = null;
        if ("SOLVED".equalsIgnoreCase(status)) {
            filterStatus = Status.SOLVED;
        } else if ("UNSOLVED".equalsIgnoreCase(status)) {
            filterStatus = Status.UNSOLVED;
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by("createAt").descending());
        Page<Information> filtered = informationService.searchVisibleByStatusAndKeywordPaged(
            filterStatus, keyword, pageable);

        List<InformationView> posts = filtered.stream().map(InformationView::from).toList();

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", filtered.getNumber());
        model.addAttribute("totalPages", filtered.getTotalPages());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);

        return "information/information";
    }

    @PostMapping("/solve/{id}")
    public String solveInformation(@PathVariable Long id, @AuthenticationPrincipal Member member) {
        informationService.markAsSolved(id, member.getId());

        return "redirect:/api/information/" + id;
    }

    @PostMapping("/comments/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId,
        @AuthenticationPrincipal Member loginMember) {

        Comment comment = commentService.findById(commentId);
        Information info = comment.getInformation();

        if (!info.getMember().getId().equals(loginMember.getId())) {
            throw new GlobalException(GlobalExceptionType.ONLY_AUTHOR_CAN_DELETE);
        }

        commentService.deleteComment(commentId);

        return "redirect:/api/information/" + info.getInformationId();
    }
}
