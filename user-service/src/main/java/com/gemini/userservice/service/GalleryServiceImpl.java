package com.gemini.userservice.service;

import com.gemini.userservice.dto.GalleryDto;
import com.gemini.userservice.dto.GeminiDto;
import com.gemini.userservice.dto.ProfileResponseDto;
import com.gemini.userservice.dto.response.ResponseGalleryDetailDto;
import com.gemini.userservice.dto.response.ResponseGalleryPageDto;
import com.gemini.userservice.dto.response.ResponseGalleryRankingDto;
import com.gemini.userservice.entity.Gallery;
import com.gemini.userservice.entity.Gemini;
import com.gemini.userservice.entity.Like;
import com.gemini.userservice.entity.UserInfo;
import com.gemini.userservice.repository.GalleryRepository;
import com.gemini.userservice.repository.LikeRepository;
import com.gemini.userservice.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService{

    private final GalleryRepository galleryRepository;

    private final UserInfoRepository userInfoRepository;

    private final LikeRepository likeRepository;

    public ResponseGalleryPageDto getGalleryPage(Integer page, Integer size) {

        List<Gallery> galleries = galleryRepository.findAll();
        if (galleries.size() > 0) {
            if (galleries.size() < size) {
                size = galleries.size();
            }
            Pageable pageable = PageRequest.of(page, size);
            int start = (int)pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), size);
            List<GalleryDto> galleryDtos = new ArrayList<>();
            for (int i = start; i < end; i++) {
                Gallery gallery = galleries.get(i);
                GalleryDto galleryDto = new GalleryDto(gallery, gallery.getGemini());
                galleryDtos.add(galleryDto);
            }
            Page<GalleryDto> galleryPage = new PageImpl<>(galleryDtos, pageable, galleries.size());
            ResponseGalleryPageDto responseGalleryPageDto = new ResponseGalleryPageDto(galleryPage);
            return responseGalleryPageDto;
        }
        ResponseGalleryPageDto responseGalleryPageDto = new ResponseGalleryPageDto();
        return responseGalleryPageDto;
    }

    public ResponseGalleryRankingDto getDailyGallery() {

        return null;
    }

    public ResponseGalleryRankingDto getWeeklyGallery() {

        return null;
    }

    public ResponseGalleryDetailDto getGalleryDetail(String username, Long galleryNo) {

        Optional<UserInfo> me = userInfoRepository.findByUsername(username);
        Gallery gallery = galleryRepository.findByGalleryNo(galleryNo);
        Gemini gemini = gallery.getGemini();
        Optional<Like> isLiked = likeRepository.findByUserInfoAndGemini(me.get(), gemini);
        Boolean liked = isLiked.isPresent();
        UserInfo producer = gemini.getUserInfo();

        ResponseGalleryDetailDto responseGalleryDetailDto = new ResponseGalleryDetailDto(producer, gemini, liked);
        return responseGalleryDetailDto;
    }

    public String likeGallery(String username, Long galleryNo) {

        Optional<UserInfo> userInfo = userInfoRepository.findByUsername(username);
        Gallery gallery = galleryRepository.findByGalleryNo(galleryNo);
        Optional<Like> isLiked = likeRepository.findByUserInfoAndGemini(userInfo.get(), gallery.getGemini());
        if (isLiked.isPresent()) {
            return "fail";
        }
        Like like = Like.builder()
                    .gemini(gallery.getGemini())
                    .userInfo(userInfo.get())
                    .build();
        likeRepository.save(like);
        return "success";
    }

    public String cancelGallery(String username, Long galleryNo) {

        Optional<UserInfo> userInfo = userInfoRepository.findByUsername(username);
        Gallery gallery = galleryRepository.findByGalleryNo(galleryNo);
        Optional<Like> isLiked = likeRepository.findByUserInfoAndGemini(userInfo.get(), gallery.getGemini());
        if (isLiked.isPresent()) {
            likeRepository.delete(isLiked.get());
            return "success";
        }
        return "fail";
    }
}
