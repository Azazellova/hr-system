package ru.hr.backend.clients.employee;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.hr.backend.clients.employee.dto.EmployeeRequest;
import ru.hr.backend.clients.employee.dto.EmployeeResponse;
import java.util.List;

@Service
public class EmployeeServiceClient {
    private final RestClient client;
    public EmployeeServiceClient(RestClient.Builder builder, @Value("${clients.employee.url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }
    public EmployeeResponse getEmployeeById(Long id) {
        return client.get().uri("/employees/{id}", id).retrieve().body(EmployeeResponse.class);
    }
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        return client.post().uri("/employees").body(request).retrieve().body(EmployeeResponse.class);
    }
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        return client.put().uri("/employees/{id}", id).body(request).retrieve().body(EmployeeResponse.class);
    }
    public List<EmployeeResponse> getAllEmployees() {
        return client.get().uri("/employees").retrieve().body(new ParameterizedTypeReference<List<EmployeeResponse>>() {});
    }
}
