# Controller_Dto_Service

发布日期：2025年7月25日

主题：您将了解应用程序的三层架构，掌握Spring Boot的依赖注入，Spring Boot的控制器（Controller）、数据传输对象（Dto）和服务（Service），学习创建Spring Boot后端。

## 三层架构

我们将应用程序分为三层：表示层、逻辑层和数据层。表示层处理数据以实现其可视化，由于我们将应用程序分为前端和后端两个部分，因此前端部分已经属于表示层。但是在后端部分，我们也可以准备数据并将其发送到前端，这部分是控制器。控制器是前端的入口点，它将调用各种服务来返回到前端的响应。逻辑层顾名思义，包含了业务逻辑部分，这部分是服务。对于社交网站，它将包含向每个用户返回正确消息的逻辑，要返回的图像，以及如何在添加新朋友或创建消息时创建链接。最后是数据层，包含内部数据结构，这部分是数据传输对象。数据保存在数据库中，从数据库中获取数据。

## Http请求

现在我们编写一个控制器，它将返回消息和图像的值，以及信息和图像的创建，另一个控制器将管理用户。我们希望加载社交网络的初始界面，查看所有社区帖子。当前端调用 GET /v1/community/messages时，所有社区消息均被回复。我们在每个入口点，每种方法都写上路的终点。@ResponseEntity内部包含将返回的Http代码和详细信息。需要接收一条消息并将其保存在数据库中。@RequestMapping是一个用于定义请求路径的注解，常用于@Controller类或方法上，常用于标识控制器的根路径或为某个方法指定访问路径。

```java
// CommunityController.java
@RestController
@RequestMapping("/v1/community")
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getCommunityMessages(
            @RequestParam(value = "page", defaultValue = "0") int page) {
        return ResponseEntity.ok(communityService.getCommunityMessages(user, page));
    }


    @GetMapping("/images")
    public ResponseEntity<List<ImageDto>> getCommunityImages(
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(communityService.getCommunityImages(userDto, page));
    }
}
```

当 URL 路径中包含变量时，就可以用 @PathVariable 把它提取出来。控制器方法会自动把变量赋值给参数 userid。

```java
// UserController.java
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileDto> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PostMapping("/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long friendId) {
        userService.addFriend(userDto, friendId);
        return ResponseEntity.noContent().build();
    }

```

## 处理传入参数

@RequestParam用于获取 URL 查询参数中的值（也就是 ?key=value这种形式的参数），并将其注入到方法参数中。@RequestBody MessageDto messageDto表示前端以 JSON 格式提交的数据会自动绑定到 messageDto 对象中。communityService.postMessage(user, messageDto)调用服务层的方法保存消息，并返回一个消息对象。

```java
    @PostMapping("/messages")
    public ResponseEntity<MessageDto> postMessage(@RequestBody MessageDto messageDto) {
        return ResponseEntity.created(URI.create("/v1/community/messages"))
                .body(communityService.postMessage(user, messageDto));
    }

    @PostMapping("/images")
    public ResponseEntity<ImageDto> postImage(
            @RequestParam MultipartFile file,
            @RequestParam(value = "title") String title
    ) throws IOException {
        return ResponseEntity.created(URI.create("/community/images"))
                .body(communityService.postImage(userDto, file, title));
    }
```

内容

```java
//ImageDto.java
package com.sergio.socialnetwork.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {

    private Long id;
    private String title;
    private String path;
    private UserSummaryDto userDto;
    private LocalDateTime createdDate;

}
```

## 建立联系

与Controller类相同，Service 类要加上 @Service 或 @Component注解。让我们从社区服务开始，创建四个与我在控制器中创建方法类似的方法。我们需要让控制器和服务建立联系。

## 构建控制器



## 结尾



## 代码

```java
//CommunityController.java
package com.sergio.socialnetwork.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.sergio.socialnetwork.dto.ImageDto;
import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.services.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/community")
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getCommunityMessages(
            @AuthenticationPrincipal UserDto user,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        return ResponseEntity.ok(communityService.getCommunityMessages(user, page));
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDto> postMessage(@AuthenticationPrincipal UserDto user,
                                                  @RequestBody MessageDto messageDto) {
        return ResponseEntity.created(URI.create("/v1/community/messages"))
                .body(communityService.postMessage(user, messageDto));
    }

    @PostMapping("/images")
    public ResponseEntity<ImageDto> postImage(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam MultipartFile file,
            @RequestParam(value = "title") String title
    ) throws IOException {
        return ResponseEntity.created(URI.create("/community/images"))
                .body(communityService.postImage(userDto, file, title));
    }

    @GetMapping("/images")
    public ResponseEntity<List<ImageDto>> getCommunityImages(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(communityService.getCommunityImages(userDto, page));
    }
}
```

```java
//UserController.java
package com.sergio.socialnetwork.controllers;

import java.util.List;

import com.sergio.socialnetwork.dto.ProfileDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileDto> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PostMapping("/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@AuthenticationPrincipal UserDto userDto,
                                          @PathVariable Long friendId) {
        userService.addFriend(userDto, friendId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<UserSummaryDto>> searchUsers(@RequestParam(value = "term") String term) {
        return ResponseEntity.ok(userService.searchUsers(term));
    }
}

```

```java

```

```java
// MessageDto.java
package com.sergio.socialnetwork.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    private Long id;
    private String content;
    private UserSummaryDto userDto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdDate;

}
```



