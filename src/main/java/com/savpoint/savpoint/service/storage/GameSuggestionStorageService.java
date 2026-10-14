package com.savpoint.savpoint.service.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savpoint.savpoint.dtos.GameSuggestionHistory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameSuggestionStorageService {

    private final ObjectMapper objectMapper;
    private final String suggestionsDir;

    public GameSuggestionStorageService(
            ObjectMapper objectMapper,
            @Value("${app.suggestions.storage.dir:suggestions}") String suggestionsDir
    ) {
        this.objectMapper = objectMapper;
        this.suggestionsDir = suggestionsDir;
        ensureDirectoryExists();
    }

    public void saveSuggestion(GameSuggestionHistory history) throws IOException {
        Path dirPath = Paths.get(suggestionsDir);
        ensureDirectoryExists();

        Path filePath = dirPath.resolve(sanitizeEmail(history.userEmail()) + ".json");
        List<GameSuggestionHistory> allHistories = loadUserHistories(history.userEmail());
        allHistories.add(history);

        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(filePath.toFile(), allHistories);
    }

    public List<GameSuggestionHistory> getUserHistories(String userEmail) throws IOException {
        return loadUserHistories(userEmail);
    }

    private List<GameSuggestionHistory> loadUserHistories(String userEmail) throws IOException {
        Path filePath = Paths.get(suggestionsDir, sanitizeEmail(userEmail) + ".json");

        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(
                filePath.toFile(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, GameSuggestionHistory.class)
        );
    }

    private String sanitizeEmail(String email) {
        return email.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(suggestionsDir));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar diretório de armazenamento de sugestões: " + e.getMessage(), e);
        }
    }
}
