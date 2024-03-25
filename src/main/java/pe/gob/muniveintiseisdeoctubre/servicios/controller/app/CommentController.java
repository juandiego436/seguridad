package pe.gob.muniveintiseisdeoctubre.servicios.controller.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AuthDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Comment;
import pe.gob.muniveintiseisdeoctubre.servicios.service.EmailService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.CommentService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.SubjectService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import javax.mail.MessagingException;

@RestController
@RequestMapping("app/comments")
public class CommentController {

    private static final Log LOG = LogFactory.getLog(CommentController.class);

    @Autowired
    private Constants constants;

    @Autowired
    CommentService commentService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    EmailService emailService;

    @PostMapping
    public ResponseEntity<ResponseDTO> create(Authentication authentication, @RequestBody Comment comment) throws MessagingException {
        comment.setUser(((AuthDTO) authentication.getPrincipal()).getAccount().getUser());
        final var subject = subjectService.find(comment.getSubject().getId());
        commentService.createOrUpdate(comment);
        emailService.sendMail(constants.emailMunicipalidad, subject.get().getDescription(), comment.getDescription());
        return ResponseEntity.ok(new ResponseDTO<>(comment, "OK", constants.buildVersion));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        var result = commentService.findAll();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }
}
