# API Design, Versioning, Security, and Best Practices

## What is an API?

An API (Application Programming Interface) is a set of rules and protocols that allows different software applications to communicate with each other and exchange data. 

**Refer to AWS API Gateway documentation for more depth explanation:** https://aws.amazon.com/what-is/api/**



## Things to consider when designing an API:

1. **Many users will be using it.** : Design your API with a large user base in mind, ensuring it's easy to use and provides a good user experience.

2. **Code is for now, API is forever** : Once an API is released, it should be maintained and supported for a long time. Avoid making breaking changes to the API unless absolutely necessary.

3. **Cannot modify existing fields in Request/Response**: Once an API is released, you cannot modify existing fields in the request. If you need to add new functionality, consider adding new fields or endpoints instead of modifying existing ones.

4. **Inform users/customers about new versions**: When releasing a new version of an API, make sure to inform users and customers about the changes and provide clear documentation on how to migrate to the new version.


5. **Keep old version running until all users have migrated**: When releasing a new version of an API, keep the old version running until all users have migrated to the new version. This will ensure that users have time to update their applications and avoid any disruptions in service.


## API Lifecycle across Teams

APIs are often span across multiple teams - from backend and frontend to DevOps and QA. It is important to have a clear understanding of the API lifecycle and how it affects different teams.


1. **Design** : Define DTOs and OpenAPI specifications and all contracts so that all teams have a clear understanding of the API's functionality and requirements.

2. **Development** : Implement the API according to the design specifications, build the required controllers, services, and data access layers, and ensure that the API is adhering to the defined contracts.

3. **Testing** : Test the API to ensure that it is functioning as expected and meets the defined requirements. This includes unit testing, integration testing, and end-to-end testing.

4. **Deployment** : Deploy APIs in CI/CD pipelines with canary releases and blue-green deployments to ensure that the API is deployed in a controlled and safe manner. (Canary releases allow for gradual rollouts of new versions, while blue-green deployments allow for easy rollback in case of issues.)

5. **Monitoring** : Track metrics like latency, uptime, and error rates using tools like Prometheus, Grafana, etc. This will help identify any issues with the API and ensure that it is performing as expected.

6. **Versioning/Maintenance** : Introduce changes carefully while maintaining backward compatibility. 

7. **Deprecation** : When deprecating an API version, provide clear communication to users and customers about the timeline for deprecation and any necessary steps they need to take to migrate to a new version.

## Advance REST Principles & Sensitive Operations

### Resource Naming : Best Practices

**Bad Urls:**
 - `/getUserDetails?id=123`
 - `/addBooktoShelf`
 - `/cancel-order`

**Good Urls:**
 - `/api/v1/users/123`
 - **POST** `/api/v1/shelves/{id}/books`
 - **POST** `/api/v1/orders/{id}/cancel`



Use plural nouns. Avoid verbs in path.
Use HTTP verbs to describe the action

**Example:**
 - **POST** `/api/v1/users` - Create a new user
 - **GET** `/api/v1/users/{id}` - Get user details
 - **PATCH** `/api/v1/users/{id}` - Update user details
 - **DELETE** `/api/v1/users/{id}` - Delete a user


**Why plural nouns?** : REST sees data as resource and not functions. Think of a resource like an object or row in a table.The HTTP methods describe what to do with that resource. So, the resource should be a noun and not a verb.

**What if you need the action to be expressed in the URL?:**

**Option 1**: Treat it as a sub-resource. For example, if you want to cancel an order, you can treat it as a sub-resource of the order and use the following URL: `/api/v1/orders/{id}/cancel`. This indicates that you are performing an action on a specific order resource.

**Option 2**: Use intent in body with  POST. For example, if you want to cancel an order, you can use the following URL: `/api/v1/orders/{id}` and include a request body like this:

```json
{
  "orderId": "123",
  "action": "cancel"
}
```

### Idempotency Key

Client generated unique identifier (UUID) that tells the server that these multiple requests are of the same intent/operation, so that the server can ignore duplicate requests and ensure that the operation is only performed or processed once.


**Usage**:
When making a request that should be idempotent, include the `Idempotency-Key` header with a unique UUID.

**Headers:**
* - `Idempotency-Key: <UUID>`


---


## DTO Patterns - Designing Contracts

**Include**:

1. All required fields upfront
2. Future additions should be nullable or optional
3. Avoid leaking sensitive data (API keys, passwords, etc.)

