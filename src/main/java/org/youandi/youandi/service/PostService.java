package org.youandi.youandi.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.youandi.youandi.advice.CAuthenticationEntryPointException;
import org.youandi.youandi.advice.CPostNotExistedException;
import org.youandi.youandi.advice.CRegionFailedException;
import org.youandi.youandi.advice.CUsernameNotFoundException;
import org.youandi.youandi.domain.Emotion;
import org.youandi.youandi.domain.EmotionType;
import org.youandi.youandi.domain.Post;
import org.youandi.youandi.domain.User;
import org.youandi.youandi.dto.PostRequestDto;
import org.youandi.youandi.repository.EmotionRepository;
import org.youandi.youandi.repository.PostRepository;
import org.youandi.youandi.repository.UserRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final EmotionRepository emotionRepository;
    private final RestTemplate restTemplate;


    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    public Post createPost(@NotNull PostRequestDto postRequestDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        String region = getRegion(postRequestDto.getLatitude(), postRequestDto.getLongitude());
        if(region.equals("")){
            throw new CRegionFailedException();
        }
        Post post = new Post(postRequestDto, user, region);
        postRepository.save(post);
        getEmotion(post);
        return post;
    }

    // 감정 조회: 감정 서버와 통신
    public void getEmotion(Post post) {
        String content = post.getContent();

        String url = "http://3.39.205.52:8000/predict";

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).queryParam("string", content).build();
        ResponseEntity<String> response = restTemplate.getForEntity(uri.toUri(), String.class);
        String emotion = ""+response.getBody();
        saveEmotion(emotion, post);
    }

    public void saveEmotion(String emotion, Post post) {
        EmotionType type = EmotionType.DEFAULT;

        switch (emotion){
            case "공포":
                type = EmotionType.HORROR;
                break;
            case "놀람":
                type = EmotionType.FRIGHTEN;
                break;
            case "분노":
                type = EmotionType.ANGRY;
                break;
            case "슬픔":
                type = EmotionType.SAD;
                break;
            case "행복":
                type = EmotionType.HAPPY;
            case "혐오":
                type = EmotionType.HATE;
                break;
            default:
                break;
        }

        Emotion emotionEntity = new Emotion(type, post);
        emotionRepository.save(emotionEntity);
        post.updateEmotion(type);
    }

    public List<Post> getAroundPosts(double latitude, double longitude) {
        String region = getRegion(latitude, longitude);
        return postRepository.findAllByRegion(region);
    }

    private static String GEOCODE_URL="https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?";
    private static String GEOCODE_USER_INFO="KakaoAK 23b5628ee9c996d7013c4cea7cfc9f4c";



    // 카카오 지오 코딩 api : 경도 위도 -> 주소
    public String getRegion(double latitude, double longitude){
        String REST_KEY = "23b5628ee9c996d7013c4cea7cfc9f4c";

        BufferedReader br = null;
        JSONObject obj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        URL converted_url;
        try {
            converted_url = new URL(GEOCODE_URL+"x="+longitude+"&y="+latitude);
            URLConnection conn = converted_url.openConnection();
            conn.setRequestProperty("Authorization", GEOCODE_USER_INFO);
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            Charset charset = Charset.forName("UTF-8");
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));

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

