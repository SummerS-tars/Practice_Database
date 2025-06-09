# API Testing Guide for LLM Evaluate Backend

## Base URL Configuration
- **Base URL**: `http://localhost:8080/api`
- **Context Path**: `/api` (configured in application.yml)
- **API Version**: `/v1`

## Endpoint: Import Raw Questions

### URL
```
POST http://localhost:8080/api/v1/raw-questions/import?sourcePlatform=stackoverflow
```

### Request Configuration in Apifox

1. **Method**: POST
2. **URL**: `http://localhost:8080/api/v1/raw-questions/import`
3. **Query Parameters**:
   - `sourcePlatform`: `stackoverflow` (optional, defaults to "stackoverflow")

4. **Headers**:
   ```
   Content-Type: multipart/form-data
   ```

5. **Body** (Form Data):
   - **Key**: `file`
   - **Type**: File
   - **Value**: Upload an XML file (e.g., StackOverflow posts XML)

### Sample XML File Content
For testing, create a file named `test_questions.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<posts>
  <row Id="1" PostTypeId="1" Title="Test Question 1" Body="This is a test question content" Score="5" Tags="&lt;java&gt;&lt;spring&gt;" />
  <row Id="2" PostTypeId="1" Title="Test Question 2" Body="Another test question" Score="3" Tags="&lt;javascript&gt;" />
</posts>
```

### Expected Response
```json
{
  "success": true,
  "message": "Import completed successfully",
  "totalProcessed": 2,
  "successCount": 2,
  "failedCount": 0,
  "errors": []
}
```

## Other Available Endpoints

### 1. Get Raw Questions (Paginated)
```
GET http://localhost:8080/api/v1/raw-questions?page=0&size=20&sortBy=id&sortDirection=asc
```

### 2. Get Raw Question by ID
```
GET http://localhost:8080/api/v1/raw-questions/{id}
```

### 3. Update Raw Question Status
```
PATCH http://localhost:8080/api/v1/raw-questions/{id}/status
Content-Type: application/json

{
  "status": "CONVERTED"
}
```

### 4. Get Answers for a Raw Question
```
GET http://localhost:8080/api/v1/raw-questions/{id}/answers?page=0&size=20
```

### 5. Import Raw Answers
```
POST http://localhost:8080/api/v1/raw-answers/import?sourcePlatform=stackoverflow
Content-Type: multipart/form-data

file: [XML file with answers]
```

### 6. Health Check Endpoint
```
GET http://localhost:8080/api/actuator/health
```

## Troubleshooting

### Common Issues:

1. **"No static resource" error**: 
   - Check if URL is correct (should include `/api/v1/`)
   - Ensure app is running on port 8080
   - Verify the endpoint is accessible

2. **404 Not Found**:
   - Make sure the application started successfully
   - Check logs for any startup errors
   - Verify the controller is being loaded

3. **400 Bad Request**:
   - Ensure you're sending a file parameter
   - Check Content-Type is multipart/form-data
   - Verify the XML file format is correct

### Testing Steps:
1. Start the application: `mvn spring-boot:run`
2. Check health: `GET http://localhost:8080/api/actuator/health`
3. Test import endpoint with proper file upload
4. Check application logs for any errors 