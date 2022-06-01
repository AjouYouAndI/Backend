package org.youandi.youandi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.youandi.youandi.advice.CAuthenticationEntryPointException;
import org.youandi.youandi.advice.CPostNotExistedException;
import org.youandi.youandi.advice.CRegionFailedException;
import org.youandi.youandi.advice.CUsernameNotFoundException;
import org.youandi.youandi.domain.Post;
import org.youandi.youandi.domain.User;
import org.youandi.youandi.dto.PostRequestDto;
import org.youandi.youandi.repository.PostRepository;
import org.youandi.youandi.repository.UserRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    public Post createPost(@NotNull PostRequestDto postRequestDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        String region = getRegion(postRequestDto.getLatitude(), postRequestDto.getLongitude());
        if(region==""){
            throw new CRegionFailedException();
        }
        Post post = new Post(postRequestDto, user, region);
        postRepository.save(post);
        return post;
    }

    public String getRegion(double latitude, double longitude){
        String REST_KEY = "f1003c1b0aeede709a2b8aa8d297e4b7";
        String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=" + longitude + "&y=" + latitude;

        BufferedReader br = null;
        JSONObject obj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        URL converted_url;
        try {
            converted_url = new URL(url);
            URLConnection conn = converted_url.openConnection();
            conn.setRequestProperty("Authorization", "KakaoAK " + REST_KEY);
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            if(br != null) obj = mapper.readValue(br, JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        KakaoGeoRes bodyJson = null;
        try {
            bodyJson = objectMapper.readValue(obj.toString(), KakaoGeoRes.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(bodyJson==null){
            return "";
        }
        else if(bodyJson.getDocuments().get(0).getRegion_4depth_name()==""){
            return bodyJson.getDocuments().get(0).getRegion_3depth_name().toString();
        }else{
            return bodyJson.getDocuments().get(0).getRegion_4depth_name().toString();
        }
    }

    public Post getPostByIdx (Long id) {
        Post post = postRepository.findById(id).orElseThrow(CPostNotExistedException::new);
        return post;
    }

    public List<Post> getPostByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        return postRepository.findAllByUser(user);
    }

    public Long deletePost(Long id, String email) {
        Post post = postRepository.findById(id).orElseThrow(CPostNotExistedException::new);
        User user = userRepository.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new CAuthenticationEntryPointException();
        }
        postRepository.delete(post);
        return id;
    }
}

@Data
class KakaoGeoRes {
    private HashMap<String, Object> meta;
    private List<Documents> documents;
}

@Data
class Documents {
    private String region_type;
    private String address_name;
    private String region_1depth_name;
    private String region_2depth_name;
    private String region_3depth_name;
    private String region_4depth_name;
    private Long code;
    private Double x;
    private Double y;
}