Try to use enums with meaning instead of "1" or "0" for status fields. For example, instead of using a boolean to represent the status of an order, use an enum with values like "PENDING", "PROCESSING", "COMPLETED", etc. This makes the API more readable and easier to understand.


## Error Handling

Always use a structured error response even for 4xx or 5xx errors in both client and server side. This will help clients to handle errors more effectively and provide a better user experience.

Example error response:

```json
{
    "timestamp": "2024-06-01T12:00:00Z",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid request parameters",
    "path": "/api/v1/users"
}
```

### Common HTTP Status Codes

| Category | Status Code | Meaning | Example |
|----------|-------------|---------|---------|
| 2xx  | 200-299 | Success |  200 OK, 201 Created, 204 No Content |
| 3xx | 300-399 | Redirection | 301 Moved Permanently, 302 Found, 304 Not Modified |
| 4xx | 400-499 | Client Error | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 409 Conflict |
| 5xx | 500-599 | Server Error | 500 Internal Server Error, 502 Bad Gateway, 503 Service Unavailable, 504 Gateway Timeout |


## API Versioning

- **URL Versioning**: Include the version number in the URL path (e.g., `/api/v1/resource`). This is the most common and recommended approach for versioning APIs.

- **Header Versioning**: Include the version number in custom headers like `X-API-Version`. This approach allows for more flexibility in versioning but can be less discoverable for clients.

- **Media Type Versioning**: Use the `Accept` header to specify the version of the API (e.g., `Accept: application/vnd.example.v1+json`). This approach allows for more granular control over versioning but can be more complex to implement.


### Best Practices for API Versioning

- Introduce breaking changes only in new versions.
- Deprecate but support old versions for a reasonable period of time.
- Document changes and provide clear migration guides for users.

---


## Filtering , Sorting and Pagination

- **Filtering**: Allow clients to filter results based on specific criteria using query parameters. For example, `/api/v1/users?status=active` to get only active users.

- **Sorting**: Allow clients to sort results based on specific fields using query parameters. For example, `/api/v1/users?sort=createdAt&order=desc` to get users sorted by creation date in descending order.

- **Pagination**: Implement pagination to limit the number of results returned in a single response. Use query parameters like `page` and `limit` to control pagination. For example, `/api/v1/users?page=2&limit=10` to get the second page of users with a limit of 10 users per page.

```json
Paginated Response Example:
{
    "data": [..],
    "page": 2,
    "limit": 10,
    "totalPages": 5,
    "totalItems": 50
}
```

## API Security Considerations

### Secure Contract Design
- Don't expose sensitive data 
- Avoid sending tokens in  URL params.
- Do not rely on frontend to validate inputs
- Authentication is about Identity
- Authorization is about Permissions


### Common Auth Methods

- **API Key:** A unique key provided to clients to authenticate their requests. This is a simple method but can be less secure if the key is exposed.

- **Session Cookies:** A method where the server creates a session for the client and stores the session ID in a cookie. This is commonly used in web applications but can be vulnerable to CSRF attacks.

- **JWT (JSON Web Token):** A compact, URL-safe token that is used for authentication and authorization. It contains claims about the user and can be verified using a secret or public key.

## Throttling vs Rate Limiting

**Rate Limiting**: Restricting the number of requests a client can make to an API within a specific time frame. This helps prevent abuse and ensures fair usage of the API.



**Example**: You can call an api `/api/v1/users` a max of 1000 times per user and when exceeded return `429 Too Many Requests` with a message like "You have exceeded the maximum number of requests allowed. Please try again later."

Your system can also return headers like:

```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 60 //Time in seconds until the rate limit resets
```

----


**Throttling**: It is about slowing down requests when the traffic is too high or resources are under pressure. It can be implemented by introducing delaying the requests, queuing requests, or returning dropping the requests over a certain threshold. 

Returning a `503 Service Unavailable` status code with a message like "The server is currently overloaded. Please try again later." can be an example of throttling.


## Monitoring and Observability

- **Monitoring**: Watching known metrics like latency, error rates, and uptime to ensure the API is performing as expected. Tools like Prometheus, Grafana, and AWS CloudWatch can be used for monitoring.

- **Observability**: Ability to answer unknown questions about the system by collecting and analyzing logs, metrics, and traces. This helps in understanding the behavior of the API and identifying issues that may not be apparent through monitoring alone.

**Must have metrics for APIs**:
- Latency (avg,P100 , P99,  P95)
- Error rates by endpoint (count of 4xx, 5xx errors)
- Traffic by api version