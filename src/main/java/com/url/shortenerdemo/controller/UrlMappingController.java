package com.url.shortenerdemo.controller;

import com.url.shortenerdemo.dtos.ClickEventDTO;
import com.url.shortenerdemo.dtos.UrlMappingDTO;
import com.url.shortenerdemo.models.User;
import com.url.shortenerdemo.service.UrlMappingService;
import com.url.shortenerdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlMappingController {
    private final UrlMappingService urlMappingService;
    private final UserService userService;

    @PostMapping("/shorten")
    // NOTE: Removed @PreAuthorize to allow anonymous access (as per WebSecurityConfig)
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String,String> request,
                                                        Principal principal) {
        String originalUrl = request.get("originalUrl");
        User user;

        if (principal != null) {
            // SCENARIO 1: User is logged in (Principal exists)
            user = userService.findByUsername(principal.getName());
        } else {
            // SCENARIO 2: Anonymous user (Public request)
            // FIX: Assign to a dedicated Anonymous User ID (e.g., ID 1L)
            // CRITICAL: Ensure an entry exists in your 'users' table with this ID.
            final Long ANONYMOUS_USER_ID = 1L;
            user = userService.findById(ANONYMOUS_USER_ID);

            // Add a safety check in case the system user is missing
            if (user == null) {
                return ResponseEntity.status(500).build();
            }
        }

        String username = (principal != null) ? principal.getName() : "Anonymous";
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDTO);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/stats/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl,
                                                               @RequestParam("startDate") String startDate,
                                                               @RequestParam("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        List<ClickEventDTO> clickEventDTOS = urlMappingService.getClickEventsByDate(shortUrl, start,end);
        return  ResponseEntity.ok(clickEventDTOS);
    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClickByDate(Principal principal,
                                                                    @RequestParam("startDate") String startDate,
                                                                    @RequestParam("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        User user = userService.findByUsername(principal.getName());
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        Map<LocalDate, Long> totalClicks = urlMappingService.getTotalClickByUserAndDate(user, start.atStartOfDay(), end.atStartOfDay());
        return  ResponseEntity.ok(totalClicks);
    }
}
