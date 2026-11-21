package ru.hr.backend.clients.document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.hr.backend.clients.document.dto.HiringDocumentRequest;
import ru.hr.backend.clients.document.dto.DocumentResponse;
import java.util.List;
@Service
public class DocumentServiceClient {
    private final RestClient client;
    public DocumentServiceClient(RestClient.Builder builder, @Value("${clients.document.url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }
    public List<DocumentResponse> createHiringDocuments(HiringDocumentRequest request) {
        return client.post().uri("/api/documents/hiring").body(request).retrieve()
                .body(new ParameterizedTypeReference<List<DocumentResponse>>() {});
    }
}
