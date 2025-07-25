Date:25/07/19

无法运行 JwtutilApplication.java

POST /login
{"username": "testuser", "password": "password"}
should return the token 
GET /hello
Authorization: Bearer {token}
should return “Hello, Authenticated User!” 

项目结构

Spring Boot 
── SecurityConfig (安全设置)
── JwtUtil (token 发放与验证)
── JwtAuthenticationFilter (请求的 Jwt 校验)
── UserDetailsService (获取用户信息)
── AuthController (登录 API)
── SampleController (需要认证的 API)
