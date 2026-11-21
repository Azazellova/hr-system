package ru.hr.backend.clients.organization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.hr.backend.clients.organization.dto.DepartmentResponse;
import ru.hr.backend.clients.organization.dto.PositionResponse;
@Service
public class OrganizationServiceClient {
    private final RestClient client;
    public OrganizationServiceClient(RestClient.Builder builder, @Value("${clients.organization.url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }
    public DepartmentResponse getDepartment(Long id) {
        return client.get().uri("/departments/{id}", id).retrieve().body(DepartmentResponse.class);
    }
    public PositionResponse getPosition(Long id) {
        return client.get().uri("/positions/{id}", id).retrieve().body(PositionResponse.class);
    }
}
