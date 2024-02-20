This is a very simple RBAC (Role Based Access Control) system whose backend is implemented in Java, whose frontend is implemented in React and that implements the following
# 1 Backend development conventions
Backend development conventions are guaranteed by ConventionAspect.java
## 1.1 HTTP Methods
In this project, only GET, PUT and POST are allowed, which is forced to be implemented by @GetMapping, @PutMapping and @PostMapping, respectively.
```java
@Before("@within(org.springframework.web.bind.annotation.RestController) &&" +
            "!@annotation(org.springframework.web.bind.annotation.GetMapping) &&" +
            "!@annotation(org.springframework.web.bind.annotation.PutMapping) &&" +
            "!@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void checkRequestMappingAnnotation() {
        throw new ConventionViolationException("Invalid annotation on handler methods");
    }
```
## 1.2 API Return Types
APIs are forced to return results of type LcmWebResult
```java
@AfterReturning(value = "@within(org.springframework.web.bind.annotation.RestController) &&" +
            "(@annotation(org.springframework.web.bind.annotation.GetMapping) ||" +
            "        @annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "        @annotation(org.springframework.web.bind.annotation.PostMapping))",
    returning = "result")
    public void afterApiReturning(Object result) {
        if (!(result instanceof LcmWebResult<?>)) {
            throw new ConventionViolationException("Invalid return type");
        }
    }
```
# 2 Token Verification
Token verification is done in TokenInterceptor.java, which is an interceptor, for protected APIs. If a developer doesn't want this verification for an API, he may go to the application.yml file of the backend and exclude the API from token verification.
```yaml
interceptors:
  token:
    exclusion-patterns: ${token_exclusion}
```
# 3 Permission Verification
Permission verification is done in PermissionAspect.java, which is an aspect, for protected APIs.
<br/>To improve the performance, the user's permissions are cached into Redis on succeeding logging in, and when he logs out, the cache will be cleared.
<br/>If a developer doesn't want this verification for an API, he may add the @NoPermissionVerification Annotation on the API.
```java
/**
     * Logs the user in
     * @see LoginRequest
     */
    @NoPermissionVerification
    @PostMapping(value = "/login")
    public LcmWebResult<LoginDto> validateLogin(@RequestBody @Validated LoginRequest request) {
        BizUser user = new BizUser();
        user.setPhoneNo(request.getPhoneNo());
        user.setLoginPwd(request.getPassword());
        LoginDto result = this.userService.userLogin(user, request.getToken(), request.getCaptcha());

        if (null == result) {
            /*
            Failed to log in
             */
            return LcmWebResult.failure(LcmWebStatus.FAILED_TO_LOGIN);
        }
        return LcmWebResult.success(result);
    }
```
# 4 User Management
## 4.1 Get the User List
Click on the menu "Backend Management => User Management" to enter the user list
## 4.2 Grant Roles to a user
Click on the "Roles" Button to configure roles for a user
# 5 Role Management
## 5.1 Get the Role List
Click on the menu "Backend Management => Role Management" to enter the role list
## 5.2 Grant permissions to a role
Click on the "Permissions" Button to configure permissions for a role
# 6 Permission Management
## 6.1 Get the Permission List by HTTP Method and URL Prefix
Click on the menu "Backend Management => Permission Management" to enter the permission list
# 7 Refresh the cached permissions
This is done by clicking the button in the top-left corner of the sidebar when the sidebar is expanded
